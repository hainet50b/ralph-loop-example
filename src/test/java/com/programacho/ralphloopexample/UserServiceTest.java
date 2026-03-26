package com.programacho.ralphloopexample;

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
    void createUser_savesAndReturnsUser() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.existsByName("Alice")).willReturn(false);
        given(userRepository.existsByEmail("alice@example.com")).willReturn(false);
        given(userRepository.save(user)).willReturn(user);

        User result = userService.createUser(user);

        assertThat(result.getName()).isEqualTo("Alice");
        then(userRepository).should().save(user);
    }

    @Test
    void createUser_throwsWhenNameAlreadyExists() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.existsByName("Alice")).willReturn(true);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists with name: Alice");
    }

    @Test
    void createUser_throwsWhenEmailAlreadyExists() {
        User user = new User("Alice", "alice@example.com");
        given(userRepository.existsByName("Alice")).willReturn(false);
        given(userRepository.existsByEmail("alice@example.com")).willReturn(true);

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists with email: alice@example.com");
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

        User result = userService.findUserById(1L);

        assertThat(result.getName()).isEqualTo("Alice");
    }

    @Test
    void findUserById_throwsWhenNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: 1");
    }

    @Test
    void updateUser_updatesAndReturnsUser() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.existsByName("Bob")).willReturn(false);
        given(userRepository.existsByEmail("bob@example.com")).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateUser(1L, new User("Bob", "bob@example.com"));

        assertThat(updated.getName()).isEqualTo("Bob");
        assertThat(updated.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void updateUser_throwsWhenNameAlreadyTaken() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.existsByName("Bob")).willReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, new User("Bob", "bob@example.com")))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists with name: Bob");
    }

    @Test
    void updateUser_throwsWhenEmailAlreadyTaken() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.existsByName("Bob")).willReturn(false);
        given(userRepository.existsByEmail("bob@example.com")).willReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, new User("Bob", "bob@example.com")))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists with email: bob@example.com");
    }

    @Test
    void updateUser_allowsSameNameAndEmailForSameUser() {
        User existing = new User("Alice", "alice@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existing));
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateUser(1L, new User("Alice", "alice@example.com"));

        assertThat(updated.getName()).isEqualTo("Alice");
        assertThat(updated.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void updateUser_throwsWhenNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(1L, new User("Bob", "bob@example.com")))
                .isInstanceOf(UserNotFoundException.class);
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
                .isInstanceOf(UserNotFoundException.class);
    }
}
