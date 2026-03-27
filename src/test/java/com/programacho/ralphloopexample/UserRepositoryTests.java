package com.programacho.ralphloopexample;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById() {
        User user = userRepository.save(new User("Alice", "alice@example.com"));

        Optional<User> found = userRepository.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice");
        assertThat(found.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findAll() {
        userRepository.save(new User("Alice", "alice@example.com"));
        userRepository.save(new User("Bob", "bob@example.com"));

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void findByName() {
        userRepository.save(new User("Alice", "alice@example.com"));

        Optional<User> found = userRepository.findByName("Alice");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findByNameNotFound() {
        Optional<User> found = userRepository.findByName("NonExistent");

        assertThat(found).isEmpty();
    }

    @Test
    void findByEmail() {
        userRepository.save(new User("Alice", "alice@example.com"));

        Optional<User> found = userRepository.findByEmail("alice@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    void deleteById() {
        User user = userRepository.save(new User("Alice", "alice@example.com"));

        userRepository.deleteById(user.getId());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
