package com.example.model.DBManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	public void insertComment(Comment c, User u) throws SQLException, PostException {
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"insert into comments (content, post_id, user_id, date_time) values (?, ?, ?, now());",
				Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, c.getContent());
			ps.setLong(2, c.getPostId());
			ps.setLong(3, u.getUserId());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			c.setId(rs.getLong(1));
			// !!! insert in post POJO comments collection required:
			postDao.addComment(postDao.getPostById(c.getPostId()), c);
		}
	}

	public void deleteComment(Comment c) throws SQLException, PostException {
		try (PreparedStatement ps = this.getCon().prepareStatement(
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

	// ::::::::: loading comments for post :::::::::
	public TreeSet<Comment> getCommentsForPost(Post p) throws SQLException, CommentException, UserException, PostException {
		TreeSet<Comment> comments = new TreeSet<Comment>();
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select comment_id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where post_id = ?;");) {
			ps.setLong(1, p.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User sentBy =  userDao.getUserById(rs.getLong("user_id"));
				sentBy.setProfilePic(multimediaDao.getMultimediaById(sentBy.getProfilePicId()));
				comments.add(new Comment(rs.getLong("comment_id"), rs.getString("content"), rs.getInt("likes_counter"),
						rs.getInt("dislikes_counter"), p.getId(), rs.getLong("user_id"),
						rs.getTimestamp("date_time"),sentBy));
			}
			return comments;
		}
	}

	// ::::::::: like/dislike operations :::::::::
	// currently comments are not keeping data for the users who liked/disliked them
	public void incrementLikes(Comment c) throws SQLException, CommentException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("update comments set likes_counter = ? where id = ?;");) {
			ps.setInt(1, c.getLikesCount());
			ps.setLong(2, c.getId());
			c.incrementLikes();
		}
	}

	public void incrementDislikes(Comment c) throws SQLException, CommentException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("update comments set dislikes_counter = ? where id = ?;");) {
			ps.setInt(1, c.getDislikesCount());
			ps.setLong(2, c.getId());
			c.incrementDislikes();
		}
	}

}