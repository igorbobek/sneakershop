package com.sneakershop.sneakershop.dao;

import com.sneakershop.sneakershop.model.Sneaker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface SneakerDao extends CrudRepository<Sneaker, Long> {
    Set<Sneaker> findByBrandId(Long id); //поиск товара по ID
}
