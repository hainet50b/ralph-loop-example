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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_returns201WithLocationHeader() throws Exception {
        User created = new User("Alice", "alice@example.com");
        created.setId(1L);
        given(userService.createUser(any(User.class))).willReturn(created);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"email\":\"alice@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void getAllUsers_returns200WithList() throws Exception {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        given(userService.findAllUsers()).willReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void getUserById_returns200WhenFound() throws Exception {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        given(userService.findUserById(1L)).willReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getUserById_returns404WhenNotFound() throws Exception {
        given(userService.findUserById(999L)).willThrow(new UserNotFoundException(999L));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 999"));
    }

    @Test
    void updateUser_returns404WhenNotFound() throws Exception {
        given(userService.updateUser(eq(999L), any(User.class)))
                .willThrow(new UserNotFoundException(999L));

        mockMvc.perform(put("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\",\"email\":\"bob@example.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 999"));
    }

    @Test
    void deleteUser_returns404WhenNotFound() throws Exception {
        willThrow(new UserNotFoundException(999L)).given(userService).deleteUser(999L);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 999"));
    }

    @Test
    void createUser_returns409WhenNameAlreadyExists() throws Exception {
        given(userService.createUser(any(User.class)))
                .willThrow(new UserAlreadyExistsException("name", "Alice"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"email\":\"alice@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already exists with name: Alice"));
    }

    @Test
    void updateUser_returns409WhenEmailAlreadyTaken() throws Exception {
        given(userService.updateUser(eq(1L), any(User.class)))
                .willThrow(new UserAlreadyExistsException("email", "bob@example.com"));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\",\"email\":\"bob@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already exists with email: bob@example.com"));
    }

    @Test
    void updateUser_returns200() throws Exception {
        User updated = new User("Bob", "bob@example.com");
        updated.setId(1L);
        given(userService.updateUser(eq(1L), any(User.class))).willReturn(updated);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\",\"email\":\"bob@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void deleteUser_returns204() throws Exception {
        willDoNothing().given(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
