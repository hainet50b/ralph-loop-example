package com.programacho.ralphloopexample;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createSavesAndReturnsUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.save(any(User.class))).willReturn(user);

        User result = userService.create(user);

        assertThat(result.getName()).isEqualTo("Alice");
        verify(userRepository).save(user);
    }

    @Test
    void findAllReturnsAllUsers() {
        User alice = new User("Alice", "alice@example.com");
        User bob = new User("Bob", "bob@example.com");
        given(userRepository.findAll()).willReturn(List.of(alice, bob));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findByIdReturnsUserWhenFound() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        Optional<User> result = userService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void updateModifiesAndReturnsUserWhenFound() {
        User existing = new User("Alice", "alice@example.com");
        existing.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User updated = new User("Alice Updated", "alice-new@example.com");
        Optional<User> result = userService.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice Updated");
        assertThat(result.get().getEmail()).isEqualTo("alice-new@example.com");
    }

    @Test
    void updateReturnsEmptyWhenNotFound() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        Optional<User> result = userService.update(99L, new User("X", "x@example.com"));

        assertThat(result).isEmpty();
    }

    @Test
    void deleteReturnsTrueWhenFound() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        boolean result = userService.delete(1L);

        assertThat(result).isTrue();
        verify(userRepository).delete(user);
    }

    @Test
    void deleteReturnsFalseWhenNotFound() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        boolean result = userService.delete(99L);

        assertThat(result).isFalse();
    }
}
