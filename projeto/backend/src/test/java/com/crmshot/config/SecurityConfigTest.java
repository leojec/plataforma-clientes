package com.crmshot.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void testPasswordEncoder() {
        SecurityConfig config = new SecurityConfig();

        PasswordEncoder passwordEncoder = config.passwordEncoder();
        assertNotNull(passwordEncoder);


        String encoded = passwordEncoder.encode("teste123");
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches("teste123", encoded));
    }
}

