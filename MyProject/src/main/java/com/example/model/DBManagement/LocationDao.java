package com.example.model.DBManagement;

import com.example.model.Location;
import com.example.model.Post;
import com.example.model.exceptions.*;
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
	public Location insertLocation(Location location) throws SQLException, LocationException {
		try {
			PreparedStatement ps = this.getConnection().prepareStatement(
					"insert into locations( latitude,longtitude, description, location_name) values (?,?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, location.getLatitude());
			ps.setString(2, location.getLongtitude());
			ps.setString(3, location.getDescription());
			ps.setString(4, location.getLocationName());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				location.setId(rs.getLong(1));
			}
		} catch (SQLException e) {
			throw new LocationException("Location could not be added. Reason: " + e.getMessage());
		}
		if(location==null){
			System.out.println("********************************************FRESHLY INSERTED LOCATION IS NULL");
		}
		return location;
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
		Location location = null;
		PreparedStatement ps = this.getConnection()
				.prepareStatement("select locations.location_id,locations.latitude, locations.longtitude," +
						"locations.description, locations.location_name from locations  " +
						"join posts on posts.location_id=locations.location_id where posts.post_id=?;");
		ps.setLong(1, post.getId());
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			location = new Location(rs.getLong("locations.location_id"), rs.getString("locations.latitude"),
					rs.getString("locations.longtitude"), rs.getString("locations.description"), rs.getString("locations.location_name"));
		}
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
				PreparedStatement ps=this.getConnection().prepareStatement("SELECT location_id, latitude, longtitude, description FROM locations WHERE location_name LIKE ?");
				ps.setString(1,locationName);
				ResultSet rs=ps.executeQuery();
				if(rs.next()){
					location=new Location(rs.getLong("location_id"), rs.getString("latitude"),
							rs.getString("longtitude"),rs.getString("description"),locationName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (LocationException e) {
				e.printStackTrace();
			}
		return location;
	}

    public HashSet<String> getAllLocationNames() {
		HashSet<String> locations=new HashSet<>();
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT location_name FROM locations");
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				String locationName=rs.getString("location_name");
				locations.add(locationName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locations;
    }

	public boolean existsLocationInDb(Location location) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT COUNT(*) FROM locations WHERE location_name LIKE ? AND latitude LIKE ? AND longtitude LIKE ?");
			ps.setString(1,location.getLocationName());
			ps.setString(2,location.getLatitude());
			ps.setString(3,location.getLongtitude());
			ResultSet rs=ps.executeQuery();
			rs.next();
				if(rs.getInt("COUNT(*)")>0){
					return true;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean existsLocation(String locationName) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT COUNT(*) FROM locations WHERE location_name LIKE ?");
			ps.setString(1,locationName);
			ResultSet rs=ps.executeQuery();
			if(rs.getInt("COUNT(*)")>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Location getLocation(Location location) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("SELECT location_id FROM locations WHERE location_name LIKE ? AND latitude LIKE ? AND longtitude LIKE ?");
			ps.setString(1,location.getLocationName());
			ps.setString(2,location.getLatitude());
			ps.setString(3,location.getLongtitude());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				location.setId(rs.getLong("location_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return location;
	}

	public void insertVisitedLocation(long userId, long id) {
		try{
			PreparedStatement ps=this.getConnection().prepareStatement("INSERT INTO visited_locations(user_id, location_id, date_time) VALUES(?,?,now());");
			ps.setLong(1,userId);
			ps.setLong(2,id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}