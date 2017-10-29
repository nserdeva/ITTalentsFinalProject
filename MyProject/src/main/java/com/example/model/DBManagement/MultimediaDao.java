package com.example.model.DBManagement;

import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 ??..
 */
@Component
public class MultimediaDao extends AbstractDao {
    @Autowired
    PostDao postDao;

    public static Multimedia AVATAR=new Multimedia(0,"avatar.jpg",false, null);

    //tested
    public void insertMultimediaInPost(Post post, Multimedia multimedia) throws SQLException, MultimediaException {
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(
                    "insert into multimedia(file_url,is_video, post_id) value (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, multimedia.getUrl());
            ps.setBoolean(2,multimedia.isVideo());
            ps.setLong(3,post.getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            multimedia.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new MultimediaException("Multimedia could not be inserted. Reason: "+e.getMessage());
        }
    }


    //TODO not working
    public void deleteMultimedia(Multimedia multimedia) throws SQLException, PostException {
        try {
            this.getConnection().setAutoCommit(false);
            PreparedStatement ps = this.getConnection().prepareStatement(
                    "delete from multimedia where multimedia_id= ? ;");
            ps.setLong(1, multimedia.getId());
            ps.executeUpdate();
            this.deleteMultimediaFromPost(multimedia);
            this.getConnection().commit();
        } catch (SQLException e) {
            throw new PostException("Error deleting multimedia. Reason: "+e.getMessage());
        }finally {
            this.getConnection().rollback();
            this.getConnection().setAutoCommit(true);
        }
    }

    public void deleteMultimediaFromPost(Multimedia multimedia) throws SQLException, PostException {
        postDao.getPostById(multimedia.getPost().getId()).deleteMultimedia(multimedia);
    }

    public HashSet<Multimedia> getAllMultimediaForPost(Post post) throws SQLException {
        PreparedStatement ps = this.getConnection().prepareStatement(
                "select multimedia_id,file_url, is_video from multimedia where post_id= ?  ;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Multimedia> multimedia=new HashSet<>();
        while (rs.next()){
            multimedia.add(new Multimedia(rs.getLong("multimedia_id"), rs.getString("file_url"), rs.getBoolean("is_video"),post));
        }
        return multimedia;
    }

    /*public Multimedia getMultimediaById(long multimediaId) throws MultimediaException, SQLException, UserException, PostException {
        PreparedStatement ps = this.getConnection().prepareStatement(
                "select file_dir, is_video, post_id from multimedia where multimedia_id= ?   ;");
        ps.setLong(1,multimediaId );
        ResultSet rs=ps.executeQuery();
        rs.next();
        Multimedia multimedia=new Multimedia(multimediaId,
                rs.getString("file_dir"), rs.getBoolean("is_video"),
                PostDao.getInstance().getPostById(rs.getLong("post_id")));
        return multimedia;
    }*/

    public void addAllMultimediaToPost(Post post, HashSet<Multimedia> multimedia) throws SQLException, MultimediaException {
        PreparedStatement ps = null;
        try {
            for (Multimedia m : multimedia) {
                this.getConnection().setAutoCommit(false);
                ps = this.getConnection().prepareStatement(
                        "insert into multimedia(file_dir,is_video, post_id) values (?,?,?);",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,m.getUrl());
                ps.setBoolean(2,m.isVideo());
                ps.setLong(3,post.getId());
                ps.executeUpdate();
                ResultSet resultSet=ps.getGeneratedKeys();
                m.setId(resultSet.getLong(1));
                this.getConnection().commit();
            }
        } catch (SQLException e) {
            throw new MultimediaException("Multimedia could not be inserted. Reason: "+e.getMessage());
        }finally {
            this.getConnection().rollback();
            this.getConnection().setAutoCommit(true);
        }
    }
    
    public Multimedia getMultimediaById(long id) throws SQLException, PostException {
		Multimedia fetched = null;
		try (PreparedStatement ps = this.getConnection().prepareStatement(
				"select file_url, is_video, post_id from multimedia where multimedia_id = ?;");) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fetched = new Multimedia(id, rs.getString("file_url"),
                        rs.getBoolean("is_video"),
                        postDao.getPostById(rs.getLong("post_id")));
			}
			return fetched;
		}
	}

    public void insertMultimedia(User user, Multimedia newAvatar) throws SQLException, MultimediaException {
        try{
            PreparedStatement ps=this.getConnection().prepareStatement(
                    "INSERT INTO multimedia(file_url, is_video) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,newAvatar.getUrl());
            ps.setBoolean(2,newAvatar.isVideo());
            ps.executeUpdate();
            ResultSet resultSet=ps.getGeneratedKeys();
            resultSet.next();
            newAvatar.setId(resultSet.getLong(1));
            user.setProfilePic(newAvatar);
        }catch (SQLException e ){
            throw new MultimediaException("Avatar could not be inserted in database.Reason: "+e.getMessage());
        }
    }

    public void changeAvatar(User user, Multimedia newAvatar) throws SQLException, MultimediaException {
        if(user.getProfilePic().getId()==0){
                insertMultimedia(user,newAvatar);
        }else{
            try{
                PreparedStatement ps=this.getConnection().prepareStatement(
                        "UPDATE  multimedia SET  file_url=? WHERE multimedia_id=?");
                ps.setString(1,newAvatar.getUrl());
                ps.setLong(2,user.getProfilePic().getId());
                ps.executeUpdate();
                newAvatar.setId(user.getProfilePic().getId());
            }catch (SQLException e ){
                throw new MultimediaException("Avatar could not be inserted in database.Reason: "+e.getMessage());
            }
        }

    }
}