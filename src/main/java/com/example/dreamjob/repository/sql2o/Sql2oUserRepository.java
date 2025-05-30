package com.example.dreamjob.repository.sql2o;

import com.example.dreamjob.exception.UpdateException;
import com.example.dreamjob.model.User;
import com.example.dreamjob.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public User save(User user) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO users(email, name, password)
                    VALUES(:email, :name, :password)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("email", user.getEmail())
                    .addParameter("name", user.getName())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return user;
        } catch (Sql2oException e) {
            var message = "Ошибка регистрации";
            log.info(message, e);
            throw new UpdateException(message);
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "SELECT * FROM users WHERE email = :email AND password = :password")
            .addParameter("email", email)
            .addParameter("password", password);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (var connection = sql2o.open()) {
            var sql = "SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)";
            return connection.createQuery(sql)
                    .addParameter("email", email)
                    .executeScalar(Boolean.class);
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            return connection.createQuery(
                    "SELECT * FROM users")
                    .executeAndFetch(User.class);
        }
    }

    @Override
    public void deleteById(int id) {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM users WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }
}
