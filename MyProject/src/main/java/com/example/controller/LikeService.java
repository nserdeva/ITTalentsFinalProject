package com.example.controller;

import com.example.model.DBManagement.CommentDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.Comment;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.exceptions.CommentException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Marina on 2.11.2017 г..
 */
@RestController
@Controller
public class LikeService {
	@Autowired
	PostDao postDao;
	@Autowired
	CommentDao commentDao;

	@RequestMapping(value = "/like/{postId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] likePost(HttpSession session, Model model, HttpServletResponse resp,
			@PathVariable("postId") long postId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Post post = null;
		try {
			post = postDao.getPostById(postId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			if (postDao.existsReaction(postId, userId)) {
				postDao.updateReaction(true, post.getId(), ((User) session.getAttribute("user")).getUserId());
				post.addPersonLiked(userId);
				post.removePersonDisliked(userId);
				resp.setStatus(200);
			} else {
				postDao.insertReaction(true, post.getId(), ((User) session.getAttribute("user")).getUserId());
				post.addPersonLiked(userId);
				resp.setStatus(201);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = post.getPeopleLiked().size();
		sizes[1] = post.getPeopleDisliked().size();
		return sizes;
	}

	@RequestMapping(value = "/unlike/{postId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] unlikePost(HttpSession session, HttpServletResponse resp, @PathVariable("postId") long postId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Post post = null;
		try {
			post = postDao.getPostById(postId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			postDao.deleteReaction(post.getId(), ((User) session.getAttribute("user")).getUserId());
			post.removePersonLiked(userId);
			// true=like false=dislike
			resp.setStatus(200);
		} catch (SQLException | PostException e) {
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = post.getPeopleLiked().size();
		sizes[1] = post.getPeopleDisliked().size();
		return sizes;
	}

	@RequestMapping(value = "/dislike/{postId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] dislikePost(HttpSession session, HttpServletResponse resp, @PathVariable("postId") long postId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Post post = null;
		try {
			post = postDao.getPostById(postId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			if (postDao.existsReaction(postId, userId)) {
				post.removePersonLiked(userId);
				post.addPersonDisliked(userId);
				postDao.updateReaction(false, post.getId(), ((User) session.getAttribute("user")).getUserId());
				resp.setStatus(200);
			} else {
				postDao.insertReaction(false, post.getId(), ((User) session.getAttribute("user")).getUserId());
				post.addPersonDisliked(userId);
				resp.setStatus(201);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = post.getPeopleLiked().size();
		sizes[1] = post.getPeopleDisliked().size();
		return sizes;
	}

	@RequestMapping(value = "/undislike/{postId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] undislikePost(HttpSession session, HttpServletResponse resp, @PathVariable("postId") long postId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Post post = null;
		try {
			post = postDao.getPostById(postId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			postDao.deleteReaction(post.getId(), ((User) session.getAttribute("user")).getUserId());
			post.removePersonDisliked(userId);
			// true=like false=dislike
			resp.setStatus(200);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = post.getPeopleLiked().size();
		sizes[1] = post.getPeopleDisliked().size();
		return sizes;
	}

	// :::::Comment like/dislike functionality methods:::::

	@RequestMapping(value = "/likeComment/{commentId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] likeComment(HttpSession session, Model model, HttpServletResponse resp,
			@PathVariable("commentId") long commentId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Comment comment = null;
		try {
			comment = commentDao.getCommentById(commentId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			if (commentDao.existsReaction(commentId, userId)) {
				commentDao.updateReaction(true, comment.getId(), ((User) session.getAttribute("user")).getUserId());
				comment.addPersonLiked(userId);
				comment.removePersonDisliked(userId);
				resp.setStatus(200);
			} else {
				commentDao.insertReaction(true, comment.getId(), ((User) session.getAttribute("user")).getUserId());
				comment.addPersonLiked(userId);
				resp.setStatus(201);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (CommentException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = comment.getPeopleLiked().size();
		sizes[1] = comment.getPeopleDisliked().size();
		return sizes;
	}

	@RequestMapping(value = "/unlikeComment/{commentId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] unlikeComment(HttpSession session, HttpServletResponse resp,
			@PathVariable("commentId") long commentId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Comment comment = null;
		try {
			comment = commentDao.getCommentById(commentId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			commentDao.deleteReaction(comment.getId(), ((User) session.getAttribute("user")).getUserId());
			comment.removePersonLiked(userId);
			// true=like false=dislike
			resp.setStatus(200);
		} catch (SQLException | PostException e) {
			e.printStackTrace();
		} catch (CommentException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = comment.getPeopleLiked().size();
		sizes[1] = comment.getPeopleDisliked().size();
		return sizes;
	}

	@RequestMapping(value = "/dislikeComment/{commentId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] dislikeComment(HttpSession session, HttpServletResponse resp,
			@PathVariable("commentId") long commentId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Comment comment = null;
		try {
			comment = commentDao.getCommentById(commentId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			if (commentDao.existsReaction(commentId, userId)) {
				comment.removePersonLiked(userId);
				comment.addPersonDisliked(userId);
				commentDao.updateReaction(false, comment.getId(), ((User) session.getAttribute("user")).getUserId());
				resp.setStatus(200);
			} else {
				commentDao.insertReaction(false, comment.getId(), ((User) session.getAttribute("user")).getUserId());
				comment.addPersonDisliked(userId);
				resp.setStatus(201);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (CommentException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = comment.getPeopleLiked().size();
		sizes[1] = comment.getPeopleDisliked().size();
		return sizes;
	}

	@RequestMapping(value = "/undislikeComment/{commentId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] undislikeComment(HttpSession session, HttpServletResponse resp,
			@PathVariable("commentId") long commentId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
		Comment comment = null;
		try {
			comment = commentDao.getCommentById(commentId);
			long userId = ((User) session.getAttribute("user")).getUserId();
			commentDao.deleteReaction(comment.getId(), ((User) session.getAttribute("user")).getUserId());
			comment.removePersonDisliked(userId);
			// true=like false=dislike
			resp.setStatus(200);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (CommentException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = comment.getPeopleLiked().size();
		sizes[1] = comment.getPeopleDisliked().size();
		return sizes;
	}

}