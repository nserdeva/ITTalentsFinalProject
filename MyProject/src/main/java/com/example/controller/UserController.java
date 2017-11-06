package com.example.controller;

import com.amdelamar.jhash.Hash;
import com.amdelamar.jhash.algorithms.Type;
import com.amdelamar.jhash.exception.BadOperationException;
import com.amdelamar.jhash.exception.InvalidHashException;
import com.example.WebInitializer;
import com.example.model.Category;
import com.example.model.DBManagement.*;
import com.example.model.Multimedia;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
		public String getIndex(HttpSession session) {
			return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login2(HttpSession session) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String logUser(HttpSession session, HttpServletRequest request) {
		String username = request.getParameter("user");
		username=username.trim();
		String password = request.getParameter("pass");
		password=password.trim();

		if("".equals(username) || "".equals(password)){
			request.setAttribute("isValidData",false);
			return "login";
		}
		try {
			User user = userDao.getUserByUsername(username);
			if (user != null) {
				if (Hash.verify(password, user.getPassword())) {
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
					return "redirect:/showPassport/" + user.getUserId();
				}
			} else {
				request.setAttribute("isValidData", false);
				return "login";
			}
		} catch (SQLException | CommentException | PostException | LocationException | UserException | CategoryException
				| NoSuchAlgorithmException | BadOperationException | InvalidHashException e) {
			e.printStackTrace();
			return "login";
		}
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegister() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(HttpServletRequest request, HttpSession session) {
		String username = request.getParameter("user");
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");
		String email = request.getParameter("email");
		try {
		User test = new User(username, pass, email); //test if given data is correct
		if (pass != null && pass.equals(pass2)) {
				if (!userDao.existsUsername(username)) {
					User user = new User(username, Hash.create(pass, Type.BCRYPT), email);
					userDao.insertUser(user);
					session.setAttribute("user", user);
					session.setAttribute("logged", true);
					return "redirect:/showPassport/" + user.getUserId();
				} else {
					System.out.println("Vlizash li TUKA weeee");

					return "register";
				}
		} else {
			request.setAttribute("doPasswordsMatch", false);
			return "register";
		}
			} catch (SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
				return "register";
			} catch (UserException | BadOperationException e) {
				System.out.println("Vlizash li weeee");
				if(e.getMessage().contains("Username")) {
					System.out.println("VLIZA PRI USERNAME");
					request.setAttribute("isValidUsername", false);
				} 
				if(e.getMessage().contains("e-mail")) {
					System.out.println("VLIZA PRI EMAIL");
					request.setAttribute("isValidEmail", false);
				}
				if(e.getMessage().contains("Password")) {
					System.out.println("VLIZA PRI PASSWORD");
					request.setAttribute("isValidPassword", false);
				}
				return "register";
			}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		session.setAttribute("logged", false);
		session.invalidate();
		return "login";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String arrangeSettings(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeDescription", method = RequestMethod.GET)
	public String getChangeDescriptionForm(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeDescription", method = RequestMethod.POST)
	public String changeDescription(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		String newDescription = request.getParameter("descriptionTxt");
		try {
			userDao.changeDescription((User) session.getAttribute("user"), newDescription);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeEmail", method = RequestMethod.GET)
	public String getChangeEmailForm(HttpSession session, Model model) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		model.addAttribute("email", ((User) session.getAttribute("user")).getEmail());
		return "settings";
	}

	@RequestMapping(value = "/settings/changeEmail", method = RequestMethod.POST)
	public String changeEmail(HttpSession session, HttpServletRequest request,
			@Valid @ModelAttribute("email") String email, BindingResult result) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		if (result.hasErrors()) {
			return "settings";
		} else {
			try {
				userDao.changeEmail((User) session.getAttribute("user"), email);
			} catch (UserException | SQLException e) {
				request.setAttribute("errorMessage", e.getMessage());
				return "error";
			}
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/changeAvatar", method = RequestMethod.GET)
	public String getAvatar(HttpSession session) {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		return "settings";
	}

	@RequestMapping(value = "/settings/getAvatar", method = RequestMethod.GET)
	public void getChangeAvatar(HttpSession session, HttpServletResponse resp, Model model) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			resp.sendRedirect("login");
		}
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
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		User user = (User) session.getAttribute("user");
		String avatarUrl = user.getUsername();
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

	@RequestMapping(value = "/settings/changePassword", method = RequestMethod.POST)
	public String changePassword(HttpSession session, HttpServletRequest request,@ModelAttribute("oldPassword") String oldPassword,
							  @Valid @ModelAttribute("newPassword") String newPassword,@ModelAttribute("confirmPassword") String confirmPassword,
								 BindingResult result) throws SQLException, BadOperationException, NoSuchAlgorithmException, InvalidHashException, UserException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			return "login";
		}
		// TODO AJAX
		if (result.hasErrors()) {
			//TODO RETURN ERROR MESSAGE
			return "settings";
		} else {
			// System.out.println(newEmail==null);
			User user=(User)session.getAttribute("user");
			if(Hash.verify(oldPassword,user.getPassword())){
				if(newPassword.equals(confirmPassword)){
					String newPass=Hash.create(newPassword, Type.BCRYPT);
					userDao.changePassword(user, newPass);
				}
			}
		}
		return "settings";
	}

}