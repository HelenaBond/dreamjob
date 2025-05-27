package com.example.dreamjob.controller;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.exception.FileLoadException;
import com.example.dreamjob.model.Vacancy;
import com.example.dreamjob.service.CityService;
import com.example.dreamjob.service.VacancyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

@Slf4j
@ThreadSafe
@Controller
@RequestMapping("/vacancies") /* Работать с кандидатами будем по URI /vacancies/** */
public class VacancyController {

    private final VacancyService vacancyService;

    private final CityService cityService;

    public VacancyController(VacancyService vacancyService, CityService cityService) {
        this.vacancyService = vacancyService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file) {
        try {
            FileDto fileDto = new FileDto(file.getOriginalFilename(), file.getBytes());
            vacancyService.save(vacancy, fileDto);
            return "redirect:/vacancies";
        } catch (IOException e) {
            String message = "Ошибка загрузки файла";
            log.error(message, e);
            throw new FileLoadException(message);
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var vacancy = vacancyService.findById(id);
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("vacancy", vacancy);
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file) {
        try {
            vacancyService.update(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/vacancies";
        } catch (IOException e) {
            String message = "Ошибка загрузки файла";
            log.error(message, e);
            throw new FileLoadException(message);
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
            vacancyService.deleteById(id);
            return "redirect:/vacancies";
    }
}
