package com.example.controller;

import com.example.model.DBManagement.LocationDao;
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
    public Collection<Location> getVisitedPlaces(HttpServletResponse response, @PathVariable("userId") long userId) throws SQLException, PostException, UserException, LocationException {
        System.out.println("THIS IS THE USER ID: "+userId);
        User user=userDao.getUserById(userId);
        Collection<Location> locations=userDao.getVisitedLocations(user).values();
        response.setStatus(200);
        return locations;
    }
}
