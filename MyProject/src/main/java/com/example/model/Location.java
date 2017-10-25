package com.example.model;


import com.example.model.exceptions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Marina on 15.10.2017 ?..
 */
public class Location {
    private final static int MAX_LENGTH = 255;
    private final static int MIN_LENGTH = 5;
    private final static String COORDINATES_PATTERN = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$\n";

    private long id;
    private String latitude;
    private String longtitude;
    private String description;
    private String locationName;
    private ConcurrentSkipListSet<User> peopleVisited;

    //constructor to be used when putting object in database

    Location(String latitude, String longtitute, String description, String locationName) throws LocationException {
        this.setLatitude(latitude);
        this.setLongtitude(longtitude);
        this.setDescription(description);
        this.setLocationName(locationName);
        this.peopleVisited = new ConcurrentSkipListSet<>();
    }

    //constructor to be used when fetching from database
    public Location(long id, String latitude, String longtitute, String description, String locationName) throws LocationException {
        this(latitude, longtitute, description, locationName);
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        //TODO validations for latitude
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return this.longtitude;
    }

    public void setLongtitude(String longtitude) {
        //TODO validations for longtitude
        this.longtitude = longtitude;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setLocationName(String locationName) throws LocationException {
        if (!locationName.isEmpty()) {
            if (locationName.length() < MIN_LENGTH) {
                throw new LocationException("Name of the category is too short. It should be more than " + MIN_LENGTH + " symbols.");
            } else if (locationName.length() > MAX_LENGTH) {
                throw new LocationException("Name of the category is too long. It should be less than" + MAX_LENGTH + " symbols");
            }
        } else {
            throw new LocationException("Name of the category should not be empty!");
        }
        this.locationName = locationName;
    }

    public Collection<User> getPeopleVisited() {
        return Collections.unmodifiableCollection(this.peopleVisited);
    }

    public void setPeopleVisited(ConcurrentSkipListSet<User> peopleVisited) {
        this.peopleVisited = peopleVisited;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return id == location.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
