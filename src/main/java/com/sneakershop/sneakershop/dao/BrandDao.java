package com.sneakershop.sneakershop.dao;

import com.sneakershop.sneakershop.model.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BrandDao extends CrudRepository<Brand,Long>{
    Brand findByName(String name); //поиск в бд бренда по названию
}
