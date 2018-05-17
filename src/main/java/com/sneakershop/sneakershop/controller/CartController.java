package com.sneakershop.sneakershop.controller;

import com.sneakershop.sneakershop.dao.BrandDao;
import com.sneakershop.sneakershop.dao.SneakerDao;
import com.sneakershop.sneakershop.dao.UserDao;
import com.sneakershop.sneakershop.model.Sneaker;
import com.sneakershop.sneakershop.model.User;
import com.sneakershop.sneakershop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Secured("ROLE_USER")
public class CartController {

    @Autowired
    UserService userService;
    @Autowired
    SneakerDao sneakerDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    UserDao userDao;
    //Страница корзины
    @GetMapping("/cart")
    public ModelAndView cart(ModelAndView modelAndView){
        modelAndView.setViewName("index");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByName(auth.getName()); //получаем юзера который сечас залогинен
        modelAndView.addObject("sneakers",user.getSneakers()); //добавляем в html все его товары
        modelAndView.addObject("brands",brandDao.findAll());
        modelAndView.addObject("cart", true);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    //Удаление товара из корзины
    @PostMapping("/deleteItem")
    public ModelAndView delete(@RequestParam("brandId") String idStr){
        long id;
        try{
            id = Long.parseLong(idStr);
        }catch (NumberFormatException e){
            System.err.println(e.getMessage());
            return new ModelAndView("redirect:/");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(sneakerDao.findById(id).isPresent()){  //удаляем товар юзера по id(удаление из cart)
            if(userDao.findByName(auth.getName()).getSneakers().contains(sneakerDao.findById(id).get())){
                User user = userDao.findByName(auth.getName());
                Sneaker sneaker= sneakerDao.findById(id).get();
                sneaker.getBuyers().remove(user);
                sneakerDao.save(sneaker);
            }
        }
        return new ModelAndView("redirect:/cart");
    }

    //Добавление товара в корзину
    @PostMapping("/addToCart")
    public ModelAndView addToCart(@RequestParam("productId") String idStr){
        long id;
        try{
            id = Long.parseLong(idStr);
        }catch (NumberFormatException e){
            System.err.println(e.getMessage());
            return new ModelAndView("redirect:/");
        }
        if(sneakerDao.findById(id).isPresent()){ //если товар существует то добавляем в корзину
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByName(auth.getName());
            Sneaker sneaker= sneakerDao.findById(id).get();
            sneaker.getBuyers().add(user);
            sneakerDao.save(sneaker);
        }
        return new ModelAndView("redirect:/cart");
    }

}
