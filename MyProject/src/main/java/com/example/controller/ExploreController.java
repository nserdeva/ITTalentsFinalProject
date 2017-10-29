package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.WebInitializer;
import com.example.model.Location;
import com.example.model.Multimedia;
import com.example.model.User;
import com.example.model.DBManagement.LocationDao;
import com.example.model.DBManagement.MultimediaDao;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.CategoryException;
import com.example.model.exceptions.LocationException;
import com.example.model.exceptions.PostException;

@Controller
public class ExploreController {
	@Autowired
	UserDao userDao;
	@Autowired
	LocationDao locationDao;
	@Autowired
	MultimediaDao multimediaDao;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(HttpSession session, HttpServletRequest request)
			throws SQLException, LocationException, CategoryException {
		ArrayList<String> checkBoxValues = new ArrayList<String>();
		checkBoxValues.add(request.getParameter("natureCheckBox")); // category_id must be '1' in db
		checkBoxValues.add(request.getParameter("seaCheckBox")); // category_id must be '2' in db
		checkBoxValues.add(request.getParameter("mountainsCheckBox")); // category_id must be '3' in db
		checkBoxValues.add(request.getParameter("dessertCheckBox")); // category_id must be '4' in db
		checkBoxValues.add(request.getParameter("landmarkCheckBox")); // category_id must be '5' in db
		checkBoxValues.add(request.getParameter("resortCheckBox")); // category_id must be '6' in db
		checkBoxValues.add(request.getParameter("cityCheckBox")); // category_id must be '7' in db
		System.out.println("'" + request.getParameter("searchFormDataTxt") + "'");
		userDao.setBrowsedLocations((User) session.getAttribute("user"), request.getParameter("searchFormDataTxt"),
				getCategoriesIds(checkBoxValues));
		return "explore";
	}

	private String getCategoriesIds(ArrayList<String> checkBoxValues) {
		checkBoxValues.trimToSize();
		StringBuilder sb = new StringBuilder();
		boolean firstFound = false;
		for (int i = 0; i < checkBoxValues.size(); i++) {
			if (checkBoxValues.get(i) != null && checkBoxValues.get(i).equals("true")) {
				if (firstFound) {
					sb.append(",");
				}
				sb.append((i + 1));
				firstFound = true;
			}
		}
		return sb.toString();
	}

	@RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
	public String getLocationPage(@PathVariable("id") int id, HttpSession session) throws CategoryException {
		try {
			Location selectedLocation = locationDao.getLocationById((id));
			session.setAttribute("location", selectedLocation);
		} catch (NumberFormatException | SQLException | LocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "location";
	}

	@RequestMapping(value = "/location/getMainPic/{id}", method = RequestMethod.GET)
	public void getLocationMainPic(@PathVariable("id") int id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException {
		Location selectedLocation = locationDao.getLocationById(id);
		locationDao.setPictures(selectedLocation);
		String locationMainPicUrl = selectedLocation.getMainPic().getUrl();
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
					+ WebInitializer.LOCATIONS_PICTURES_LOCATION + File.separator + locationMainPicUrl);
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/location/picture/{id}", method = RequestMethod.GET)
	public void getLocationPicture(@PathVariable("id") int id, HttpServletResponse response)
			throws SQLException, LocationException, CategoryException {
		try {
			File myFile = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION
					+ WebInitializer.LOCATIONS_PICTURES_LOCATION + File.separator + multimediaDao.getMultimediaById(id).getUrl());
			OutputStream out = response.getOutputStream();
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PostException e) { //e stiga we ?????
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}