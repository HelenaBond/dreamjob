package com.example.dreamjob.repository.sql2o;

import com.example.dreamjob.configuration.DatasourceConfiguration;
import com.example.dreamjob.model.Vacancy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oVacancyRepositoryTest {
    private static Sql2oVacancyRepository sql2oVacancyRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oVacancyRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oVacancyRepository = new Sql2oVacancyRepository(sql2o);
    }

    @AfterEach
    public void clearVacancies() {
        var vacancies = sql2oVacancyRepository.findAll();
        for (var vacancy : vacancies) {
            sql2oVacancyRepository.deleteById(vacancy.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, 1));
        var savedVacancy = sql2oVacancyRepository.findById(vacancy.getId()).get();
        assertThat(savedVacancy).usingRecursiveComparison().isEqualTo(vacancy);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy1 = sql2oVacancyRepository.save(new Vacancy(0, "title1", "description1", creationDate, true, 1, 1));
        var vacancy2 = sql2oVacancyRepository.save(new Vacancy(0, "title2", "description2", creationDate, false, 1, 1));
        var vacancy3 = sql2oVacancyRepository.save(new Vacancy(0, "title3", "description3", creationDate, true, 1, 1));
        var result = sql2oVacancyRepository.findAll();
        assertThat(result).isEqualTo(List.of(vacancy1, vacancy2, vacancy3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oVacancyRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oVacancyRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, 1));
        sql2oVacancyRepository.deleteById(vacancy.getId());
        var savedVacancy = sql2oVacancyRepository.findById(vacancy.getId());
        assertThat(savedVacancy).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, 1));
        var updatedVacancy = new Vacancy(
                vacancy.getId(), "new title", "new description", creationDate.plusDays(1),
                !vacancy.getVisible(), 1, 1
        );
        var isUpdated = sql2oVacancyRepository.update(updatedVacancy);
        var savedVacancy = sql2oVacancyRepository.findById(updatedVacancy.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedVacancy).usingRecursiveComparison().isEqualTo(updatedVacancy);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = new Vacancy(0, "title", "description", creationDate, true, 1, 1);
        var isUpdated = sql2oVacancyRepository.update(vacancy);
        assertThat(isUpdated).isFalse();
    }
}
