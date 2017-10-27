package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Marina on 27.10.2017 г..
 */
@Controller
public class WelcomeController {

    @RequestMapping(value = "/wanderlust",method = RequestMethod.GET)
    public String getWelcomePage(){
        return "index";
    }

    @RequestMapping(value = "/myPassport",method = RequestMethod.GET)
    public String getMyPassport(){
        return "myPassport";
    }

    @RequestMapping(value = "/explore",method = RequestMethod.GET)
    public String getExplore(){
        return "explore";
    }

    @RequestMapping(value = "/newsfeed",method = RequestMethod.GET)
    public String getNewsfeed(){
        return "newsfeed";
    }


}
