package com.example.controller;

import com.example.model.DBManagement.UserDao;
import com.example.model.Location;
import com.example.model.User;
import com.example.model.exceptions.LocationException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Marina on 5.11.2017 г..
 */
@RestController
@Controller
public class LocationsService {
    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/getVisitedPlaces/{userId}",method = RequestMethod.GET)
    @ResponseBody
    public Collection<Location> getVisitedPlaces(HttpSession session,HttpServletResponse response, @PathVariable("userId") long userId) throws IOException {
		if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
			response.sendRedirect("login");
		}
    	User user = null;
    	Collection<Location> locations = null;
    	try {
    	user=userDao.getUserById(userId);
        locations=userDao.getVisitedLocations(user).values();
        response.setStatus(200);
    	} catch (SQLException e) {
			e.printStackTrace();
		} catch (LocationException e) {
			e.printStackTrace();
		} catch (PostException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
        return locations;
    }
    
}