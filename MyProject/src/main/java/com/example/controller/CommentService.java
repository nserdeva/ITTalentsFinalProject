package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Comment;
import com.example.model.User;
import com.example.model.DBManagement.CommentDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.CommentException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

@Controller
@RestController
public class CommentService {
	@Autowired
	UserDao userDao;
	@Autowired
	CommentDao commentDao;
	@Autowired
	PostDao postDao;

	@RequestMapping(value = "/postComment/{postId}/{content}", method = RequestMethod.POST)
	@ResponseBody
	public Comment postComment(HttpSession session, HttpServletResponse resp, HttpServletRequest request,
			@PathVariable("postId") long postId, @PathVariable("content") String content) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		User sentBy = (User) session.getAttribute("user");
		try {
			Comment comment = new Comment(content, postId, sentBy.getUserId(), sentBy);
			commentDao.insertComment(comment, sentBy);
			resp.setStatus(200);
			return commentDao.getCommentById(comment.getId());
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CommentException e) {
			e.printStackTrace();
		}
		return null;
	}

}