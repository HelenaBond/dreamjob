package com.example.dreamjob.repository;

import com.example.dreamjob.model.City;

import java.util.Collection;

public interface CityRepository {
    Collection<City> findAll();
}
