package com.programacho.ralphloopexample.user;

import java.util.List;
import java.util.Optional;

import tools.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void create_returnsCreatedUser() throws Exception {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        given(userService.create(any(User.class))).willReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void findAll_returnsAllUsers() throws Exception {
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");
        given(userService.findAll()).willReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    void findById_whenExists_returnsUser() throws Exception {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        given(userService.findById(1L)).willReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void findById_whenNotExists_returns404() throws Exception {
        given(userService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 1"));
    }

    @Test
    void update_returnsUpdatedUser() throws Exception {
        User updated = new User("Alice Updated", "alice.updated@example.com");
        updated.setId(1L);
        given(userService.update(eq(1L), any(User.class))).willReturn(updated);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void update_whenNotExists_returns404() throws Exception {
        User user = new User("Alice", "alice@example.com");
        given(userService.update(eq(1L), any(User.class)))
                .willThrow(new UserNotFoundException(1L));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 1"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        willDoNothing().given(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotExists_returns404() throws Exception {
        willThrow(new UserNotFoundException(1L)).given(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 1"));
    }
}
