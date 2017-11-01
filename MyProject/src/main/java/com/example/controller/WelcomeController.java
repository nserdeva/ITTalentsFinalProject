package com.example.controller;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.Post;
import com.example.model.User;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.CategoryException;
import com.example.model.exceptions.CommentException;
import com.example.model.exceptions.LocationException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

/**
 * Created by Marina on 27.10.2017 г..
 */
@Controller
public class WelcomeController {
	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/wanderlust", method = RequestMethod.GET)
	public String getWelcomePage() {
		return "index";
	}

	@RequestMapping(value = "/myPassport", method = RequestMethod.GET)
	public String getMyPassport(HttpSession session) {
		long userId = ((User) session.getAttribute("user")).getUserId();
		return "redirect:/showPassport/"+userId;
	}

	@RequestMapping(value = "/newsfeed", method = RequestMethod.GET)
	public String showNewsfeed(HttpSession session)
			throws SQLException, LocationException, CategoryException, UserException, PostException, CommentException {
		TreeSet<Post> newsfeedPosts = new TreeSet<Post>();
		User currentUser = (User) session.getAttribute("user");
		userDao.setFollowing(currentUser);
		for (User followed : currentUser.getFollowing()) {
			System.out.println(" :::::::::: Following: " + followed.getUsername());
			userDao.setFollowers(followed);
			userDao.setFollowing(followed);
			userDao.setProfilePic(followed);
			userDao.setPosts(followed);
			// userDao.setVisitedLocations(followed);
			// userDao.setWishlistLocations(followed);
			newsfeedPosts.addAll(userDao.getPosts(followed));

		}
		session.setAttribute("newsfeedPosts", newsfeedPosts);
		return "newsfeed";
	}

}