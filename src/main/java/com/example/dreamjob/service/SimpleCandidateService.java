package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.exception.CandidateNotFoundException;
import com.example.dreamjob.model.Candidate;
import com.example.dreamjob.model.File;
import com.example.dreamjob.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

@ThreadSafe
@Service
public class SimpleCandidateService implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final FileService fileService;

    public SimpleCandidateService(CandidateRepository candidateRepository, FileService fileService) {
        this.candidateRepository = candidateRepository;
        this.fileService = fileService;
    }

    @Override
    public Candidate save(Candidate candidate, FileDto image) {
        File file = fileService.save(image);
        candidate.setFileId(file.getId());
        return candidateRepository.save(candidate);
    }

    @Override
    public void deleteById(int candidateId) {
        Candidate candidate = findById(candidateId);
        fileService.deleteById(candidate.getFileId());
        candidateRepository.deleteById(candidateId);
    }

    @Override
    public void update(Candidate candidate, FileDto image) {
        existsById(candidate.getId());  /* предпроверка */
        if (image.getContent().length != 0) {
            var file = fileService.save(image);
            var oldFileId = candidate.getFileId();
            fileService.deleteById(oldFileId);
            candidate.setFileId(file.getId());
        }
        boolean isSaved = candidateRepository.update(candidate);
        if (!isSaved) { /* компенсация */
            fileService.deleteById(candidate.getFileId());
        }
    }

    @Override
    public Candidate findById(int id) {
        return candidateRepository
                .findById(id)
                .orElseThrow(
                        () -> new CandidateNotFoundException(
                                "Кандидат с указанным идентификатором не найден"));
    }

    @Override
    public void existsById(int id) {
        boolean result = candidateRepository.existsById(id);
        if (!result) {
            throw new CandidateNotFoundException(
                    "Кандидат с указанным идентификатором не найден");
        }
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }
}
