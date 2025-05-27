package com.example.dreamjob.controller;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.exception.FileLoadException;
import com.example.dreamjob.model.Candidate;
import com.example.dreamjob.service.CandidateService;
import com.example.dreamjob.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/candidates")
@ThreadSafe
public class CandidateController {

    private final CandidateService candidateService;

    private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "candidates/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            candidateService.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/candidates";
        } catch (IOException e) {
            String message = "Ошибка загрузки файла";
            log.error(message, e);
            throw new FileLoadException(message);
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
            var candidate = candidateService.findById(id);
            model.addAttribute("cities", cityService.findAll());
            model.addAttribute("candidate", candidate);
            return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            candidateService.update(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/candidates";
        } catch (IOException e) {
            String message = "Ошибка загрузки файла";
            log.error(message, e);
            throw new FileLoadException(message);
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
            candidateService.deleteById(id);
            return "redirect:/candidates";
    }
}
