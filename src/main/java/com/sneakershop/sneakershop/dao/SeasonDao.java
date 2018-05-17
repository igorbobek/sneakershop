package com.sneakershop.sneakershop.dao;

import com.sneakershop.sneakershop.model.Season;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SeasonDao extends CrudRepository<Season, Long> {
    Season findByName(String name); //поиск сезона по названию
}
