package com.sneakershop.sneakershop.dao;

import com.sneakershop.sneakershop.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserDao extends CrudRepository<User,Long>{
    User findByEmail(String email); //поиск юзера по email
    User findByName(String login); //поиск по имени
}
