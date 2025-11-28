package com.crmshot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @Test
    void testHealth() {
        ResponseEntity<Map<String, Object>> response = healthController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("CRMShot Backend API is running", response.getBody().get("message"));
        assertEquals("0.0.1-SNAPSHOT", response.getBody().get("version"));
    }

    @Test
    void testHealthCheck() {
        ResponseEntity<Map<String, Object>> response = healthController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("CRMShot Backend API is healthy", response.getBody().get("message"));
    }
}

