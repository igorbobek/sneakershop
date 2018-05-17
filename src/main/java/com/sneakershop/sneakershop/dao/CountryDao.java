package com.sneakershop.sneakershop.dao;

import com.sneakershop.sneakershop.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CountryDao extends CrudRepository<Country,Long>{
    Country findByName(String name); //поиск страны по имени
}
