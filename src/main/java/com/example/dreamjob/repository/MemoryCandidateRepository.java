package com.example.dreamjob.repository;

import com.example.dreamjob.model.Candidate;
import org.springframework.stereotype.Repository;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public MemoryCandidateRepository() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        save(new Candidate(0, "Петр", "Senior Java Developer", now, 1, 0));
        save(new Candidate(0, "Александр", "Middle+ Java Developer", now, 1, 0));
        save(new Candidate(0, "Елена", "Middle Java Developer", now, 2, 0));
        save(new Candidate(0, "Ольга", "Junior+ Java Developer", now, 2, 0));
        save(new Candidate(0, "Владимир", "Junior Java Developer", now, 3, 0));
        save(new Candidate(0, "Инга", "Intern Java Developer", now, 3, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                        oldCandidate.getCreationDate(),
                        candidate.getCityId(),
                        candidate.getFileId()
                )) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public boolean existsById(int id) {
        return candidates.containsKey(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
