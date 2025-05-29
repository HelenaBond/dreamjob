package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.exception.UpdateException;
import com.example.dreamjob.exception.EntityNotFoundException;
import com.example.dreamjob.model.Vacancy;
import com.example.dreamjob.repository.VacancyRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
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
        vacancy.setCreationDate(LocalDateTime.now());
        return vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteById(int vacancyId) {
        Vacancy vacancy = findById(vacancyId);
        vacancyRepository.deleteById(vacancyId);
        fileService.deleteById(vacancy.getFileId());
    }

    @Override
    public void update(Vacancy vacancy, FileDto image) {
        Vacancy oldVacancy = findById(vacancy.getId());
        if (image.getContent().length != 0) {
            var file = fileService.save(image);
            var oldFileId = vacancy.getFileId();
            fileService.deleteById(oldFileId);
            vacancy.setFileId(file.getId());
        }
        vacancy.setCreationDate(oldVacancy.getCreationDate());
        boolean isSaved = vacancyRepository.update(vacancy);
        if (!isSaved) {
            fileService.deleteById(vacancy.getFileId());
            throw new UpdateException("Не удалось обновить вакансию");
        }
    }

    @Override
    public Vacancy findById(int id) {
        return vacancyRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Вакансия с указанным идентификатором не найдена"));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }
}
