package com.example.dreamjob.service;

import com.example.dreamjob.model.City;
import com.example.dreamjob.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SimpleCityService implements CityService {

    private final CityRepository cityRepository;

    public SimpleCityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Collection<City> findAll() {
        return cityRepository.findAll();
    }
}
