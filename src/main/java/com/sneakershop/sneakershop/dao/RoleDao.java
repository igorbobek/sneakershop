package com.sneakershop.sneakershop.dao;

import com.sneakershop.sneakershop.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RoleDao  extends CrudRepository<Role,Long>{
    Role findByRole(String role); //поиск роли по названию
}
