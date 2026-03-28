package com.programacho.ralphloopexample.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    @Test
    void constructorSetsNameAndEmail() {
        User user = new User("Alice", "alice@example.com");

        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
        assertNull(user.getId());
    }

    @Test
    void settersUpdateFields() {
        User user = new User("Alice", "alice@example.com");

        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob@example.com");

        assertEquals(1L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
    }
}
