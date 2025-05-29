package com.example.dreamjob.repository.sql2o;

import com.example.dreamjob.configuration.DatasourceConfiguration;
import com.example.dreamjob.model.Candidate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oCandidateRepositoryTest {
    private static Sql2oCandidateRepository sql2oCandidateRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oCandidateRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sq2o = configuration.databaseClient(datasource);

        sql2oCandidateRepository = new Sql2oCandidateRepository(sq2o);
    }

    @AfterEach
    public void clearCandidates() {
        var candidates = sql2oCandidateRepository.findAll();
        for (var candidate : candidates) {
            sql2oCandidateRepository.deleteById(candidate.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "name", "description", creationDate, 1, 1));
        var savedCandidate = sql2oCandidateRepository.findById(candidate.getId()).get();
        assertThat(candidate).isEqualTo(savedCandidate);
    }

    @Test
    public void whenSaveSerialThenGetAll() {
        var creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        var candidate1 = sql2oCandidateRepository.save(new Candidate(0, "name1", "description1", creationDate, 1, 1));
        var candidate2 = sql2oCandidateRepository.save(new Candidate(0, "name2", "description2", creationDate, 1, 1));
        var candidate3 = sql2oCandidateRepository.save(new Candidate(0, "name3", "description3", creationDate, 1, 1));
        var result = sql2oCandidateRepository.findAll();
        assertThat(result).isEqualTo(List.of(candidate1, candidate2, candidate3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oCandidateRepository.findAll()).isEqualTo(Collections.emptyList());
        assertThat(sql2oCandidateRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenDeleteThenEmptyOptional() {
        var creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "name", "description", creationDate, 1, 1));
        sql2oCandidateRepository.deleteById(candidate.getId());
        assertThat(sql2oCandidateRepository.findById(candidate.getId())).isEqualTo(Optional.empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "name", "description", creationDate, 1, 1));
        var updatedCandidate = new Candidate(candidate.getId(), "new name", "new description", creationDate, 1, 1);
        assertThat(sql2oCandidateRepository.update(updatedCandidate)).isTrue();
        assertThat(sql2oCandidateRepository.findById(candidate.getId()).get()).usingRecursiveComparison().isEqualTo(updatedCandidate);
    }

    @Test
    public void whenUpdateUnExistingCandidateThenGetFalse() {
        var creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = new Candidate(0, "name", "description", creationDate, 1, 1);
        var isUpdated = sql2oCandidateRepository.update(candidate);
        assertThat(isUpdated).isFalse();
    }
}
