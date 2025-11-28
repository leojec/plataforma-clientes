package com.crmshot.controller;

import com.crmshot.dto.ExpositorRequest;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Usuario;
import com.crmshot.service.ExpositorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpositorControllerTest {

    @Mock
    private ExpositorService expositorService;

    @InjectMocks
    private ExpositorController expositorController;

    private Expositor expositor;
    private ExpositorRequest request;
    private Usuario vendedor;

    @BeforeEach
    void setUp() {
        vendedor = new Usuario();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");
        vendedor.setEmail("vendedor@teste.com");

        expositor = new Expositor();
        expositor.setId(1L);
        expositor.setRazaoSocial("Empresa Teste LTDA");
        expositor.setNomeFantasia("Empresa Teste");
        expositor.setCnpj("12345678000190");
        expositor.setEmail("empresa@teste.com");
        expositor.setStatus(Expositor.StatusExpositor.ATIVO);
        expositor.setVendedor(vendedor);

        request = new ExpositorRequest();
        request.setRazaoSocial("Empresa Teste LTDA");
        request.setNomeFantasia("Empresa Teste");
        request.setCnpj("12345678000190");
        request.setEmail("empresa@teste.com");
    }

    @Test
    void testCriarExpositor_Success() {
        when(expositorService.existeCnpj("12345678000190")).thenReturn(false);
        when(expositorService.criarExpositor(any(ExpositorRequest.class))).thenReturn(expositor);

        ResponseEntity<?> response = expositorController.criarExpositor(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(expositorService, times(1)).existeCnpj("12345678000190");
        verify(expositorService, times(1)).criarExpositor(any(ExpositorRequest.class));
    }

    @Test
    void testCriarExpositor_CnpjJaExiste() {
        when(expositorService.existeCnpj("12345678000190")).thenReturn(true);

        ResponseEntity<?> response = expositorController.criarExpositor(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CNPJ já está cadastrado", response.getBody());
        verify(expositorService, times(1)).existeCnpj("12345678000190");
        verify(expositorService, never()).criarExpositor(any(ExpositorRequest.class));
    }

    @Test
    void testCriarExpositor_Exception() {
        when(expositorService.existeCnpj("12345678000190")).thenReturn(false);
        when(expositorService.criarExpositor(any(ExpositorRequest.class)))
                .thenThrow(new RuntimeException("Erro ao salvar"));

        ResponseEntity<?> response = expositorController.criarExpositor(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao criar expositor"));
    }

    @Test
    void testListarExpositores_Success() {
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorService.listarExpositores()).thenReturn(expositores);

        ResponseEntity<List<Map<String, Object>>> response = expositorController.listarExpositores();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(expositorService, times(1)).listarExpositores();
    }

    @Test
    void testListarExpositores_Exception() {
        when(expositorService.listarExpositores()).thenThrow(new RuntimeException("Erro no banco"));

        ResponseEntity<List<Map<String, Object>>> response = expositorController.listarExpositores();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testListarExpositoresPaginado() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Expositor> page = new PageImpl<>(Arrays.asList(expositor), pageable, 1);
        when(expositorService.buscarExpositores(null, null, null, pageable)).thenReturn(page);

        ResponseEntity<Page<Expositor>> response = expositorController.listarExpositoresPaginado(null, null, null, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void testBuscarExpositor_Success() {
        when(expositorService.buscarPorId(1L)).thenReturn(Optional.of(expositor));

        ResponseEntity<?> response = expositorController.buscarExpositor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(expositorService, times(1)).buscarPorId(1L);
    }

    @Test
    void testBuscarExpositor_NotFound() {
        when(expositorService.buscarPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = expositorController.buscarExpositor(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAtualizarExpositor_Success() {
        when(expositorService.atualizarExpositor(1L, request)).thenReturn(expositor);

        ResponseEntity<?> response = expositorController.atualizarExpositor(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(expositorService, times(1)).atualizarExpositor(1L, request);
    }

    @Test
    void testAtualizarExpositor_NotFound() {
        when(expositorService.atualizarExpositor(999L, request))
                .thenThrow(new RuntimeException("Expositor não encontrado"));

        ResponseEntity<?> response = expositorController.atualizarExpositor(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testExcluirExpositor_Success() {
        doNothing().when(expositorService).excluirExpositor(1L);

        ResponseEntity<?> response = expositorController.excluirExpositor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Expositor excluído com sucesso", response.getBody());
        verify(expositorService, times(1)).excluirExpositor(1L);
    }

    @Test
    void testListarExpositoresPorStatus() {
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorService.listarExpositoresPorStatus(Expositor.StatusExpositor.ATIVO)).thenReturn(expositores);

        ResponseEntity<List<Expositor>> response = expositorController.listarExpositoresPorStatus(Expositor.StatusExpositor.ATIVO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testListarExpositoresPorVendedor() {
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorService.listarExpositoresPorVendedor(1L)).thenReturn(expositores);

        ResponseEntity<List<Expositor>> response = expositorController.listarExpositoresPorVendedor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAtualizarStatus_Success() {
        when(expositorService.buscarPorId(1L)).thenReturn(Optional.of(expositor));
        when(expositorService.atualizarExpositor(any(Expositor.class))).thenReturn(expositor);

        Map<String, String> statusData = new java.util.HashMap<>();
        statusData.put("status", "Em Andamento");

        ResponseEntity<Map<String, Object>> response = expositorController.atualizarStatus(1L, statusData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(expositorService, times(1)).buscarPorId(1L);
        verify(expositorService, times(1)).atualizarExpositor(any(Expositor.class));
    }

    @Test
    void testAtualizarStatus_StatusVazio() {
        Map<String, String> statusData = new java.util.HashMap<>();
        statusData.put("status", "");

        ResponseEntity<Map<String, Object>> response = expositorController.atualizarStatus(1L, statusData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testAtualizarStatus_ExpositorNaoEncontrado() {
        when(expositorService.buscarPorId(999L)).thenReturn(Optional.empty());

        Map<String, String> statusData = new java.util.HashMap<>();
        statusData.put("status", "Em Andamento");

        ResponseEntity<Map<String, Object>> response = expositorController.atualizarStatus(999L, statusData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAtualizarStatus_DiferentesStatus() {
        when(expositorService.buscarPorId(1L)).thenReturn(Optional.of(expositor));
        when(expositorService.atualizarExpositor(any(Expositor.class))).thenReturn(expositor);

        String[] statuses = {"Lead", "Em Andamento", "Em Negociação", "Stand Fechado"};
        
        for (String status : statuses) {
            Map<String, String> statusData = new java.util.HashMap<>();
            statusData.put("status", status);

            ResponseEntity<Map<String, Object>> response = expositorController.atualizarStatus(1L, statusData);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void testAtualizarStatus_StatusInvalido() {
        when(expositorService.buscarPorId(1L)).thenReturn(Optional.of(expositor));

        Map<String, String> statusData = new java.util.HashMap<>();
        statusData.put("status", "Status Invalido");

        ResponseEntity<Map<String, Object>> response = expositorController.atualizarStatus(1L, statusData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAtualizarStatus_Exception() {
        when(expositorService.buscarPorId(1L)).thenReturn(Optional.of(expositor));
        when(expositorService.atualizarExpositor(any(Expositor.class))).thenThrow(new RuntimeException("Erro"));

        Map<String, String> statusData = new java.util.HashMap<>();
        statusData.put("status", "Em Andamento");

        ResponseEntity<Map<String, Object>> response = expositorController.atualizarStatus(1L, statusData);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testListarExpositores_ComVendedor() {
        expositor.setVendedor(vendedor);
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorService.listarExpositores()).thenReturn(expositores);

        ResponseEntity<List<Map<String, Object>>> response = expositorController.listarExpositores();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get(0).containsKey("vendedor"));
    }

    @Test
    void testListarExpositores_SemVendedor() {
        expositor.setVendedor(null);
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorService.listarExpositores()).thenReturn(expositores);

        ResponseEntity<List<Map<String, Object>>> response = expositorController.listarExpositores();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().get(0).containsKey("vendedor"));
    }

    @Test
    void testBuscarExpositor_ComVendedor() {
        expositor.setVendedor(vendedor);
        when(expositorService.buscarPorId(1L)).thenReturn(Optional.of(expositor));

        ResponseEntity<?> response = expositorController.buscarExpositor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testBuscarExpositor_Exception() {
        when(expositorService.buscarPorId(1L)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<?> response = expositorController.buscarExpositor(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAtualizarExpositor_Exception() {
        when(expositorService.atualizarExpositor(1L, request))
                .thenThrow(new RuntimeException("Erro ao atualizar"));

        ResponseEntity<?> response = expositorController.atualizarExpositor(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testExcluirExpositor_Exception() {
        doThrow(new RuntimeException("Erro")).when(expositorService).excluirExpositor(1L);

        ResponseEntity<?> response = expositorController.excluirExpositor(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}



