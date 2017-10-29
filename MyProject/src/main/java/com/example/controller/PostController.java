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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        List<MultipartFile> images=new ArrayList<>() ;
        model.addAttribute("newPost", post);
        model.addAttribute("images", images);
        return "uploadPost";
    }

   /* @RequestMapping(value = "/uploadPost", method = RequestMethod.POST)
    public String insertPost(HttpServletRequest request, @ModelAttribute Post post, @ModelAttribute ArrayList<MultipartFile> images, Model model ){
        //Get the uploaded files and store them
        if (null != images && images.size() > 0)
        {
            for (MultipartFile multipartFile : images) {

                String fileName = multipartFile.getOriginalFilename();
                File image = new File(WebInitializer.LOCATION + File.separator + multipartFile.getOriginalFilename());
                Multimedia multimedia1=new Multimedia(image.getAbsolutePath(),false, post);
                post.addmultimedia(fileName);

                File imageFile = new File(HttpServletRequest.getServletContext().getRealPath("/image"), fileName);
                try
                {
                    multipartFile.transferTo(imageFile);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Here, you can save the product details in database

        model.addAttribute("product", product);
        return "viewProductDetail";
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


        @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
        public String uploadAvatar(HttpServletRequest request, @ModelAttribute Post post) {
            User user = (User) request.getSession().getAttribute("user");
            String avatarUrl = file.getOriginalFilename() +".jpg";
            //Get the uploaded files and store them
            List<MultipartFile> files = post.getImages();
            List<String> fileNames = new ArrayList<String>();
            if (null != files && files.size() > 0)
            {
                for (MultipartFile multipartFile : files) {

                    String fileName = multipartFile.getOriginalFilename();
                    fileNames.add(fileName);

                    File imageFile = new File(servletRequest.getServletContext().getRealPath("/image"), fileName);
                    try
                    {
                        multipartFile.transferTo(imageFile);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            // Here, you can save the product details in database

            model.addAttribute("product", product);
            return "viewProductDetail";
        try {
            if (file.isEmpty()) {
                //apache tika
                //ne e kacheno nishto c if
                return "myprofile";
            }
            File f = new File(WebInitializer.LOCATION + File.separator + avatarUrl);
            file.transferTo(f);
            //postDao.insertImg()
            //request.getSession().setAttribute("avatar", avatarUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "myprofile";
    }
*/

}
