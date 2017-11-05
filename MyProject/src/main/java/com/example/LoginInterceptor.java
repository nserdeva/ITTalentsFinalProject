package com.example;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by Marina on 25.10.2017 г..
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = Logger.getLogger("LoginInterceptor");


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("=========================PRE HANDLE LOGIN INTERCEPTOR ========================");
        if(!request.getRequestURI().equals("*") &&
                !request.getRequestURI().equals("/register")) {
            boolean isLogged = (Boolean) request.getSession().getAttribute("logged");
            if (!isLogged) {
                response.sendRedirect("*");
                return false;
            }
        }
        return true;
    }
}
