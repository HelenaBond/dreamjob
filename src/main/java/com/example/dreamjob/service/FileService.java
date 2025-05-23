package com.example.dreamjob.service;

import com.example.dreamjob.dto.FileDto;
import com.example.dreamjob.model.File;

import java.util.Optional;

public interface FileService {
    File save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    void deleteById(int id);
}
