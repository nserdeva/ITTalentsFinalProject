package com.example.controller;

import com.example.model.DBManagement.PostDao;
import com.example.model.Post;
import com.example.model.exceptions.CategoryException;
import com.example.model.exceptions.MultimediaException;
import com.example.model.exceptions.PostException;
import com.example.model.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.SQLException;

/**
 * Created by Marina on 25.10.2017 г..
 */
@Controller
public class PostController {
    @Autowired
    PostDao postDao;

    @RequestMapping(value = "/uploadPost", method = RequestMethod.GET)
    public String uploadPost(Model model){
        Post post=new Post();
        model.addAttribute("newPost", post);
        return "uploadPost";
    }

    @RequestMapping(value = "/uploadPost", method = RequestMethod.POST)
    public String insertPost(Model model){
        Post post=new Post();
        try {
            postDao.insertNewPost(post);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CategoryException e) {
            e.printStackTrace();
        } catch (PostException e) {
            e.printStackTrace();
        } catch (MultimediaException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        }
        return "myPassport";
    }
}
