package com.example.controller;

import com.example.WebInitializer;
import com.example.model.*;
import com.example.model.DBManagement.LocationDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.DBManagement.TagDao;
import com.example.model.DBManagement.UserDao;
import com.example.model.exceptions.*;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public String getUploadPostForm(HttpServletRequest request, HttpServletResponse response){
        return "uploadPost";
    }

    @RequestMapping(value = "/uploadPost", method = RequestMethod.POST)
    public String uploadPost(@RequestParam("description") String description, @RequestParam("location") String locationName,
                             @RequestParam("taggedPeople") String taggedPeople, @RequestParam("tags") String tagNames,
                             @RequestParam("categories") String categoryNames, @RequestParam("image1") MultipartFile image1,
                             @RequestParam("image2") MultipartFile image2, @RequestParam("image3") MultipartFile image3,
                             @RequestParam("video") MultipartFile video1, HttpSession session){
        Post post=null;
        try {
            User user=(User)session.getAttribute("user");
            Location location=locationDao.getLocationByName(locationName);
            HashSet<User> taggedUsers=getTaggedUsers(taggedPeople);
            HashSet<Tag> tags=getTags(tagNames);
            HashSet<Category> categories=getCategories(categoryNames);
            HashSet<Multimedia> multimedia=getMultimedia(image1,image2,image3);
            Multimedia video=readMultimedia(video1,true);
            post=new Post(user,description,video,location,categories,multimedia,taggedUsers,tags);
            postDao.insertNewPost(post);
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
        }
        //put everything in database
        //reload posts
        //return to my profile
        return "myPassport";
    }

    private HashSet<Multimedia> getMultimedia(MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        Multimedia multimedia1=readMultimedia(image1,false);
        Multimedia multimedia2=readMultimedia(image2,false);
        Multimedia multimedia3=readMultimedia(image3,false);
        HashSet<Multimedia> images=new HashSet<>();
        images.add(multimedia1);
        images.add(multimedia2);
        images.add(multimedia3);
        return images;
    }

    private Multimedia readMultimedia(MultipartFile file, boolean isVideo) {
        Multimedia transferredFile=null;
        //trying to get a unique file name
        String extention;
        if(isVideo){
            extention=".mp4";
        }else{
            extention=".jpg";
        }
        String fileUrl = file.getOriginalFilename()+System.currentTimeMillis()+extention;
        try {
            if(!file.isEmpty()){
                File f = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION+File.separator + fileUrl);
                file.transferTo(f);
                transferredFile=new Multimedia(fileUrl,isVideo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transferredFile;
    }


    private HashSet<Category> getCategories(String input) {
        HashSet<Category> categories=new HashSet<>();
        String[] splitCategories=input.split("\\w+");
        for (int i = 0; i < splitCategories.length; i++) {
            String categoryName=splitCategories[i];
            Category category=((HashMap<String, Category>)servletContext.getAttribute("categories")).get(categoryName);
            categories.add(category);
        }
        return categories;
    }

    private HashSet<Tag> getTags(String input) {
        HashSet<Tag> tags=new HashSet<>();
        String[] splitTags=input.split("\\w+");
        for (int i = 0; i < splitTags.length; i++) {
            String tagName=splitTags[i];

            if(((HashSet<String> )servletContext.getAttribute("tags")).contains(tagName)){
                Tag tag=tagDao.getTagByTagName(tagName);
                tags.add(tag);
            }else{
                Tag tag=tagDao.insertTag(tagName);
                tags.add(tag);
            }
        }
        return tags;
    }

    private HashSet<User> getTaggedUsers(String input) {
        HashSet<User> users=new HashSet<>();
        String[] splitUsernames=input.split("\\w+");
        for (int i = 0; i < splitUsernames.length; i++) {
            try {
                User user=userDao.getUserByUsername(splitUsernames[i]);
                users.add(user);
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
        return users;
    }


    @RequestMapping(value = "/like/{postId}",method = RequestMethod.POST)
    public void likePost(HttpServletResponse resp ,@PathVariable("postId") long postId){
        try {
            System.out.println("=================================="+postId);
            Post post=postDao.getPostById(postId);
            postDao.incrementLikes(post);
            resp.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/unlike/{postId}",method = RequestMethod.POST)
    public void unlikePost(HttpServletResponse resp ,@PathVariable("postId") long postId){
        try {
            System.out.println("=================================="+postId);
            Post post=postDao.getPostById(postId);
            postDao.decrementLikes(post);
            resp.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/dislike/{postId}",method = RequestMethod.POST)
    public void dislikePost(HttpServletResponse resp ,@PathVariable("postId") long postId){
        try {
            System.out.println("=================================="+postId);
            Post post=postDao.getPostById(postId);
            postDao.incrementDislikes(post);
            resp.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/undislike/{postId}",method = RequestMethod.POST)
    public void undislikePost(HttpServletResponse resp ,@PathVariable("postId") long postId){
        try {
            System.out.println("=================================="+postId);
            Post post=postDao.getPostById(postId);
            postDao.decrementDislikes(post);
            resp.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }

    }






}
