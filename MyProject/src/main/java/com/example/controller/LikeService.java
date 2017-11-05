package com.example.controller;

import com.example.model.DBManagement.CommentDao;
import com.example.model.DBManagement.PostDao;
import com.example.model.Comment;
import com.example.model.Post;
import com.example.model.User;
import com.example.model.exceptions.CommentException;
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
import java.util.ArrayList;

/**
 * Created by Marina on 2.11.2017 г..
 */
@RestController
@Controller
public class LikeService
{
    @Autowired
    PostDao postDao;
    @Autowired
    CommentDao commentDao;


    @RequestMapping(value = "/like/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public Integer[] likePost(HttpSession session, Model model, HttpServletResponse resp , @PathVariable("postId") long postId) throws UserException, JsonProcessingException {
        Post post=null;
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
        Integer[] sizes=new Integer[2];
        sizes[0]=post.getPeopleLiked().size();
        sizes[1]=post.getPeopleDisliked().size();
        return sizes;
    }


    @RequestMapping(value = "/unlike/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public Integer[] unlikePost(HttpSession session,HttpServletResponse resp ,@PathVariable("postId") long postId) throws UserException, SQLException, PostException {
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
        Integer[] sizes=new Integer[2];
        sizes[0]=post.getPeopleLiked().size();
        sizes[1]=post.getPeopleDisliked().size();
        return sizes;
    }

    @RequestMapping(value = "/dislike/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public Integer[] dislikePost(HttpSession session,HttpServletResponse resp ,@PathVariable("postId") long postId) throws UserException{
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
        Integer[] sizes=new Integer[2];
        sizes[0]=post.getPeopleLiked().size();
        sizes[1]=post.getPeopleDisliked().size();
        return sizes;
    }

    @RequestMapping(value = "/undislike/{postId}",method = RequestMethod.POST)
    @ResponseBody
    public Integer[] undislikePost(HttpSession session,HttpServletResponse resp ,@PathVariable("postId") long postId) throws UserException{
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
        Integer[] sizes=new Integer[2];
        sizes[0]=post.getPeopleLiked().size();
        sizes[1]=post.getPeopleDisliked().size();
        return sizes;
    }
   
    //:::::Comment like/dislike functionality methods:::::
    

    @RequestMapping(value = "/likeComment/{commentId}",method = RequestMethod.POST)
    @ResponseBody
    public String likeComment(HttpSession session, Model model, HttpServletResponse resp , @PathVariable("commentId") long commentId) throws UserException, JsonProcessingException, CommentException {
        Comment comment = null;
        ObjectMapper mapper=new ObjectMapper();
        try {
            System.out.println("::::: E VLIZASH LI V SERVICA WEE "+ commentId);
            comment=commentDao.getCommentById(commentId);
            long userId=((User)session.getAttribute("user")).getUserId();
            if(commentDao.existsReaction(commentId,userId)){
                commentDao.updateReaction(true, comment.getId(),((User)session.getAttribute("user")).getUserId());
                comment.addPersonLiked(userId);
                comment.removePersonDisliked(userId);
                resp.setStatus(200);
            }else{
                commentDao.insertReaction(true, comment.getId(), ((User)session.getAttribute("user")).getUserId());
                comment.addPersonLiked(userId);
                resp.setStatus(201);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(comment.getPeopleLiked().size());
    }


    @RequestMapping(value = "/unlikeComment/{commentId}",method = RequestMethod.POST)
    @ResponseBody
    public String unlikeComment(HttpSession session,HttpServletResponse resp ,@PathVariable("commentId") long commentId) throws UserException, SQLException, PostException, CommentException {
        System.out.println(":::::::::::::: SHE UNLIKE-VAM MOTHAFUCKAAAA "+commentId);
        Comment comment=null;
        try {
            comment=commentDao.getCommentById(commentId);
            long userId=((User)session.getAttribute("user")).getUserId();
            commentDao.deleteReaction( comment.getId(), ((User) session.getAttribute("user")).getUserId());
            comment.removePersonLiked(userId);
            //true=like false=dislike
            resp.setStatus(200);
        } catch (SQLException | PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(comment.getPeopleLiked().size());
    }

    @RequestMapping(value = "/dislikeComment/{commentId}",method = RequestMethod.POST)
    @ResponseBody
    public String dislikeComment(HttpSession session,HttpServletResponse resp ,@PathVariable("commentId") long commentId) throws UserException, CommentException{
        Comment comment=null;
        try {
            System.out.println("::: SHE DISLAIKVAM MOTHAFUCKAAA "+commentId);
            comment=commentDao.getCommentById(commentId);
            long userId=((User)session.getAttribute("user")).getUserId();
            if(commentDao.existsReaction(commentId,userId)){
            	comment.removePersonLiked(userId);
            	comment.addPersonDisliked(userId);
            	commentDao.updateReaction(false, comment.getId(),((User)session.getAttribute("user")).getUserId());
                resp.setStatus(200);
            }else{
            	commentDao.insertReaction(false, comment.getId(), ((User)session.getAttribute("user")).getUserId());
                comment.addPersonDisliked(userId);
                resp.setStatus(201);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(comment.getPeopleDisliked().size());
    }

    @RequestMapping(value = "/undislikeComment/{commentId}",method = RequestMethod.POST)
    @ResponseBody
    public String undislikeComment(HttpSession session,HttpServletResponse resp ,@PathVariable("commentId") long commentId) throws UserException, CommentException{
        System.out.println(":: AAA SQ SHE UNDISLIKE-VAM:"+commentId);
        Comment comment=null;
        try {
            comment=commentDao.getCommentById(commentId);
            long userId=((User)session.getAttribute("user")).getUserId();
            commentDao.deleteReaction( comment.getId(), ((User) session.getAttribute("user")).getUserId());
            comment.removePersonDisliked(userId);
            //true=like false=dislike
            resp.setStatus(200);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        }
        return String.valueOf(comment.getPeopleDisliked().size());
    }

    
    
    
}
