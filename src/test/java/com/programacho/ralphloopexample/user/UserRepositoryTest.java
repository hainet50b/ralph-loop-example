package com.programacho.ralphloopexample.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_persistsUser() {
        User user = new User("Alice", "alice@example.com");

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alice");
        assertThat(saved.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findById_returnsUser() {
        User saved = userRepository.save(new User("Bob", "bob@example.com"));

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bob");
    }

    @Test
    void findById_returnsEmptyWhenNotFound() {
        Optional<User> found = userRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void findAll_returnsAllUsers() {
        userRepository.save(new User("Alice", "alice@example.com"));
        userRepository.save(new User("Bob", "bob@example.com"));

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void deleteById_removesUser() {
        User saved = userRepository.save(new User("Alice", "alice@example.com"));

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}
