package com.example.controller;

import model.User;
import model.UserDao;
import model.exceptions.UserException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Marina on 18.10.2017 г..
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check for user credentials
        String username=request.getParameter("user");
        String password=request.getParameter("pass");
        String password2=request.getParameter("pass2");
        String email=request.getParameter("email");
        if(!password.equals(password2)){
            request.setAttribute("error", "passwords missmatch");
            request.getRequestDispatcher("register.jsp").forward(request,response);
            return;
        }
        try {
            if(UserDao.getInstance().existsUser(username, password)){
                request.setAttribute("error","user already exists");
                request.getRequestDispatcher("register.jsp").forward(request,response);
                return;
            }else{
                User user=new User(username, password, email);
                UserDao.getInstance().insertUser(user);
                request.getSession().setAttribute("user",user);
                request.getRequestDispatcher("login.jsp").forward(request,response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "database problem : " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
