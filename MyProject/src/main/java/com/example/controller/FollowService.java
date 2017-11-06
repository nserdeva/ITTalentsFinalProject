package com.example.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

@RestController
@Controller
public class FollowService {
	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/follow/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public Integer[] followUser(HttpSession session, HttpServletResponse resp, @PathVariable("userId") long userId)
			throws IOException {
		if (session.getAttribute("user") == null || session.getAttribute("logged").equals(false)) {
			resp.sendRedirect("login");
		}
		User follower = (User) session.getAttribute("user");
		User followed = null;
		try {
			followed = userDao.getUserById(userId);
			userDao.follow(follower, followed);
			userDao.setFollowers(followed);
			userDao.setFollowing(followed);
			resp.setStatus(200);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = followed.getFollowers().size();
		sizes[1] = followed.getFollowing().size();
		return sizes;
	}

	@RequestMapping(value = "/unfollow/{userId}", method = RequestMethod.POST)
	public Integer[] unfollowUser(HttpSession session, HttpServletResponse resp, @PathVariable("userId") long userId)
			throws IOException {
		if (session.getAttribute("user") == null || session.getAttribute("logged").equals(false)) {
			resp.sendRedirect("login");
		}
		User follower = (User) session.getAttribute("user");
		User followed = null;
		try {
			followed = userDao.getUserById(userId);
			userDao.setFollowers(followed);
			userDao.setFollowing(followed);
			if (follower != null & followed != null) {
				userDao.unfollow(follower, followed);
				resp.setStatus(200);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
		Integer[] sizes = new Integer[2];
		sizes[0] = followed.getFollowers().size();
		sizes[1] = followed.getFollowing().size();
		return sizes;
	}

}