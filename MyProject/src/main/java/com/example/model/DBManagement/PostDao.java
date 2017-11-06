package com.example.model.DBManagement;

import com.example.model.*;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * Created by Marina on 15.10.2017 ??..
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
    @Autowired
    TagDao tagDao;
    @Autowired
    CommentDao commentDao;

	// tested
	public void insertNewPost(Post post)
			throws SQLException, CategoryException, PostException, MultimediaException, UserException {
		this.getConnection().setAutoCommit(false);
		PreparedStatement ps=null;
		try {
			Location location=null;
			if(post.getLocation()!=null){
				if(locationDao.existsLocationInDb(post.getLocation())){
					location=locationDao.getLocation(post.getLocation());
					post.setLocation(location);
				}else{
					location=locationDao.insertLocation(post.getLocation());
					post.setLocation(location);
				}

				ps = this.getConnection().prepareStatement(
						"insert into posts(user_id, description, date_time,location_id) value (?,?,now(),?);",
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, post.getUser().getUserId());
				ps.setString(2, post.getDescription());
				ps.setLong(3,post.getLocation().getId());
			}else{
				ps = this.getConnection().prepareStatement(
						"insert into posts(user_id, description, date_time) value (?,?,now());",
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, post.getUser().getUserId());
				ps.setString(2, post.getDescription());
			}
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			post.setId(rs.getLong(1));
			post.setDateTime(getTimeStampFromPost(post));
			categoryDao.addAllCategoriesToPost(post, post.getCategories()); // not sure if it is correct this way
			multimediaDao.addAllMultimediaToPost(post, (HashSet<Multimedia>) post.getMultimedia());
			if (null != post.getVideo()) {
				multimediaDao.addVideoToPost(post, post.getVideo());
			}
			User user = userDao.getUserById(post.getUser().getUserId());
			userDao.addPost(user, post);
			tagDao.addTagsToPost(post, post.getTags());
			if(post.getLocation()!=null){
				locationDao.insertVisitedLocation(user.getUserId(), post.getLocation().getId());
			}
			this.tagAllUsers(post, post.getTaggedPeople());
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("Post could not be added. Reason: " + e.getMessage());
		} catch (LocationException e) {
			e.printStackTrace();
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

	private Timestamp getTimeStampFromPost(Post post) {
		Timestamp timestamp = null;
		try {
			PreparedStatement ps = this.getConnection().prepareStatement("SELECT date_time FROM posts WHERE post_id=?");
			ps.setLong(1, post.getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			timestamp = rs.getTimestamp("date_time");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	// tested
	private void tagAllUsers(Post post, Set<User> set) throws SQLException, PostException {
		try {
			PreparedStatement ps = this.getConnection()
					.prepareStatement("insert into tagged_users(post_id, user_id) values(?,?);");
			for (User user : set) {
				ps.setLong(1, post.getId());
				ps.setLong(2, user.getUserId());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			throw new PostException("Error tagging users. Reason: " + e.getMessage());
		}

	}

	// tested
	public void tagUser(Post post, User user) throws SQLException, PostException {
		try {
			// TODO AM I FORGETTING TO PUT THE TAG IN SOME COLLECTION?
			this.getConnection().setAutoCommit(false);
			PreparedStatement ps = this.getConnection()
					.prepareStatement("insert into tagged_users(post_id, user_id) values(?,?);");
			ps.setLong(1, post.getId());
			ps.setLong(2, user.getUserId());
			ps.executeUpdate();
			post.tagUser(user);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("user could not be tagged. Reason: " + e.getMessage());
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

	// tested
	public void addCategoryToPost(Post post, Category category) throws SQLException, PostException {
		PreparedStatement ps = null;
		try {
			this.getConnection().setAutoCommit(false);
			ps = this.getConnection()
					.prepareStatement("insert into posts_categories(post_id, category_id) values(?,?);");
			ps.setLong(1, post.getId());
			ps.setLong(2, category.getId());
			ps.executeUpdate();
			post.addCategory(category);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("Category could not be added to post. Reason: " + e.getMessage());
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

	/*
	 * //tested public void deletePost(Post post) throws SQLException, PostException
	 * { PreparedStatement ps = null; try {
	 * this.getConnection().setAutoCommit(false); ps =
	 * this.getConnection().prepareStatement( "delete from posts where post_id=?;");
	 * ps.setLong(1, post.getId()); ps.executeUpdate();
	 * this.getConnection().commit(); } catch (SQLException e) { throw new
	 * PostException("Post could not be deleted. Reason: "+e.getMessage()); }finally
	 * { this.getConnection().rollback(); this.getConnection().setAutoCommit(true);
	 * } }
	 */

	// tested
	public void updateLocation(Post post, Location newLocation) throws SQLException, PostException {
		try {
			this.getConnection().setAutoCommit(false);
			PreparedStatement ps = this.getConnection()
					.prepareStatement("update posts set location_id= ?  where post_id= ?;");
			ps.setLong(1, newLocation.getId());
			ps.setLong(2, post.getId());
			ps.executeUpdate();
			post.setLocation(newLocation);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("location could not be updated");
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}

	}

	// tested
	public void incrementLikes(Post post) throws SQLException, PostException {
		try {
			this.getConnection().setAutoCommit(false);
			PreparedStatement ps = this.getConnection()
					.prepareStatement("update posts set likes_count= ?  where post_id= ?;");
			ps.setInt(1, post.getLikesCount() + 1);
			ps.setLong(2, post.getId());
			ps.executeUpdate();
			post.setLikesCount(post.getLikesCount() + 1);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("could not increment likes. Reason: " + e.getMessage());
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

	// tested
	public void decrementLikes(Post post) throws SQLException, PostException {
		try {
			this.getConnection().setAutoCommit(false);
			PreparedStatement ps = this.getConnection()
					.prepareStatement("update posts set likes_count= ?  where post_id= ?;");
			ps.setInt(1, post.getLikesCount() - 1);
			ps.setLong(2, post.getId());
			ps.executeUpdate();
			post.setLikesCount(post.getLikesCount() - 1);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("couldn't unlike this post. Reason: " + e.getMessage());
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

	// tested
	public void incrementDislikes(Post post) throws SQLException, PostException {
		// TODO dislikes should never become less than 0
		try {
			this.getConnection().setAutoCommit(false);
			PreparedStatement ps = this.getConnection()
					.prepareStatement("update posts set dislikes_count= ?  where posts.post_id= ?;");
			ps.setInt(1, post.getDislikesCount() + 1);
			ps.setLong(2, post.getId());
			ps.executeUpdate();
			post.setDislikesCount(post.getLikesCount() + 1);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("could not dislike this post. Reason: " + e.getMessage());
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

    //tested
    public TreeSet<Post> getPostsForUser(User user) throws SQLException, VisitedLocationException, UserException, PostException, CategoryException, MultimediaException, LocationException, CommentException {
        PreparedStatement ps = this.getConnection().prepareStatement("select post_id, description, " +
                "likes_count, dislikes_count, date_time from posts where user_id= ?;");
        ps.setLong(1, user.getUserId());
        ResultSet rs = ps.executeQuery();
        TreeSet<Post> posts=new TreeSet<>();
        while(rs.next()){
            Post post=new Post(rs.getLong("post_id"),
                    rs.getString("description"), rs.getInt("likes_count"),
                    rs.getInt("dislikes_count"),rs.getTimestamp("date_time"));
            post.setUser(user);
            post.setLocation(locationDao.getLocationByPost(post));
            post.setCategories(categoryDao.getCategoriesForPost(post));
            post.setMultimedia(multimediaDao.getAllMultimediaForPost(post));
            post.setComments(commentDao.getCommentsForPost(post));
            post.setTaggedPeople(userDao.getAllTaggedUsersForPost(post));
            post.setVideo(multimediaDao.getVideoForPost(post));
            post.setTags(tagDao.getTagsForPost(post));
            post.setPeopleDisliked(getAllPeopleDisliked(post));
            post.setPeopleLiked(getAllPeopleLiked(post));
            posts.add(post);
        }
        return posts;
    }

	public HashSet<Long> getAllPeopleLiked(Post post) {
		HashSet<Long> peopleLiked=new HashSet<>();
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT user_id FROM posts_reactions WHERE post_id=? AND reaction=1");
			ps.setLong(1,post.getId());
			ResultSet rs=ps.executeQuery();
			while (rs.next()){
				long currentId=rs.getLong("user_id");
				peopleLiked.add(currentId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return peopleLiked;
	}

	public HashSet<Long> getAllPeopleDisliked(Post post) {
		HashSet<Long> peopleDisliked=new HashSet<>();
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT user_id FROM posts_reactions WHERE post_id=? AND reaction=0");
			ps.setLong(1,post.getId());
			ResultSet rs=ps.executeQuery();
			while (rs.next()){
				long currentId=rs.getLong("user_id");
				peopleDisliked.add(currentId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return peopleDisliked;
	}

	// tested
	public void decrementDislikes(Post post) throws SQLException, PostException {
		try {
			this.getConnection().setAutoCommit(false);
			PreparedStatement ps = this.getConnection()
					.prepareStatement("update posts set dislikes_count= ?  where posts.post_id= ?;");
			ps.setInt(1, post.getDislikesCount() - 1);
			ps.setLong(2, post.getId());
			ps.executeUpdate();
			post.setDislikesCount(post.getLikesCount() - 1);
			this.getConnection().commit();
		} catch (SQLException e) {
			throw new PostException("could not remove dislike from this post. Reason: " + e.getMessage());
		} finally {
			this.getConnection().rollback();
			this.getConnection().setAutoCommit(true);
		}
	}

	// tested
	public void updateDescription(Post post, String newDescription) throws SQLException {
		PreparedStatement ps = this.getConnection()
				.prepareStatement("update posts set description= ?  where posts.post_id= ?;");
		ps.setString(1, newDescription);
		ps.setLong(2, post.getId());
		int affectedRows = ps.executeUpdate();
		if (affectedRows > 0) {
			// TODO PUT SOME POPUP WITH INFO
		}
	}



	// tested
	public Post getPostById(long post_id) throws SQLException, PostException, UserException {
		Post post = null;
		PreparedStatement ps = this.getConnection().prepareStatement(
				"select user_id, description, likes_count, dislikes_count, date_time from posts where post_id = ? ;");
		ps.setLong(1, post_id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			post = new Post(post_id, rs.getString("description"), rs.getInt("likes_count"), rs.getInt("dislikes_count"),
					rs.getTimestamp("date_time"));
				post.setUser(userDao.getUserById(rs.getLong("user_id")));
		}
		return post;
	}

	public void addComment(Post postById, Comment c) throws SQLException {
		postById.addComment(c);
	}

	public void deleteComment(Post postById, Comment c) throws SQLException {
		postById.deleteComment(c);
	}

	public TreeSet<Post> getFilteredPosts(String searchFormText, String categoriesIds)
			throws PostException, SQLException, UserException, LocationException, CategoryException, CommentException {
		TreeSet<Post> filteredPosts = new TreeSet<Post>();
		PreparedStatement ps = null;
		if (searchFormText == null || searchFormText.isEmpty()) {
			if (categoriesIds != null && !categoriesIds.isEmpty()) {
				ps = this.getConnection().prepareStatement(
						"select distinct posts.post_id, posts.user_id, posts.description, posts.likes_count, posts.dislikes_count, posts.date_time from posts join posts_categories on(posts.post_id = posts_categories.post_id) where posts_categories.category_id in(?);");
				ps.setString(1, categoriesIds);
			}
		} else {
			if (categoriesIds != null && !categoriesIds.isEmpty()) {
				ps = this.getConnection().prepareStatement(
						"select distinct posts.post_id, posts.user_id, posts.description, posts.likes_count, posts.dislikes_count, posts.date_time from posts  join posts_tags  on(posts.post_id = posts_tags.post_id) join tags on(posts_tags.tag_id = tags.tag_id)join posts_categories on(posts.post_id = posts_categories.post_id) where posts.description like ?  or tags.tag_name like ? or posts_categories.category_id in(?);");
				ps.setString(1, "%" + searchFormText + "%");
				ps.setString(2, "%" + searchFormText + "%");
				ps.setString(3, categoriesIds);
			} else {
				ps = this.getConnection().prepareStatement(
						"select distinct posts.post_id, posts.user_id, posts.description, posts.likes_count, posts.dislikes_count, posts.date_time from posts  join posts_tags  on(posts.post_id = posts_tags.post_id) join tags on(posts_tags.tag_id = tags.tag_id) where posts.description like ? or tags.tag_name like ?;");
				ps.setString(1, "%" + searchFormText + "%");
				ps.setString(2, "%" + searchFormText + "%");
			}
		}
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Post post = new Post(rs.getLong("posts.post_id"), rs.getString("posts.description"),
					rs.getInt("posts.likes_count"), rs.getInt("posts.dislikes_count"),
					rs.getTimestamp("posts.date_time"));
			post.setUser(userDao.getUserById(rs.getLong("posts.user_id")));
			post.setMultimedia(multimediaDao.getAllMultimediaForPost(post));
			post.setVideo(multimediaDao.getVideoForPost(post));
			post.setLocation(locationDao.getLocationByPost(post));
			post.setCategories(categoryDao.getCategoriesForPost(post));
			post.setTags(tagDao.getTagsForPost(post));
			post.setTaggedPeople(userDao.getAllTaggedUsersForPost(post));
			post.setComments(commentDao.getCommentsForPost(post));
			filteredPosts.add(post);
		}
		return filteredPosts;
	}

	public boolean existsReaction(long postId, long userId) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT COUNT(*) FROM posts_reactions WHERE post_id = ? AND user_id=? ");
			ps.setLong(1,postId);
			ps.setLong(2,userId);
			ResultSet rs=ps.executeQuery();
			rs.next();
			if(rs.getInt("COUNT(*)")>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateReaction(boolean reaction, long postId, long userId) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("UPDATE posts_reactions SET reaction=? WHERE post_id=? AND user_id=? ");
			ps.setBoolean(1,reaction);
			ps.setLong(2,postId);
			ps.setLong(3,userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertReaction(boolean b, long id, long user) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("INSERT INTO posts_reactions(post_id, reaction, user_id) VALUE (?,?,?)");
			ps.setLong(1,id);
			ps.setBoolean(2,b);
			ps.setLong(3,user);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteReaction(long id, long user) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("DELETE FROM posts_reactions WHERE post_id=? AND user_id=?");
			ps.setLong(1,id);
			ps.setLong(2,user);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}