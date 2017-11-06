package com.example.controller;

import com.example.WebInitializer;
import com.example.model.*;
import com.example.model.DBManagement.LocationDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.DBManagement.TagDao;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Marina on 25.10.2017 ?..
 */
@Controller
public class PostController {
    @Autowired
    PostDao postDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TagDao tagDao;
    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/uploadPost", method = RequestMethod.GET)
    public String getUploadPostForm(HttpSession session,HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
            return "login";
        }
        return "uploadPost";
    }

    @RequestMapping(value = "/uploadPost", method = RequestMethod.POST)
    public String uploadPost(@RequestParam("description") String description, @RequestParam("locationName") String locationName,
                             @RequestParam("latitude") String latitude, @RequestParam("longtitude") String longtitude,
                             @RequestParam("taggedPeople") String taggedPeople, @RequestParam("tags") String tagNames,
                             @RequestParam("categories") String categoryNames, @RequestParam("image1") MultipartFile image1,
                             @RequestParam("image2") MultipartFile image2, @RequestParam("image3") MultipartFile image3,
                             @RequestParam("video") MultipartFile video1, HttpSession session){
        if(session.getAttribute("user")==null || session.getAttribute("logged").equals(false)){
            return "login";
        }
        if("".equals(description) && "".equals(locationName) && "".equals(latitude) && "".equals(longtitude) && "".equals(taggedPeople)
                && "".equals(tagNames) && "".equals(categoryNames) && image1.getSize()==0 && image2.getSize()==0
                && image3.getSize()==0 && video1.getSize()==0){
            return "redirect:/myPassport";
        }
        Post post=null;


        try {
            User user=(User)session.getAttribute("user");
            Location location=getLocation(locationName, latitude, longtitude);
            HashSet<User> taggedUsers=getTaggedUsers(taggedPeople);
            HashSet<Tag> tags=getTags(tagNames);
            HashSet<Category> categories=getCategories(categoryNames);
            if(location==null && taggedUsers.size()==0 && categories.size()==0){
                return "redirect:/myPassport";
            }
            HashSet<Multimedia> multimedia= getImages(image1, image2, image3);
            Multimedia video=null;
            if(video1.getSize() != 0){
                video=readMultimedia(video1,true);
            }
            post=new Post(user,description,video,location,categories,multimedia,taggedUsers,tags);
            postDao.insertNewPost(post);
            //user.setPosts(postDao.getPostsForUser(user));
            userDao.setPosts(user);
        } catch (PostException e) {
            e.printStackTrace();
        } catch (CategoryException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MultimediaException e) {
            e.printStackTrace();
        }  catch (LocationException e) {
            e.printStackTrace();
        } catch (CommentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //put everything in database
        //reload posts
        //return to my profile
        return "redirect:/myPassport";
    }

    private Location getLocation(String locationInput, String latitude, String longtitude) throws LocationException, SQLException, IOException {

        /*if (!"".equals(locationInput)) {
            if(locationDao.existsLocation(locationInput, latitude, longtitude)){
                location = locationDao.getLocationByName(locationInput);
                System.out.println("************************ADDED LOCATION: "+location.getLocationName());
            }
        }*/

        Location location1=new Location(latitude,longtitude, "", locationInput);
        //location=locationDao.insertLocation(location1);
        return location1;
    }


    private HashSet<Multimedia> getImages(MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        HashSet<Multimedia> images=new HashSet<>();
        if(image1.getSize() != 0){
            Multimedia multimedia1=readMultimedia(image1,false);
            images.add(multimedia1);
        }
        if(image2.getSize() != 0){
            Multimedia multimedia2=readMultimedia(image2,false);
            images.add(multimedia2);
        }
        if(image3.getSize() != 0){
            Multimedia multimedia3=readMultimedia(image3,false);
            images.add(multimedia3);
        }
        System.out.println(images.size()+" IMAGES SIZE...................................................");
        return images;
    }

    private Multimedia readMultimedia(MultipartFile file, boolean isVideo) {

        Multimedia transferredFile=null;

        String fileUrl =System.currentTimeMillis()+file.getOriginalFilename();
        try {
            if(!file.isEmpty()){
                File f = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION+File.separator + fileUrl);
                file.transferTo(f);

                    /*System.out.println("MULTIMEDIA URL IN readMultimedia: "+transferredFile.getUrl());
                    System.out.println(transferredFile.isVideo());*/
                return new Multimedia(fileUrl,isVideo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ABE DA NE BI DA VLIZASH TUK???????????????????????");
        }
        return transferredFile;
    }


    private HashSet<Category> getCategories(String input) {
        HashSet<Category> categories=new HashSet<>();
        if(!"".equals(input)){
            String[] splitCategories=input.split(",");
            System.out.println("***********COUNT OF CATEGORIES "+splitCategories.length);

            for (int i = 0; i < splitCategories.length; i++) {
                String categoryName=splitCategories[i];
                if(!"".equals(categoryName)){
                    categoryName=categoryName.trim();
                    categoryName=categoryName.replace("]","");
                    categoryName=categoryName.replace("[","");
                    HashMap<String,Category> applicationScopeCategories=(HashMap<String, Category>)servletContext.getAttribute("categories");
                    if(applicationScopeCategories.containsKey(categoryName)){
                        Category category=applicationScopeCategories.get(categoryName);
                        categories.add(category);
                        System.out.println("//////////////////////////////////////////////////");
                        System.out.println("Category added: "+category.getName());
                        System.out.println("Category id: "+category.getId());
                    }
                }
            }
        }
        return categories;
    }

    private HashSet<Tag> getTags(String input) {
        HashSet<Tag> tags = new HashSet<>();
        if (!"".equals(input)) {
            String[] splitTags = input.split(",");
            for (int i = 0; i < splitTags.length; i++) {
                String tagName = splitTags[i];
                tagName=tagName.trim();
                tagName=tagName.replace("]","");
                tagName=tagName.replace("[","");
                if (!"".equals(tagName)) {
                    if (((HashSet<String>) servletContext.getAttribute("tags")).contains(tagName)) {
                        Tag tag = tagDao.getTagByTagName(tagName);
                        tags.add(tag);
                        System.out.println("****************CONTAINS "+tagName);
                    } else {
                        Tag tag = tagDao.insertTag(tagName);
                        tags.add(tag);
                        System.out.println("****************DOES NOT CONTAIN "+tagName);
                    }
                }
            }
        }
        return tags;
    }

    private HashSet<User> getTaggedUsers(String input) {
        HashSet<User> users=new HashSet<>();
        if(!"".equals(input)){
            String[] splitUsernames=input.split(",");
            for (int i = 0; i < splitUsernames.length; i++) {
                try {
                    String currentUsername=splitUsernames[i];
                    currentUsername=currentUsername.replace("]","");
                    currentUsername=currentUsername.replace("[","");
                    currentUsername=currentUsername.trim();
                    if(!"".equals(currentUsername)){
                        if(userDao.existsUsername(currentUsername)){
                            User user=userDao.getUserByUsername(currentUsername);
                            users.add(user);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (UserException e) {
                    e.printStackTrace();
                } catch (PostException e) {
                    e.printStackTrace();
                } catch (LocationException e) {
                    e.printStackTrace();
                } catch (CategoryException e) {
                    e.printStackTrace();
                } catch (CommentException e) {
                    e.printStackTrace();
                }
            }
        }

        return users;
    }









}
