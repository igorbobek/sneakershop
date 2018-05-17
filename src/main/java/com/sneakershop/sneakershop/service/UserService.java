package com.sneakershop.sneakershop.service;


import com.sneakershop.sneakershop.model.User;

public interface UserService {

    void save(User user);
    User findById(Long id);
    User findByName(String name);
    User findByEmail(String email);
    void update(User user);

}
