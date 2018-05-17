package com.sneakershop.sneakershop.controller;


import com.sneakershop.sneakershop.dao.*;
import com.sneakershop.sneakershop.model.Brand;
import com.sneakershop.sneakershop.model.Season;
import com.sneakershop.sneakershop.model.Sneaker;
import com.sneakershop.sneakershop.service.UserService;
import com.sneakershop.sneakershop.util.XmlUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    UserService userService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    SneakerDao sneakerDao;
    @Autowired
    CountryDao countryDao;
    @Autowired
    SeasonDao seasonDao;

    //Страница Добавление товара
    @GetMapping("/addSneaker")
    public ModelAndView addProduct(ModelAndView modelAndView){
        modelAndView.setViewName("add_sneaker"); //передаем html
        modelAndView.addObject("sneaker", new Sneaker()); //добавляем продукт в html
        modelAndView.addObject("user", userService.findByName("admin")); // добавляем юзера в html
        return modelAndView; //возвращаем клиенту html где будет юзер и товар
    }
    //Страница Добавление бренда
    @GetMapping("/addBrand")
    public ModelAndView addBrand(ModelAndView modelAndView){
        modelAndView.setViewName("add_brand");
        modelAndView.addObject("brand", new Brand());
        modelAndView.addObject("user", userService.findByName("admin"));
        return modelAndView;
    }

    //Добавление бренда
    @PostMapping("/addBrand")
    public void addBrand(@ModelAttribute("brand") @Valid Brand brand,HttpServletResponse response) throws IOException{
        if(brandDao.findByName(brand.getName()) == null){
            brandDao.save(brand);
        }                           //если бренд существует то не создаем
        response.sendRedirect("/");
    }
    //Добавление товара
    @PostMapping("/addSneaker")
    public void addProduct(@ModelAttribute("sneaker") @Valid Sneaker sneaker, HttpServletResponse response) throws IOException{

        if(brandDao.findByName(sneaker.getBrand().getName()) == null){
            response.sendRedirect("/");
            return;   //если бренд не существует то не создаем и выходим
        }else{
            sneaker.setBrand(brandDao.findByName(sneaker.getBrand().getName())); //если существует то устанваливаем продукту данный бренд
        }

        if(countryDao.findByName(sneaker.getCountry().getName()) != null){
            sneaker.setCountry(countryDao.findByName(sneaker.getCountry().getName())); //также как и с брендом
        }

        if(seasonDao.findByName(sneaker.getSeason().getName()) != null){
            sneaker.setSeason(seasonDao.findByName(sneaker.getSeason().getName()));//также как и с брендом
        }

        sneakerDao.save(sneaker); //сохраняем товар в бд
        response.sendRedirect("/");
    }
    //Удаление товара
    @PostMapping("/deleteSneaker")
    public  String deletePlayer(@RequestParam("sneakerId") String idStr, HttpServletRequest request){
        String referer = request.getHeader("Referer");
        long id;

        try{
            id = Long.parseLong(idStr);
        }catch (NumberFormatException e){
            System.err.println(e.getMessage());
            return "redirect:"+referer;
        }

        if(sneakerDao.findById(id).isPresent()){  //ищем товар по id и удаляем
            Sneaker sneaker= sneakerDao.findById(id).get();
            sneaker.setBrand(null);
            sneaker.setCountry(null);
            sneaker.setSeason(null);
            sneaker.setBuyers(null);
            sneakerDao.save(sneaker);
            sneakerDao.delete(sneakerDao.findById(id).get());
        }
        return "redirect:"+referer;
    }

    //Получение xml файла товаров
    @GetMapping("/getXml")
    public void getXml(ModelMap map, HttpServletResponse response) throws IOException {
        String xml = XmlUtil.xmlString(sneakerDao.findAll());
        byte[] documentBody = xml.getBytes();
        InputStream is = new ByteArrayInputStream(documentBody);
        IOUtils.copy(is, response.getOutputStream());
        response.setContentType("text/xml");
        response.flushBuffer();
    }

}
