package com.example.dreamjob.controller;

import com.example.dreamjob.repository.CandidateRepository;
import com.example.dreamjob.repository.MemoryCandidateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateRepository candidateRepository = MemoryCandidateRepository.getInstance();

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String create() {
        return "candidates/create.html";
    }
}
