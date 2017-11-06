package com.example.model.DBManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.TreeSet;

import com.example.model.*;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CommentDao extends AbstractDao { // used to operate with table 'comments' from db
	@Autowired
	MultimediaDao multimediaDao;
	@Autowired
	UserDao userDao;
	@Autowired
	PostDao postDao;

	// ::::::::: insert/remove from db :::::::::
	public void insertComment(Comment c, User sentBy)throws PostException, UserException {
		try (PreparedStatement ps = this.getConnection().prepareStatement(
				"insert into comments (content, post_id, user_id, date_time) values (?, ?, ?, now());",
				Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, c.getContent());
			ps.setLong(2, c.getPostId());
			ps.setLong(3, sentBy.getUserId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			c.setId(rs.getLong(1));	
			// !!! insert in post POJO comments collection required:
			postDao.addComment(postDao.getPostById(c.getPostId()), c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteComment(Comment c) throws SQLException, PostException, UserException {
		try (PreparedStatement ps = this.getConnection().prepareStatement(
				"delete from comments where id = ? and content = ? and post_id = ? and user_id = ? and date_time = ?;",
				Statement.RETURN_GENERATED_KEYS);) {
			ps.setLong(1, c.getId());
			ps.setString(2, c.getContent());
			ps.setLong(3, c.getPostId());
			ps.setLong(4, c.getUserId());
			ps.setTimestamp(5, c.getDatetime());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			c.setId(rs.getLong(1));
			// !!! delete from post POJO comments collection required:
			postDao.deleteComment(postDao.getPostById(c.getPostId()), c);
		}
	}

	public Comment getCommentById(long id) throws CommentException, SQLException, UserException, PostException {
		Comment c = null;
		try (PreparedStatement ps = this.getConnection().prepareStatement(
				"select comment_id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where comment_id = ?;");) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User sentBy =  userDao.getUserById(rs.getLong("user_id"));
				sentBy.setProfilePic(multimediaDao.getMultimediaById(sentBy.getProfilePic().getId()));
				c = new Comment(rs.getLong("comment_id"), rs.getString("content"), rs.getInt("likes_counter"),
						rs.getInt("dislikes_counter"), rs.getLong("post_id"), rs.getLong("user_id"),
						rs.getTimestamp("date_time"));
				c.setPeopleLiked(this.getAllPeopleLiked(c));
				c.setPeopleDisliked(this.getAllPeopleDisliked(c));
			}
			return c;
		}
	}
	
	// ::::::::: loading comments for post :::::::::
	public TreeSet<Comment> getCommentsForPost(Post p) throws SQLException, CommentException, UserException, PostException {
		TreeSet<Comment> comments = new TreeSet<Comment>();
		try (PreparedStatement ps = this.getConnection().prepareStatement(
				"select comment_id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where post_id = ?;");) {
			ps.setLong(1, p.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User sentBy =  userDao.getUserById(rs.getLong("user_id"));
				sentBy.setProfilePic(multimediaDao.getMultimediaById(sentBy.getProfilePic().getId()));
				Comment comment = new Comment(rs.getLong("comment_id"), rs.getString("content"), rs.getInt("likes_counter"),
						rs.getInt("dislikes_counter"), p.getId(), rs.getLong("user_id"),
						rs.getTimestamp("date_time"),sentBy);
				  comment.setPeopleDisliked(this.getAllPeopleDisliked(comment));
		            comment.setPeopleLiked(getAllPeopleLiked(comment));
					comments.add(comment);
			}
			return comments;
		}
	}

	public HashSet<Long> getAllPeopleLiked(Comment comment) {
		HashSet<Long> peopleLiked=new HashSet<>();
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT user_id FROM comments_reactions WHERE comment_id=? AND reaction=1");
			ps.setLong(1,comment.getId());
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

	public HashSet<Long> getAllPeopleDisliked(Comment comment) {
		HashSet<Long> peopleDisliked=new HashSet<>();
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT user_id FROM comments_reactions WHERE comment_id=? AND reaction=0");
			ps.setLong(1,comment.getId());
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

	
	// ::::::::: like/dislike operations :::::::::
	// currently comments are not keeping data for the users who liked/disliked them
	public void incrementLikes(Comment c) throws SQLException, CommentException {
		try (PreparedStatement ps = this.getConnection()
				.prepareStatement("update comments set likes_counter = ? where id = ?;")) {
			ps.setInt(1, c.getLikesCount());
			ps.setLong(2, c.getId());
			c.incrementLikes();
		}
	}

	public void incrementDislikes(Comment c) throws SQLException, CommentException {
		try (PreparedStatement ps = this.getConnection()
				.prepareStatement("update comments set dislikes_counter = ? where id = ?;");) {
			ps.setInt(1, c.getDislikesCount());
			ps.setLong(2, c.getId());
			c.incrementDislikes();
		}
	}

	public boolean existsReaction(long commentId, long userId) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT COUNT(*) FROM comments_reactions WHERE comment_id = ? AND user_id=? ");
			ps.setLong(1,commentId);
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
	
	public void updateReaction(boolean reaction, long commentId, long userId) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("UPDATE comments_reactions SET reaction=? WHERE comment_id=? AND user_id=? ");
			ps.setBoolean(1,reaction);
			ps.setLong(2,commentId);
			ps.setLong(3,userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertReaction(boolean b, long commentId, long userId) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("INSERT INTO comments_reactions(comment_id, reaction, user_id) VALUE (?,?,?)");
			ps.setLong(1,commentId);
			ps.setBoolean(2,b);
			ps.setLong(3,userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteReaction(long commentId, long userId) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("DELETE FROM comments_reactions WHERE comment_id=? AND user_id=?");
			ps.setLong(1,commentId);
			ps.setLong(2,userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}