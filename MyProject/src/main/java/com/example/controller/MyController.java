package com.example.controller;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Marina on 25.10.2017 г..
 */
@Controller
public class MyController {

       @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String trialMethod(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("name","marina");
        return "marina";
    }
}
