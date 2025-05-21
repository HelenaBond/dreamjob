package com.example.dreamjob.service;

import com.example.dreamjob.model.Candidate;
import com.example.dreamjob.repository.CandidateRepository;
import com.example.dreamjob.repository.MemoryCandidateRepository;

import java.util.Collection;
import java.util.Optional;

public class SimpleCandidateService implements CandidateService {

    private static final CandidateService INSTANCE = new SimpleCandidateService();

    private final CandidateRepository repository = MemoryCandidateRepository.getInstance();

    private SimpleCandidateService() {

    }

    public static CandidateService getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return repository.save(candidate);
    }

    @Override
    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return repository.update(candidate);
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return repository.findAll();
    }
}
