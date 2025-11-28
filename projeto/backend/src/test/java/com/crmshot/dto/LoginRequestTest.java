package com.crmshot.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testDefaultConstructor() {
        LoginRequest request = new LoginRequest();

        assertNull(request.getEmail());
        assertNull(request.getSenha());
    }

    @Test
    void testParameterizedConstructor() {
        LoginRequest request = new LoginRequest("test@test.com", "senha123");

        assertEquals("test@test.com", request.getEmail());
        assertEquals("senha123", request.getSenha());
    }

    @Test
    void testGettersAndSetters() {
        LoginRequest request = new LoginRequest();

        request.setEmail("novo@test.com");
        request.setSenha("novaSenha");

        assertEquals("novo@test.com", request.getEmail());
        assertEquals("novaSenha", request.getSenha());
    }
}

