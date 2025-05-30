package com.example.dreamjob.repository.sql2o;

import com.example.dreamjob.configuration.DatasourceConfiguration;
import com.example.dreamjob.exception.UpdateException;
import com.example.dreamjob.model.User;
import com.example.dreamjob.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Sql2oUserRepositoryTest {

    private static UserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepository() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var connectionPool = configuration.connectionPool(url, username, password);
        var databaseClient = configuration.databaseClient(connectionPool);

        sql2oUserRepository = new Sql2oUserRepository(databaseClient);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    void whenSaveThenGetSame() {
        var user = new User(0, "email", "name", "password");
        var savedUser = sql2oUserRepository.save(user);
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveSameUserThenException() {
        var user = new User(0, "email", "name", "password");
        sql2oUserRepository.save(user);
        Exception exception = assertThrows(UpdateException.class,
                () -> sql2oUserRepository.save(user));
        assertEquals("Ошибка регистрации", exception.getMessage());
    }

    @Test
    public void existsByEmail() {
        var user = new User(0, "email", "name", "password");
        sql2oUserRepository.save(user);
        assertThat(sql2oUserRepository.existsByEmail(user.getEmail())).isTrue();
    }

    @Test
    public void whenUserNotExistsByEmail() {
        assertThat(sql2oUserRepository.existsByEmail("email")).isFalse();
    }

    @Test
    public void findByEmailAndPassword() {
        var user = new User(0, "email", "name", "password");
        var savedUser = sql2oUserRepository.save(user);
        var findUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(findUser.get());
    }

    @Test
    public void whenFindByWrongEmailAndPasswordThenNull() {
        Optional<User> result = sql2oUserRepository.findByEmailAndPassword("email", "password");
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void whenSaveSerialThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(0, "email1", "name", "password"));
        var user2 = sql2oUserRepository.save(new User(0, "email2", "name", "password"));
        var user3 = sql2oUserRepository.save(new User(0, "email3", "name", "password"));
        var users = sql2oUserRepository.findAll();
        assertThat(users).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    public void whenDeleteThenFindEmptyOptional() {
        var user = new User(0, "email", "name", "password");
        var savedUser = sql2oUserRepository.save(user);
        sql2oUserRepository.deleteById(savedUser.getId());
        assertThat(sql2oUserRepository.findById(savedUser.getId())).isEqualTo(Optional.empty());
    }
}
