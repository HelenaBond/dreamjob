package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.model.Candidate;
import com.example.dreamjob.model.File;
import com.example.dreamjob.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Optional;

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
    public boolean deleteById(int id) {
        fileService.deleteById(id);
        return candidateRepository.deleteById(id);
    }

    @Override
    public boolean update(Candidate candidate, FileDto image) {
        var isNewFileEmpty = image.getContent().length == 0;
        if (isNewFileEmpty) {
            return candidateRepository.update(candidate);
        }
        /* если передан новый не пустой файл, то старый удаляем, а новый сохраняем */
        File file = fileService.save(image);
        candidate.setFileId(file.getId());
        return candidateRepository.update(candidate);
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }
}
