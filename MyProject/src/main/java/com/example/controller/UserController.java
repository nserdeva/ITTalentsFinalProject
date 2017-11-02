package com.example.controller;

import com.amdelamar.jhash.Hash;
import com.amdelamar.jhash.algorithms.Type;
import com.amdelamar.jhash.exception.BadOperationException;
import com.amdelamar.jhash.exception.InvalidHashException;
import com.example.WebInitializer;
import com.example.model.Category;
import com.example.model.DBManagement.*;
import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.Tag;
import com.example.model.User;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 26.10.2017 ?..
 */
@Controller
public class UserController {
	@Autowired
	UserDao userDao;
	@Autowired
	MultimediaDao multimediaDao;
	@Autowired
	ServletContext servletContext;
	@Autowired
	TagDao tagDao;
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	LocationDao locationDao;

	@RequestMapping(value = "*", method = RequestMethod.GET)
	public String login(Model model) {
		// TODO CHECK IF LOGGED IN
		// model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value = "*", method = RequestMethod.POST)
	public String logUser(HttpSession session, HttpServletRequest request)
			throws NoSuchAlgorithmException, BadOperationException, InvalidHashException {
		String username = request.getParameter("user");
		String password = request.getParameter("pass");
		// TODO HASHING
		try {

			// WARNING
			User user = userDao.getUserByUsername(username);
			if (user != null) {
				if (Hash.verify(password, user.getPassword())) {
					// userDao.setProfilePic(user);
					session.setAttribute("user", user);

					session.setAttribute("logged", true);
					request.setAttribute("isValidData", true);
					HashSet<String> usernames = userDao.getAllUsernames();
					for (String name : usernames) {
						System.out.println(name);
					}
					HashSet<String> tags = tagDao.getAllTags();
					for (String tag : tags) {
						System.out.println(tag);
					}
					HashMap<String, Category> categories = categoryDao.getAllCategories();
					for (Category category : categories.values()) {
						System.out.println(category.getId());
						System.out.println(category.getName());
					}
					Set<String> categoryNames = categories.keySet();
					HashSet<String> locationNames = locationDao.getAllLocationNames();
					servletContext.setAttribute("locations", locationNames);
					servletContext.setAttribute("usernames", usernames);
					servletContext.setAttribute("tags", tags);
					servletContext.setAttribute("categories", categories);
					servletContext.setAttribute("categoryNames", categoryNames);

					return "index";
				}
			} else {
				request.setAttribute("isValidData", false);
				return "login";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (CommentException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (LocationException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		} catch (CategoryException e) {
			e.printStackTrace();
		}
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegister() {
		// TODO check if logged
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(HttpServletRequest request, HttpSession session)
			throws NoSuchAlgorithmException, BadOperationException {
		String username = request.getParameter("user");
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");
		String email = request.getParameter("email");
		if (pass != null && pass.equals(pass2)) {
			try {
				if (!userDao.existsUsername(username)) {
					User user = new User(username, Hash.create(pass, Type.BCRYPT), email);
					userDao.insertUser(user);
					session.setAttribute("user", user);
					session.setAttribute("logged", true);
					return "index";
				} else {
					System.out.println("second if- else");
					return "register";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (UserException e) {
				e.printStackTrace();
			}
		}
		return "register";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.setAttribute("logged", false);
		session.invalidate();
		return "login";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String arrangeSettings(HttpSession session) {
		// TODO CHECK IF LOGGED
		return "settings";
	}

	@RequestMapping(value = "/settings/changeDescription", method = RequestMethod.GET)
	public String getChangeDescriptionForm(HttpSession session, HttpServletRequest request) throws SQLException {
		return "settings";
	}

	@RequestMapping(value = "/settings/changeDescription", method = RequestMethod.POST)
	public String changeDescription(HttpSession session, HttpServletRequest request) throws SQLException {
		String newDescription = request.getParameter("descriptionTxt");
		System.out.println(newDescription == null);
		userDao.changeDescription((User) session.getAttribute("user"), newDescription);
		return "settings";
	}

	@RequestMapping(value = "/settings/changeEmail", method = RequestMethod.GET)
	public String getChangeEmailForm(HttpSession session, Model model) throws SQLException {
		model.addAttribute("email", ((User) session.getAttribute("user")).getEmail());
		return "settings";
	}

	@RequestMapping(value = "/settings/changeEmail", method = RequestMethod.POST)
	public String changeEmail(HttpSession session, HttpServletRequest request,
			@Valid @ModelAttribute("email") String email, BindingResult result) throws SQLException {
		// String newEmail = request.getParameter("emailTxt");
		// TODO CHECK FOR MISTAKEN EMAIL
		// TODO AJAX
		if (result.hasErrors()) {
			System.out.println("================IMA LI GRESHKI==========================]");
			return "settings";
		} else {
			// System.out.println(newEmail==null);
			try {
				// TODO GOING HERE BUT GETTING NULL EMAIL
				System.out.println(
						"=================//////////" + email + "///////////////===================================");
				userDao.changeEmail((User) session.getAttribute("user"), email);
			} catch (UserException e) {
				return "settings";
			}
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeAvatar", method = RequestMethod.GET)
	public String getAvatar() {
		return "settings";
	}

	@RequestMapping(value = "/settings/getAvatar", method = RequestMethod.GET)
	public void getChangeAvatar(HttpSession session, HttpServletResponse resp, Model model) {
		User u = (User) session.getAttribute("user");
		String avatarUrl = u.getProfilePic().getUrl();
		try {
			File newAvatar = new File(
					WebInitializer.LOCATION + WebInitializer.AVATAR_LOCATION + File.separator + avatarUrl);
			OutputStream out = resp.getOutputStream();
			Path path = newAvatar.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/settings/changeAvatar", method = RequestMethod.POST)
	public String changeAvatar(HttpSession session, @RequestParam("avatar") MultipartFile file, Model model) {
		User user = (User) session.getAttribute("user");
		String avatarUrl = user.getUsername() + ".jpg";
		try {
			if (file.isEmpty()) {
				// TODO NOT SURE HERE
				return "settings";
			}
			File f = new File(WebInitializer.LOCATION + WebInitializer.AVATAR_LOCATION + File.separator + avatarUrl);
			file.transferTo(f);
			Multimedia newAvatar = new Multimedia(avatarUrl, false);
			multimediaDao.changeAvatar(user, newAvatar); // insert in multimedia table and UPDATE USER HAVE THE NEWLY
															// INSERTED AVATAR
			// insert in user the new avatar
			session.setAttribute("avatar", avatarUrl);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MultimediaException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "settings";
	}

	@RequestMapping(value = "/follow/{userId}", method = RequestMethod.POST)
	public void followUser(HttpSession session, HttpServletResponse resp, @PathVariable("userId") long userId)
			throws UserException {
		try {
			User follower = (User) session.getAttribute("user");
			User followed = userDao.getUserById(userId);
			userDao.follow(follower, followed);
			resp.setStatus(200);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/unfollow/{userId}", method = RequestMethod.POST)
	public void unfollowUser(HttpSession session, HttpServletResponse resp, @PathVariable("userId") long userId)
			throws UserException {
		try {
			User follower = (User) session.getAttribute("user");
			User followed = userDao.getUserById(userId);
			if (follower != null & followed != null) {
				userDao.unfollow(follower, followed);
				resp.setStatus(200);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		}
	}

}