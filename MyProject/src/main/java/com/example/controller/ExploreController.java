package com.example.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.User;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.LocationException;

@Controller
public class ExploreController {
	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(HttpSession session, HttpServletRequest request) throws SQLException, LocationException {
		ArrayList<String> checkBoxValues = new ArrayList<String>();
		checkBoxValues.add(request.getParameter("natureCheckBox")); // category_id must be '1' in db
		checkBoxValues.add(request.getParameter("seaCheckBox")); // category_id must be '2' in db
		checkBoxValues.add(request.getParameter("mountainsCheckBox")); // category_id must be '3' in db
		checkBoxValues.add(request.getParameter("dessertCheckBox")); // category_id must be '4' in db
		checkBoxValues.add(request.getParameter("landmarkCheckBox")); // category_id must be '5' in db
		checkBoxValues.add(request.getParameter("resortCheckBox")); // category_id must be '6' in db
		checkBoxValues.add(request.getParameter("cityCheckBox")); // category_id must be '7' in db
		System.out.println("'" + request.getParameter("searchFormDataTxt") + "'");
		userDao.setBrowsedLocations((User) session.getAttribute("user"), request.getParameter("searchFormDataTxt"), getCategoriesIds(checkBoxValues));
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
	

    @RequestMapping(value = "/location",method = RequestMethod.GET)
    public String getLocationPage(){
        return "location";
    }


}