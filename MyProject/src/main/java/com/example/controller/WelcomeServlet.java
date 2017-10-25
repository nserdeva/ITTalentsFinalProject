package com.example.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Marina on 23.10.2017 г..
 */
@WebServlet("")
public class WelcomeServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            // app scope setting all products
            boolean logged = false;
            //TODO See this
            if (request.getSession().getAttribute("logged") != null) {
                logged = (boolean) request.getSession().getAttribute("logged");
            }
            if (logged) {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }

    }

