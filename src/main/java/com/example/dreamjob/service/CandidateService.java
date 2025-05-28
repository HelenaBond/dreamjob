package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.model.Candidate;

import java.util.Collection;

public interface CandidateService {
    Candidate save(Candidate candidate, FileDto image);

    void deleteById(int candidateId);

    void update(Candidate candidate, FileDto image);

    Candidate findById(int id);

    Collection<Candidate> findAll();
}
