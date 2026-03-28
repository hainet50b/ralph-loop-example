package com.programacho.ralphloopexample.user;

import java.util.List;
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
    void create_savesAndReturnsUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.save(user)).willReturn(user);

        User result = userService.create(user);

        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        then(userRepository).should().save(user);
    }

    @Test
    void findAll_returnsAllUsers() {
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");
        given(userRepository.findAll()).willReturn(List.of(user1, user2));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_whenExists_returnsUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        Optional<User> result = userService.findById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void update_whenExists_updatesAndReturnsUser() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        User updated = new User("Alice Updated", "alice.updated@example.com");
        User result = userService.update(1L, updated);

        assertThat(result.getName()).isEqualTo("Alice Updated");
        assertThat(result.getEmail()).isEqualTo("alice.updated@example.com");
    }

    @Test
    void update_whenNotExists_throwsException() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        User updated = new User("Alice", "alice@example.com");

        assertThatThrownBy(() -> userService.update(1L, updated))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void delete_whenExists_deletesUser() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));

        userService.delete(1L);

        then(userRepository).should().delete(existing);
    }

    @Test
    void delete_whenNotExists_throwsException() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(1L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
