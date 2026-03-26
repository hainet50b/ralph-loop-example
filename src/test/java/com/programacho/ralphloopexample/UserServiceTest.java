package com.programacho.ralphloopexample;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_savesAndReturnsUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.save(user)).willReturn(user);

        User result = userService.createUser(user);

        assertThat(result.getName()).isEqualTo("Alice");
        then(userRepository).should().save(user);
    }

    @Test
    void findAllUsers_returnsList() {
        List<User> users = List.of(new User("Alice", "alice@example.com"));
        given(userRepository.findAll()).willReturn(users);

        List<User> result = userService.findAllUsers();

        assertThat(result).hasSize(1);
    }

    @Test
    void findUserById_returnsUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findUserById_returnsEmptyWhenNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        Optional<User> result = userService.findUserById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void updateUser_updatesAndReturnsUser() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateUser(1L, new User("Bob", "bob@example.com"));

        assertThat(updated.getName()).isEqualTo("Bob");
        assertThat(updated.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void updateUser_throwsWhenNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(1L, new User("Bob", "bob@example.com")))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteUser_deletesExistingUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.deleteUser(1L);

        then(userRepository).should().delete(user);
    }

    @Test
    void deleteUser_throwsWhenNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
