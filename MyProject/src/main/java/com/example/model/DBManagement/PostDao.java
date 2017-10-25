package com.example.model.DBManagement;
import com.example.model.*;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 15.10.2017 Рі..
 */
@Component
public class PostDao extends AbstractDao{
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    MultimediaDao multimediaDao;
    @Autowired
    UserDao userDao;
    @Autowired
    LocationDao locationDao;

    //private static PostDao instance;

    /*public static synchronized PostDao getInstance(){
        if(instance==null){
            instance=new PostDao();
        }
        return instance;
    }*/

    //tested
    public void insertNewPost(Post post) throws SQLException, CategoryException, PostException, MultimediaException, UserException {
        try {
            this.getCon().setAutoCommit(false);
            PreparedStatement ps = this.getCon().prepareStatement(
                    "insert into posts(user_id, description, date_time) value (?,?,now());",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, post.getUser().getUserId());
            ps.setString(2,post.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            post.setId(rs.getLong(1));

            categoryDao.addAllCategoriesToPost(post, post.getCategories()); //not sure if it is correct this way
            multimediaDao.addAllMultimediaToPost(post, post.getMultimedia());
            User user=userDao.getUserById(post.getUser().getUserId());
            userDao.addPost(user, post);
            this.tagAllUsers(post, post.getTaggedPeople());
            this.getCon().commit();
        } catch (SQLException e) {
            throw new PostException("Post could not be added. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    //tested
    private void tagAllUsers(Post post, Set<User> set) throws SQLException, PostException {
        try{
            PreparedStatement ps = this.getCon().prepareStatement(
                    "insert into tagged_users(post_id, user_id) values(?,?);");
            for (User user : set) {
                ps.setLong(1,post.getId());
                ps.setLong(2,user.getUserId());
                ps.addBatch();
            }
            ps.executeBatch();
        }catch (SQLException e){
            throw new PostException("Error tagging users. Reason: "+e.getMessage());
        }

    }

    //tested
    public void tagUser(Post post, User user) throws SQLException, PostException {
        try{
            //TODO AM I FORGETTING TO PUT THE TAG IN SOME COLLECTION?
            this.getCon().setAutoCommit(false);
            PreparedStatement ps = this.getCon().prepareStatement(
                    "insert into tagged_users(post_id, user_id) values(?,?);");
            ps.setLong(1,post.getId());
            ps.setLong(2,user.getUserId());
            ps.executeUpdate();
            post.tagUser(user);
            this.getCon().commit();
        }catch (SQLException e){
            throw new PostException("user could not be tagged. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    //tested
    public void addCategoryToPost(Post post, Category category) throws SQLException, PostException {
        PreparedStatement ps = null;
        try {
            this.getCon().setAutoCommit(false);
            ps = this.getCon().prepareStatement(
                    "insert into posts_categories(post_id, category_id) values(?,?);");
            ps.setLong(1, post.getId());
            ps.setLong(2,category.getId());
            ps.executeUpdate();
            post.addCategory(category);
            this.getCon().commit();
        } catch (SQLException e) {
            throw new PostException("Category could not be added to post. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    /*//tested
    public void deletePost(Post post) throws SQLException, PostException {
        PreparedStatement ps = null;
        try {
            this.getCon().setAutoCommit(false);
            ps = this.getCon().prepareStatement(
                    "delete from posts where post_id=?;");
            ps.setLong(1, post.getId());
            ps.executeUpdate();
            this.getCon().commit();
        } catch (SQLException e) {
            throw new PostException("Post could not be deleted. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }*/

    //tested
    public void updateLocation(Post post, Location newLocation) throws SQLException, PostException {
        try{
            this.getCon().setAutoCommit(false);
            PreparedStatement ps = this.getCon().prepareStatement(
                    "update posts set location_id= ?  where post_id= ?;");
            ps.setLong(1, newLocation.getId());
            ps.setLong(2,post.getId());
            ps.executeUpdate();
            post.setLocation(newLocation);
            this.getCon().commit();
        }catch (SQLException e){
            throw new PostException("location could not be updated");
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }

    }

    //tested
    public void incrementLikes(Post post) throws SQLException, PostException {
        try{
            this.getCon().setAutoCommit(false);
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "update posts set likes_count= ?  where post_id= ?;");
            ps.setInt(1, post.getLikesCount()+1);
            ps.setLong(2,post.getId());
            ps.executeUpdate();
            post.setLikesCount(post.getLikesCount()+1);
            this.getCon().commit();
        }catch (SQLException e){
            throw new PostException("could not increment likes. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    //tested
    public void decrementLikes(Post post) throws SQLException, PostException {
        try{
            this.getCon().setAutoCommit(false);
            PreparedStatement ps = this.getCon().prepareStatement(
                    "update posts set likes_count= ?  where post_id= ?;");
            ps.setInt(1, post.getLikesCount()-1);
            ps.setLong(2,post.getId());
            ps.executeUpdate();
            post.setLikesCount(post.getLikesCount()-1);
            this.getCon().commit();
        }catch (SQLException e){
            throw new PostException("couldn't unlike this post. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }


    //tested
    public void incrementDislikes(Post post) throws SQLException, PostException {
        //TODO dislikes should never become less than 0
        try{
            this.getCon().setAutoCommit(false);
            PreparedStatement ps = this.getCon().prepareStatement(
                    "update posts set dislikes_count= ?  where posts.post_id= ?;");
            ps.setInt(1, post.getDislikesCount()+1);
            ps.setLong(2,post.getId());
            ps.executeUpdate();
            post.setDislikesCount(post.getLikesCount()+1);
            this.getCon().commit();
        }catch (SQLException e){
            throw new PostException("could not dislike this post. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    //tested
    public void decrementDislikes(Post post) throws SQLException, PostException {
        try{
            this.getCon().setAutoCommit(false);
            PreparedStatement ps = this.getCon().prepareStatement(
                    "update posts set dislikes_count= ?  where posts.post_id= ?;");
            ps.setInt(1, post.getDislikesCount()-1);
            ps.setLong(2,post.getId());
            ps.executeUpdate();
            post.setDislikesCount(post.getLikesCount()-1);
            this.getCon().commit();
        }catch (SQLException e){
            throw new PostException("could not remove dislike from this post. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    //tested
    public void updateDescription(Post post, String newDescription) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "update posts set description= ?  where posts.post_id= ?;");
        ps.setString(1, newDescription);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    //tested
    public HashSet<Post> getPostsForUser(User user) throws SQLException, VisitedLocationException, UserException, PostException, CategoryException, MultimediaException, LocationException {
        PreparedStatement ps = this.getCon().prepareStatement("select post_id, description, " +
                "likes_count, dislikes_count, date_time from posts where user_id= ?;");
        ps.setLong(1, user.getUserId());
        ResultSet rs = ps.executeQuery();
        HashSet<Post> posts=new HashSet<Post>();
        while(rs.next()){
            Post post=new Post(rs.getLong("post_id"),
                    rs.getString("description"), rs.getInt("likes_count"),
                    rs.getInt("dislikes_count"),rs.getTimestamp("date_time"));
            post.setUser(user);
            post.setLocation(locationDao.getLocationByPost(post));
            post.setCategories(categoryDao.getCategoriesForPost(post));
            post.setMultimedia(multimediaDao.getAllMultimediaForPost(post));
            posts.add(post);
        }
        return posts;
    }


    //tested
    public Post getPostById(long post_id) throws SQLException, PostException {
    	Post post = null;
        PreparedStatement ps = this.getCon().prepareStatement("select description, likes_count, " +
                "dislikes_count, date_time from posts where post_id = ? ;");
        ps.setLong(1, post_id);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
       post =new Post(post_id, rs.getString("description"),rs.getInt("likes_count"),rs.getInt("dislikes_count"),
                rs.getTimestamp("date_time"));
        }
        return post;
    }

    public void addComment(Post postById, Comment c) throws SQLException {
        postById.addComment(c);
    }

    public void deleteComment(Post postById, Comment c) throws SQLException {
        postById.deleteComment(c);
    }
    
}
