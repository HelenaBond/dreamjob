package com.example.dreamjob.repository;

import com.example.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

public interface CandidateRepository {
    Candidate save(Candidate candidate);

    boolean deleteById(int id);

    boolean update(Candidate candidate);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();
}
