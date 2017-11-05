package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.WebInitializer;
import com.example.model.Location;
import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.DBManagement.CategoryDao;
import com.example.model.DBManagement.CommentDao;
import com.example.model.DBManagement.LocationDao;
import com.example.model.DBManagement.MultimediaDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.DBManagement.TagDao;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.CategoryException;
import com.example.model.exceptions.CommentException;
import com.example.model.exceptions.LocationException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;

@Controller
public class ExploreController {
	@Autowired
	UserDao userDao;
	@Autowired
	LocationDao locationDao;
	@Autowired
	MultimediaDao multimediaDao;
	@Autowired
	PostDao postDao;
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	TagDao tagDao;
	@Autowired
	CommentDao commentDao;

	@RequestMapping(value = "/searchAdventurers", method = RequestMethod.POST)
	public String searchAdventurers(HttpSession session, HttpServletRequest request)
			throws SQLException, LocationException, CategoryException {
		try {
			session.setAttribute("browsedAdventurers",
					userDao.getFilteredUsers(request.getParameter("searchFormDataTxt")));
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "exploreAdventurers";
	}

	@RequestMapping(value = "/showMostPopular", method = RequestMethod.POST)
	public String showMostPopularFirst(HttpSession session, HttpServletRequest request)
			throws SQLException, LocationException, CategoryException, PostException, UserException, CommentException {
		TreeSet<Post> newsfeedPosts = new TreeSet<Post>((p1, p2)-> (p2.getLikesCount()-p1.getLikesCount())!=0 ? (p2.getLikesCount()-p1.getLikesCount()) : (p2.getDateTime().compareTo(p1.getDateTime()))  );
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
	
	@RequestMapping(value = "/searchDestinations", method = RequestMethod.POST)
	public String searchDestinations(HttpSession session, HttpServletRequest request)
			throws SQLException, LocationException, CategoryException {
		session.setAttribute("browsedLocations",
				locationDao.getFilteredLocations(request.getParameter("searchFormDataTxt")));
		return "exploreDestinations";
	}

	

	@RequestMapping(value = "/searchAdventures", method = RequestMethod.POST)
	public String searchAdventures(HttpSession session, HttpServletRequest request)
			throws SQLException, LocationException, CategoryException, PostException, UserException, CommentException {
		ArrayList<String> checkBoxValues = new ArrayList<String>();
		checkBoxValues.add(request.getParameter("natureCheckBox")); // category_id must be '1' in db
		checkBoxValues.add(request.getParameter("seaCheckBox")); // category_id must be '2' in db
		checkBoxValues.add(request.getParameter("mountainsCheckBox")); // category_id must be '3' in db
		checkBoxValues.add(request.getParameter("dessertCheckBox")); // category_id must be '4' in db
		checkBoxValues.add(request.getParameter("landmarkCheckBox")); // category_id must be '5' in db
		checkBoxValues.add(request.getParameter("resortCheckBox")); // category_id must be '6' in db
		checkBoxValues.add(request.getParameter("cityCheckBox")); // category_id must be '7' in db
		session.setAttribute("browsedAdventures", postDao.getFilteredPosts(request.getParameter("searchFormDataTxt"),
				this.getCategoriesIds(checkBoxValues)));
		return "exploreAdventures";
	}

	private String getCategoriesIds(ArrayList<String> checkBoxValues) {
		checkBoxValues.trimToSize();
		StringBuilder sb = new StringBuilder();
		boolean firstFound = false;
		for (int i = 0; i < checkBoxValues.size(); i++) {
			if (checkBoxValues.get(i) != null && checkBoxValues.get(i).equals("true")) {
				if (firstFound) {
					sb.append(",");
				}
				sb.append((i + 1));
				firstFound = true;
			}
		}
		return sb.toString();
	}

	@RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
	public String getLocationPage(@PathVariable("id") long id, HttpSession session) throws CategoryException {
		try {
			Location selectedLocation = locationDao.getLocationById((id));
			session.setAttribute("location", selectedLocation);
		} catch (NumberFormatException | SQLException | LocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "location";
	}

	@RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
	public String getPostPage(@PathVariable("id") long id, HttpSession session)
			throws CategoryException, UserException {
		try {
			Post selectedPost = postDao.getPostById(id);
			selectedPost.setCategories(categoryDao.getCategoriesForPost(selectedPost));
			selectedPost.setTags(tagDao.getTagsForPost(selectedPost));
			selectedPost.setMultimedia(multimediaDao.getAllMultimediaForPost(selectedPost));
			selectedPost.setVideo(multimediaDao.getVideoForPost(selectedPost));
			selectedPost.setLocation(locationDao.getLocationByPost(selectedPost));
			selectedPost.setTaggedPeople(userDao.getAllTaggedUsersForPost(selectedPost));
			selectedPost.setComments(commentDao.getCommentsForPost(selectedPost));
			selectedPost.setPeopleDisliked(postDao.getAllPeopleDisliked(selectedPost));
			selectedPost.setPeopleLiked(postDao.getAllPeopleLiked(selectedPost));
			session.setAttribute("post", selectedPost);
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "post";
	}

	@RequestMapping(value = "/showPassport/{id}", method = RequestMethod.GET)
	public String getPassportPage(@PathVariable("id") long id, HttpSession session, HttpServletRequest request,Model model)
			throws CategoryException, UserException {
		try {
			User current = (User)session.getAttribute("user");
			User selectedUser = userDao.getUserById(id);
			userDao.setFollowers(selectedUser);
			userDao.setFollowing(selectedUser);
			userDao.setPosts(selectedUser);
			userDao.setProfilePic(selectedUser);
			// userDao.setVisitedLocations(selectedUser);
			// userDao.setWishlistLocations(selectedUser);
			session.setAttribute("selectedUser", selectedUser);
            request.setAttribute("thisFollowsSelected", current.follows(selectedUser));
            request.setAttribute("isMyPassport", current.equals(selectedUser));
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "passport";
	}

	@RequestMapping(value = "/location/getMainPic/{id}", method = RequestMethod.GET)
	public void getLocationMainPic(@PathVariable("id") long id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException {
		Location selectedLocation = locationDao.getLocationById(id);
		locationDao.setPictures(selectedLocation);
		String locationMainPicUrl = selectedLocation.getMainPic().getUrl();
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
					+ WebInitializer.LOCATIONS_PICTURES_LOCATION + File.separator + locationMainPicUrl);
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/post/getMainPic/{id}", method = RequestMethod.GET)
	public void getPostMainPic(@PathVariable("id") long id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException, PostException, UserException {
		Post selectedPost = postDao.getPostById(id);
		selectedPost.setMultimedia(multimediaDao.getAllMultimediaForPost(selectedPost));
		if (selectedPost.getMainPic() != null) {
			String postMainPicUrl = selectedPost.getMainPic().getUrl();
			try {
				File myFile = new File(
						WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION + File.separator + postMainPicUrl);
				OutputStream out = response.getOutputStream();
				Path path = myFile.toPath();
				Files.copy(path, out);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO SERIOUS NEED OF REFACTORING - METHODS WITH SAME FUNCTIONALITY
	// !!!!!!!!!!!!!!!!!!!!!!!!
	@RequestMapping(value = "/location/picture/{id}", method = RequestMethod.GET)
	public void getLocationPicture(@PathVariable("id") long id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException, UserException {
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
					+ WebInitializer.LOCATIONS_PICTURES_LOCATION + File.separator
					+ multimediaDao.getMultimediaById(id).getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PostException e) { // e stiga we ?????
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/post/multimedia/{id}", method = RequestMethod.GET)
	public void getPostMultimediaFile(@PathVariable("id") long id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException, UserException {
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION + File.separator
					+ multimediaDao.getMultimediaById(id).getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PostException e) { // e stiga we ?????
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

		}
	}

	@RequestMapping(value = "/user/picture/{id}", method = RequestMethod.GET)
	public void getUserPicture(@PathVariable("id") long id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException {
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.AVATAR_LOCATION + File.separator
					+ userDao.getUserById(id).getProfilePic().getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PostException e) { // e stiga we ?????
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}