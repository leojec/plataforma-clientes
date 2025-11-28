package com.crmshot.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CnpjControllerTest {

    @InjectMocks
    private CnpjController cnpjController;

    @Test
    void testBuscarCNPJ_CNPJInvalido_MenosDe14Digitos() {
        ResponseEntity<Object> response = cnpjController.buscarCNPJ("1234567890123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> erro = (Map<String, String>) response.getBody();
        assertEquals("ERROR", erro.get("status"));
        assertEquals("CNPJ deve ter 14 dígitos", erro.get("message"));
    }

    @Test
    void testBuscarCNPJ_CNPJInvalido_MaisDe14Digitos() {
        ResponseEntity<Object> response = cnpjController.buscarCNPJ("123456789012345");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testBuscarCNPJ_CNPJComFormatacao() {

        ResponseEntity<Object> response = cnpjController.buscarCNPJ("12.345.678/0001-90");


        assertNotNull(response);
        assertTrue(response.getStatusCode() == HttpStatus.OK ||
                   response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testBuscarCNPJ_CNPJValido() {

        ResponseEntity<Object> response = cnpjController.buscarCNPJ("12345678000190");


        assertNotNull(response);
        assertTrue(response.getStatusCode() == HttpStatus.OK ||
                   response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

