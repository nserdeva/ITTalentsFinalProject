package com.example.model.DBManagement;

import com.example.model.Location;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.exceptions.*;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */

@Component
public class LocationDao extends AbstractDao {

	@Autowired
	CategoryDao categoryDao;
	@Autowired
	MultimediaDao multimediaDao;

	// tested
	public void insertLocation(Location location) throws SQLException, LocationException {
		try {
			PreparedStatement ps = this.getConnection().prepareStatement(
					"insert into locations( latitude,longtitude, description, location_name) values (?,?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, location.getLatitude());
			ps.setString(2, location.getLongtitude());
			ps.setString(3, location.getDescription());
			ps.setString(4, location.getLocationName());
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			location.setId(rs.getLong(1));
		} catch (SQLException e) {
			throw new LocationException("Location could not be added. Reason: " + e.getMessage());
		}
	}

	// tested
	public Location getLocationById(long id) throws SQLException, LocationException, CategoryException {
		Location location;
		try {
			PreparedStatement ps = this.getConnection().prepareStatement(
					"SELECT latitude, longtitude, description, location_name FROM locations where location_id=?;");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			location = new Location(id, rs.getString("latitude"), rs.getString("longtitude"),
					rs.getString("description"), rs.getString("location_name"));
			this.setPictures(location);
		} catch (SQLException e) {
			throw new LocationException("Location could not be loaded. Reason: " + e.getMessage());
		}
		return location;
	}

	// tested
	public Location getLocationByPost(Post post) throws SQLException, LocationException {
		PreparedStatement ps = this.getConnection()
				.prepareStatement("select l.location_id, l.latitude, l.longtitude, l.description, l.location_name"
						+ " from locations as l join posts " + "on posts.location_id=l.location_id"
						+ " where post_id=?;");
		ps.setLong(1, post.getId());
		ResultSet rs = ps.executeQuery();
		rs.next();
		Location location = new Location(rs.getLong("l.location_id"), rs.getString("l.latitude"),
				rs.getString("l.longtitude"), rs.getString("l.description"), rs.getString("l.location_name"));
		return location;
	}

	public HashSet<Location> getFilteredLocations(String searchFormText)
			throws LocationException, SQLException, CategoryException {
		HashSet<Location> filteredLocations = new HashSet<Location>();
		if (searchFormText != null && !searchFormText.isEmpty()) {
			try (PreparedStatement ps = this.getConnection().prepareStatement(
					"select distinct location_id, latitude, longtitude, description, location_name from locations where location_name like ? or description like ?;");) {
				ps.setString(1, "%" + searchFormText+"%");
				ps.setString(2, "%" + searchFormText+"%");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Location location = new Location(rs.getLong("location_id"), rs.getString("latitude"),
							rs.getString("longtitude"), rs.getString("description"), rs.getString("location_name"));
					this.setPictures(location);
					filteredLocations.add(location);
				}
			}
		}
		return filteredLocations;
	}

	public void setPictures(Location l) throws SQLException, CategoryException {
		l.setPictures(multimediaDao.getPicturesForLocation(l));
	}

    public Location getLocationByName(String locationName) {
		Location location=null;
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT location_id, latitude, " +
					"longtitude, description FROM locations WHERE location_name=?");
			ps.setString(1,locationName);
			ResultSet rs=ps.executeQuery();
			rs.next();
			location=new Location(rs.getLong("location_id"), rs.getString("latitude"),
					rs.getString("longtitude"),rs.getString("description"),locationName);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LocationException e) {
			e.printStackTrace();
		}
		return location;
	}
}