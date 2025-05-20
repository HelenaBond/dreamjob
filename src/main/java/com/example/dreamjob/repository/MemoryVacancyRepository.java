package com.example.dreamjob.repository;

import com.example.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryVacancyRepository implements VacancyRepository {

    private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        save(new Vacancy(0,
                "Intern Java Developer", "Java Core", now));
        save(new Vacancy(0,
                "Junior Java Developer", "Java Core, SQL", now));
        save(new Vacancy(0,
                "Junior+ Java Developer", "Java Core, SQL, JPA", now));
        save(new Vacancy(0,
                "Middle Java Developer", "Java Core, SQL, Hibernate, Concurrency", now));
        save(new Vacancy(0,
                "Middle+ Java Developer", "Java, SQL, Hibernate, Redis", now));
        save(new Vacancy(0,
                "Senior Java Developer", "Java, SQL, Hibernate, Redis, Kafka", null));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public void deleteById(int id) {
        vacancies.remove(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(
                        vacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        oldVacancy.getCreationDate()
                )) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
