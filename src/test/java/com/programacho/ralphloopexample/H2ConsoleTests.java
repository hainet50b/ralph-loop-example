package com.programacho.ralphloopexample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.h2console.autoconfigure.H2ConsoleProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class H2ConsoleTests {

    @Autowired
    private H2ConsoleProperties h2ConsoleProperties;

    @Test
    void h2ConsoleIsEnabled() {
        assertThat(h2ConsoleProperties.isEnabled()).isTrue();
    }

    @Test
    void h2ConsolePathIsConfigured() {
        assertThat(h2ConsoleProperties.getPath()).isEqualTo("/h2-console");
    }
}
