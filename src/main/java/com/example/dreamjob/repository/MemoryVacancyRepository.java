package com.example.dreamjob.repository;

import com.example.dreamjob.model.Vacancy;
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
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    public MemoryVacancyRepository() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        save(new Vacancy(0,
                "Intern Java Developer", "Java Core", now, false, 1, 0));
        save(new Vacancy(0,
                "Junior Java Developer", "Java Core, SQL", now, false, 1, 0));
        save(new Vacancy(0,
                "Junior+ Java Developer", "Java Core, SQL, JPA", now, false, 2, 0));
        save(new Vacancy(0,
                "Middle Java Developer", "Java Core, SQL, Hibernate, Concurrency", now, false, 2, 0));
        save(new Vacancy(0,
                "Middle+ Java Developer", "Java, SQL, Hibernate, Redis", now, false, 3, 0));
        save(new Vacancy(0,
                "Senior Java Developer", "Java, SQL, Hibernate, Redis, Kafka", now, false, 3, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(
                        oldVacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        oldVacancy.getCreationDate(),
                        vacancy.getVisible(),
                        vacancy.getCityId(),
                        vacancy.getFileId()
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
