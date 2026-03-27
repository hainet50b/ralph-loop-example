package com.programacho.ralphloopexample;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUserNotFoundReturns404WithMessage() {
        UserNotFoundException ex = new UserNotFoundException(42L);

        ResponseEntity<Map<String, String>> response = handler.handleUserNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("error", "User not found with id: 42");
    }

    @Test
    void handleUserAlreadyExistsReturns409WithMessage() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("name", "Alice");

        ResponseEntity<Map<String, String>> response = handler.handleUserAlreadyExists(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("error", "User already exists with name: Alice");
    }
}
