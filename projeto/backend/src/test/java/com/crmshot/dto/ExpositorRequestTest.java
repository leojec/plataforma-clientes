package com.crmshot.dto;

import com.crmshot.entity.Expositor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpositorRequestTest {

    @Test
    void testDefaultConstructor() {
        ExpositorRequest request = new ExpositorRequest();

        assertNull(request.getRazaoSocial());
        assertNull(request.getNomeFantasia());
        assertNull(request.getCnpj());
        assertEquals(Expositor.StatusExpositor.ATIVO, request.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        ExpositorRequest request = new ExpositorRequest();

        request.setRazaoSocial("Empresa Teste LTDA");
        request.setNomeFantasia("Teste");
        request.setCnpj("12.345.678/0001-90");
        request.setEmail("teste@teste.com");
        request.setTelefone("(11) 1234-5678");
        request.setCelular("(11) 98765-4321");
        request.setEndereco("Rua Teste, 123");
        request.setCidade("São Paulo");
        request.setEstado("SP");
        request.setCep("01234-567");
        request.setSite("https://teste.com");
        request.setDescricao("Descrição teste");
        request.setStatus(Expositor.StatusExpositor.POTENCIAL);
        request.setVendedorId(1L);

        assertEquals("Empresa Teste LTDA", request.getRazaoSocial());
        assertEquals("Teste", request.getNomeFantasia());
        assertEquals("12.345.678/0001-90", request.getCnpj());
        assertEquals("teste@teste.com", request.getEmail());
        assertEquals("(11) 1234-5678", request.getTelefone());
        assertEquals("(11) 98765-4321", request.getCelular());
        assertEquals("Rua Teste, 123", request.getEndereco());
        assertEquals("São Paulo", request.getCidade());
        assertEquals("SP", request.getEstado());
        assertEquals("01234-567", request.getCep());
        assertEquals("https://teste.com", request.getSite());
        assertEquals("Descrição teste", request.getDescricao());
        assertEquals(Expositor.StatusExpositor.POTENCIAL, request.getStatus());
        assertEquals(1L, request.getVendedorId());
    }
}

