package com.example.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.User;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.LocationException;

/**
 * Created by Marina on 27.10.2017 г..
 */
@Controller
public class WelcomeController {
	@Autowired
	UserDao userDao;

    @RequestMapping(value = "/wanderlust",method = RequestMethod.GET)
    public String getWelcomePage(){
        return "index";
    }

    @RequestMapping(value = "/myPassport",method = RequestMethod.GET)
    public String getMyPassport(){
        return "myPassport";
    }
    
	@RequestMapping(value = "/explore",method = RequestMethod.GET)
    public String explore(HttpSession session, HttpServletRequest request ) throws SQLException, LocationException{
       	return "explore";
    }

    @RequestMapping(value = "/newsfeed",method = RequestMethod.GET)
    public String getNewsfeed(){
        return "newsfeed";
    }

}