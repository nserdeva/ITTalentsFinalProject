package com.example.controller;

import model.PostDao;
import model.User;
import model.UserDao;
import model.exceptions.CategoryException;
import model.exceptions.CommentException;
import model.exceptions.LocationException;
import model.exceptions.PostException;
import model.exceptions.UserException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Marina on 21.10.2017 ??..
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("user");
		String password = request.getParameter("pass");

		try {
			if (UserDao.getInstance().existsUser(username, password)) {
				request.getSession().setAttribute("logged", true);
				User user=UserDao.getInstance().getUserByUsername(username);
				request.getSession().setAttribute("user", user);
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			} else {
				request.setAttribute("isValidData", "false");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
		} catch (SQLException | UserException e) {
			e.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} catch (PostException e) {
			e.printStackTrace();
		} catch (LocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CategoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}