package com.programacho.ralphloopexample;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser() throws Exception {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        given(userService.create(any(User.class))).willReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"alice@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void createUserConflict() throws Exception {
        given(userService.create(any(User.class))).willThrow(new UserAlreadyExistsException("name", "Alice"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"alice@example.com"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already exists with name: Alice"));
    }

    @Test
    void updateUserConflict() throws Exception {
        given(userService.update(eq(1L), any(User.class))).willThrow(new UserAlreadyExistsException("email", "bob@example.com"));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice","email":"bob@example.com"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already exists with email: bob@example.com"));
    }

    @Test
    void findAllUsers() throws Exception {
        User user1 = new User("Alice", "alice@example.com");
        user1.setId(1L);
        User user2 = new User("Bob", "bob@example.com");
        user2.setId(2L);
        given(userService.findAll()).willReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    void findUserById() throws Exception {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        given(userService.findById(1L)).willReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void findUserByIdNotFound() throws Exception {
        given(userService.findById(99L)).willThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 99"));
    }

    @Test
    void updateUser() throws Exception {
        User updated = new User("Alice Updated", "alice-updated@example.com");
        updated.setId(1L);
        given(userService.update(eq(1L), any(User.class))).willReturn(updated);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alice Updated","email":"alice-updated@example.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void updateUserNotFound() throws Exception {
        given(userService.update(eq(99L), any(User.class))).willThrow(new UserNotFoundException(99L));

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Nobody","email":"nobody@example.com"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 99"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserNotFound() throws Exception {
        willThrow(new UserNotFoundException(99L)).given(userService).delete(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 99"));
    }
}
