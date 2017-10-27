package com.example.controller;

import com.example.model.DBManagement.UserDao;
import com.example.model.User;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Created by Marina on 26.10.2017 г..
 */
@Controller
public class UserController {
    @Autowired
    UserDao userDao;

    @RequestMapping(value = "*",method = RequestMethod.GET)
    public String login(Model model){
        //TODO CHECK IF LOGGED IN
        //model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "*",method = RequestMethod.POST)
    public String logUser(HttpSession session, HttpServletRequest request){
        String username=request.getParameter("user");
        String password=request.getParameter("pass");
        //TODO HASHING
        try {
            if(userDao.existsUser(username, password)){
                User user=userDao.getUserByUsername(username);
                userDao.setPosts(user);
                userDao.setFollowers(user);
                userDao.setFollowing(user);
                userDao.setVisitedLocations(user);
                userDao.setWishlistLocations(user);
                session.setAttribute("user", user);
                session.setAttribute("logged", true);
                return "myPassport";
            }else{
                request.setAttribute("isValidData",false);
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
        return "myPassport";
    }

    @RequestMapping(value="/register", method=RequestMethod.GET)
    public String getRegister(){
        //TODO check if logged
        return "register";
    }

    @RequestMapping(value = "/register", method =RequestMethod.POST)
    public String registerUser(HttpServletRequest request, HttpSession session){
        String username=request.getParameter("user");
        String pass=request.getParameter("pass");
        String pass2=request.getParameter("pass2");
        String email=request.getParameter("email");
        if(pass!= null && pass.equals(pass2)){
            try {
                if(!userDao.existsUsername(username)){
                    User user=new User(username, pass, email);
                    userDao.insertUser(user);
                    session.setAttribute("user",user);
                    session.setAttribute("logged", true);
                    return "myPassport";
                }else{
                    System.out.println("second if- else");
                    return "register";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (UserException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("first if- else");

            return "register";
        }
        System.out.println("last if- else");
        return "myPassport";
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.setAttribute("logged", false);
        session.invalidate();
        return "login";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String arrangeSettings( HttpSession session){
	        //TODO CHECK IF LOGGED
        return "settings";
    }
    
    
    @RequestMapping(value = "/settings/changeDescription",method = RequestMethod.GET)
    public String getChangeDescriptionForm(HttpSession session, HttpServletRequest request) throws SQLException{   
    	return "settings";
    }
    

    @RequestMapping(value = "/settings/changeDescription",method = RequestMethod.POST)
    public String changeDescription(HttpSession session, HttpServletRequest request ) throws SQLException{
    	String newDescription = request.getParameter("descriptionTxt");
    	System.out.println(newDescription==null);
    	userDao.changeDescription((User)session.getAttribute("user"), newDescription);
    	return "settings";
    }

    
}
