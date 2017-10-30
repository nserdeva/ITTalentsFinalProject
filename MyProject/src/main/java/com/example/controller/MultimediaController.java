package com.example.controller;

import com.example.WebInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Marina on 29.10.2017 г..
 */
@Controller
public class MultimediaController {

    @RequestMapping(value = "/getVideo/{url}", method = RequestMethod.GET)
    public void getUploadedImagesForm(@PathVariable("url") String tempUrl, HttpSession session, HttpServletResponse resp) {
            try {
                String url=tempUrl+".mp4";
            File tempImage = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION +File.separator+url);
            OutputStream out = resp.getOutputStream();
            Path path = tempImage.toPath();
            Files.copy(path, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getMultimedia/{url}", method = RequestMethod.GET)
    public void getVideo(@PathVariable("url") String tempUrl, HttpSession session, HttpServletResponse resp) {
        try {
            String url=tempUrl+".jpg";
            File tempImage = new File(WebInitializer.LOCATION + WebInitializer.MULTIMEDIA_LOCATION +File.separator+url);
            OutputStream out = resp.getOutputStream();
            Path path = tempImage.toPath();
            Files.copy(path, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
