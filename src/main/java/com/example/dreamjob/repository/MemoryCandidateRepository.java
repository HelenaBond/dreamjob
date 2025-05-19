package com.example.dreamjob.repository;

import com.example.dreamjob.model.Candidate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private static final CandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Петр", "Senior Java Developer", null));
        save(new Candidate(0, "Александр", "Middle+ Java Developer", null));
        save(new Candidate(0, "Елена", "Middle Java Developer", null));
        save(new Candidate(0, "Ольга", "Junior+ Java Developer", null));
        save(new Candidate(0, "Владимир", "Junior Java Developer", null));
        save(new Candidate(0, "Инга", "Intern Java Developer", null));
    }

    public static CandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidate.setCreationDate(LocalDateTime.now());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public void deleteById(int id) {
        candidates.remove(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(
                        candidate.getId(),
                        candidate.getName(),
                        candidate.getDescription(),
                        oldCandidate.getCreationDate()
                )) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
