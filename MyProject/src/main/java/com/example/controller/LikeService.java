package com.example.controller;

import com.example.model.DBManagement.PostDao;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Marina on 2.11.2017 г..
 */
@RestController
@Controller
public class LikeService
{
    @Autowired
    PostDao postDao;

    @RequestMapping(value = "/like/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public String likePost(HttpSession session, Model model, HttpServletResponse resp , @PathVariable("postId") long postId) throws UserException, JsonProcessingException {
        Post post=null;
        ObjectMapper mapper=new ObjectMapper();
        try {
            System.out.println("=================================="+postId);
            post=postDao.getPostById(postId);
            long userId=((User)session.getAttribute("user")).getUserId();
            System.out.println("=========================================THIS IS THE USER ID: "+userId);
            if(postDao.existsReaction(postId,userId)){
                postDao.updateReaction(true, post.getId(),((User)session.getAttribute("user")).getUserId());
                System.out.println("THE POST IS NULL***********************");
                post.addPersonLiked(userId);
                post.removePersonDisliked(userId);
                resp.setStatus(200);
            }else{
                postDao.insertReaction(true, post.getId(), ((User)session.getAttribute("user")).getUserId());
                post.addPersonLiked(userId);
                resp.setStatus(201);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(post.getPeopleLiked().size());
    }


    @RequestMapping(value = "/unlike/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public String unlikePost(HttpSession session,HttpServletResponse resp ,@PathVariable("postId") long postId) throws UserException, SQLException, PostException {
        System.out.println("=================================="+postId);
        Post post=null;
        try {
            post=postDao.getPostById(postId);
            long userId=((User)session.getAttribute("user")).getUserId();
            postDao.deleteReaction( post.getId(), ((User) session.getAttribute("user")).getUserId());
            post.removePersonLiked(userId);
            //true=like false=dislike
            resp.setStatus(200);
        } catch (SQLException | PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(post.getPeopleLiked().size());
    }

    @RequestMapping(value = "/dislike/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public String dislikePost(HttpSession session,HttpServletResponse resp ,@PathVariable("postId") long postId) throws UserException{
        Post post=null;
        try {
            System.out.println("=================================="+postId);
            post=postDao.getPostById(postId);
            long userId=((User)session.getAttribute("user")).getUserId();
            if(postDao.existsReaction(postId,userId)){
                post.removePersonLiked(userId);
                post.addPersonDisliked(userId);
                postDao.updateReaction(false, post.getId(),((User)session.getAttribute("user")).getUserId());
                resp.setStatus(200);
            }else{
                postDao.insertReaction(false, post.getId(), ((User)session.getAttribute("user")).getUserId());
                post.addPersonDisliked(userId);
                resp.setStatus(201);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(post.getPeopleDisliked().size());
    }

    @RequestMapping(value = "/undislike/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public String undislikePost(HttpSession session,HttpServletResponse resp ,@PathVariable("postId") long postId) throws UserException{
        System.out.println("=================================="+postId);
        Post post=null;
        try {
            post=postDao.getPostById(postId);
            long userId=((User)session.getAttribute("user")).getUserId();
            postDao.deleteReaction( post.getId(), ((User) session.getAttribute("user")).getUserId());
            post.removePersonDisliked(userId);
            //true=like false=dislike
            resp.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(post.getPeopleDisliked().size());
    }

}
