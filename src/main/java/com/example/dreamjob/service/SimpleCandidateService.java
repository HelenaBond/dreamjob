package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.exception.DatabaseUpdateException;
import com.example.dreamjob.exception.EntityNotFoundException;
import com.example.dreamjob.model.Candidate;
import com.example.dreamjob.model.File;
import com.example.dreamjob.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;

@ThreadSafe
@Service
public class SimpleCandidateService implements CandidateService {

    private final CandidateRepository candidateRepository;

    private final FileService fileService;

    public SimpleCandidateService(CandidateRepository sql2oCandidateRepository, FileService fileService) {
        this.candidateRepository = sql2oCandidateRepository;
        this.fileService = fileService;
    }

    @Override
    public Candidate save(Candidate candidate, FileDto image) {
        File file = fileService.save(image);
        candidate.setFileId(file.getId());
        candidate.setCreationDate(LocalDateTime.now());
        return candidateRepository.save(candidate);
    }

    @Override
    public void deleteById(int candidateId) {
        Candidate candidate = findById(candidateId);
        candidateRepository.deleteById(candidateId);
        fileService.deleteById(candidate.getFileId());
    }

    @Override
    public void update(Candidate candidate, FileDto image) {
        Candidate oldCandidate = findById(candidate.getId());
        if (image.getContent().length != 0) {
            var file = fileService.save(image);
            var oldFileId = candidate.getFileId();
            fileService.deleteById(oldFileId);
            candidate.setFileId(file.getId());
        }
        candidate.setCreationDate(oldCandidate.getCreationDate());
        boolean isSaved = candidateRepository.update(candidate);
        if (!isSaved) {
            fileService.deleteById(candidate.getFileId());
            throw new DatabaseUpdateException("Не удалось обновить кандидата");
        }
    }

    @Override
    public Candidate findById(int id) {
        return candidateRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Кандидат с указанным идентификатором не найден"));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }
}
