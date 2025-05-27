package com.example.dreamjob.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileLoadException.class)
    public String fileNotLoad(FileLoadException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StorageException.class)
    public String storageUnavailable(StorageException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String entityNotFound(EntityNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DatabaseUpdateException.class)
    public String databaseNotUpdate(DatabaseUpdateException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String unexpected(Exception ex, Model model) {
        log.error("Необработанное исключение", ex);
        model.addAttribute("message", "Неожиданная ошибка. Обратитесь в техподдержку");
        return "errors/404";
    }
}
