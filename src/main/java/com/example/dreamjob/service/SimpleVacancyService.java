package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.exception.VacancyNotFoundException;
import com.example.dreamjob.model.Vacancy;
import com.example.dreamjob.repository.VacancyRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

@ThreadSafe
@Service
public class SimpleVacancyService implements VacancyService {

    private final VacancyRepository vacancyRepository;

    private final FileService fileService;

    public SimpleVacancyService(VacancyRepository sql2oVacancyRepository, FileService fileService) {
        this.vacancyRepository = sql2oVacancyRepository;
        this.fileService = fileService;
    }

    @Override
    public Vacancy save(Vacancy vacancy, FileDto image) {
        var file = fileService.save(image);
        vacancy.setFileId(file.getId());
        return vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteById(int vacancyId) {
        Vacancy vacancy = findById(vacancyId);
        fileService.deleteById(vacancy.getFileId());
        vacancyRepository.deleteById(vacancyId);
    }

    @Override
    public void update(Vacancy vacancy, FileDto image) {
        existsById(vacancy.getId()); /* предпроверка */
        if (image.getContent().length != 0) {
            var file = fileService.save(image);
            var oldFileId = vacancy.getFileId();
            fileService.deleteById(oldFileId);
            vacancy.setFileId(file.getId());
        }
        boolean isSaved = vacancyRepository.update(vacancy);
        if (!isSaved) { /* компенсация */
            fileService.deleteById(vacancy.getFileId());
        }
    }

    @Override
    public Vacancy findById(int id) {
        return vacancyRepository
                .findById(id)
                .orElseThrow(() ->
                        new VacancyNotFoundException(
                                "Вакансия с указанным идентификатором не найдена"));
    }

    @Override
    public void existsById(int id) {
        boolean result = vacancyRepository.existsById(id);
        if (!result) {
            throw new VacancyNotFoundException(
                    "Вакансия с указанным идентификатором не найдена");
        }
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }
}
