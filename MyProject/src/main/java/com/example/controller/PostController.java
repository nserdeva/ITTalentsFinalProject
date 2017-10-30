package com.example.controller;

import com.example.WebInitializer;
import com.example.model.DBManagement.PostDao;
import com.example.model.Multimedia;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.exceptions.CategoryException;
import com.example.model.exceptions.MultimediaException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Marina on 25.10.2017 ?..
 */
@Controller
public class PostController {
    @Autowired
    PostDao postDao;

    @RequestMapping(value = "/uploadPost", method = RequestMethod.GET)
    public String getUploadPostForm(Model model){
        //create an empty post for uploading
        Post post=new Post();
        //create list to fill the images
        List<MultipartFile> images=new ArrayList<>();
        //send them to the model
        model.addAttribute("newPost", post);
        model.addAttribute("images", images);
        //return form for uploading a post
        return "uploadPost";
    }

    @RequestMapping(value = "/uploadPost", method = RequestMethod.POST)
    public String uploadPost(@ModelAttribute("post") Post post, @RequestParam("images") ArrayList<MultipartFile> images,Model model){
        //get all the information from the form (Post)
        //foreach list with images and transfer them to real files
        //set the arraylist with images to the set of multimedia of the post
        //put everything in database
        //return to my profile
        return "myPassport";
    }

    @RequestMapping(value = "/uploadPost/uploadImg", method = RequestMethod.POST)
    public void uploadImg(@RequestParam("img") MultipartFile image,@RequestParam("images") ArrayList<MultipartFile> images){
            //get the uploaded picture
            //add it to the list with images
            if(image==null || image.isEmpty()){
                //TODO NOT SURE HERE
                return;
            }
            images.add(image);
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
