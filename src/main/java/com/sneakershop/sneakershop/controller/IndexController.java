package com.sneakershop.sneakershop.controller;

import com.sneakershop.sneakershop.dao.BrandDao;
import com.sneakershop.sneakershop.dao.SneakerDao;
import com.sneakershop.sneakershop.dao.UserDao;
import com.sneakershop.sneakershop.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {

    @Autowired
    UserDao userDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    SneakerDao sneakerDao;
    //Главная страница
    @GetMapping("/")
    public ModelAndView index(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("sneakers", sneakerDao.findAll());
        modelAndView.addObject("brands", brandDao.findAll());
        modelAndView.addObject("user", userDao.findByName(auth.getName()));
        return modelAndView;
    }
    //Страница с товарами бренда
    @GetMapping("/brand/{brand}")
    public ModelAndView category(@PathVariable(value = "brand") String brand){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView("index");
        if(brandDao.findByName(brand) == null){
            return new ModelAndView("redirect:/");
        }
        modelAndView.addObject("sneakers", sneakerDao.findByBrandId(brandDao.findByName(brand).getId()));
        modelAndView.addObject("brands", brandDao.findAll());
        modelAndView.addObject("user", userDao.findByName(auth.getName()));
        return modelAndView;
    }
    //Страница товара по ID
    @GetMapping("/sneaker/{id}")
    public ModelAndView product(@PathVariable(value = "id") String idStr){
        Long id;
        try {
            id = Long.parseLong(idStr);
        }catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return new ModelAndView("redirect:/");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView("index");

        if(!sneakerDao.findById(id).isPresent()){
            return new ModelAndView("redirect:/");
        }else {
            modelAndView.addObject("sneaker", sneakerDao.findById(id).get());
            modelAndView.addObject("brands", brandDao.findAll());
            modelAndView.addObject("user", userDao.findByName(auth.getName()));
        }
        return modelAndView;
    }

    //Не удалять
    @PostMapping("/")
    public void index(HttpServletResponse response) throws IOException{
        response.sendRedirect("/");
    }

}
