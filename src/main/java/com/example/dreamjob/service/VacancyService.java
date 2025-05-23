package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.model.Vacancy;

import java.util.Collection;

public interface VacancyService {
    Vacancy save(Vacancy vacancy, FileDto image);

    void deleteById(int vacancyId);

    void update(Vacancy vacancy, FileDto image);

    Vacancy findById(int id);

    void existsById(int id);

    Collection<Vacancy> findAll();
}
