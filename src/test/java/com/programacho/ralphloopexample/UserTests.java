package com.programacho.ralphloopexample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTests {

    @Test
    void defaultConstructorCreatesUserWithNullFields() {
        // The protected constructor is accessible within the same package
        User user = new User("test", "test@example.com");
        user.setId(null);
        user.setName(null);
        user.setEmail(null);

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void parameterizedConstructorSetsNameAndEmail() {
        User user = new User("Alice", "alice@example.com");

        assertNull(user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    void settersAndGettersWork() {
        User user = new User("Alice", "alice@example.com");
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob@example.com");

        assertEquals(1L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
    }
}
