package com.sneakershop.sneakershop.controller;

import com.sneakershop.sneakershop.model.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    //Страница входа
    @Secured("ROLE_ANONYMOUS")
    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

}
