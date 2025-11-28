package com.crmshot.controller;

import com.crmshot.entity.Expositor;
import com.crmshot.entity.Oportunidade;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.OportunidadeRepository;
import com.crmshot.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OportunidadeControllerTest {

    @Mock
    private OportunidadeRepository oportunidadeRepository;

    @Mock
    private ExpositorRepository expositorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OportunidadeController oportunidadeController;

    private Oportunidade oportunidade;
    private Expositor expositor;
    private Usuario vendedor;

    @BeforeEach
    void setUp() {
        expositor = new Expositor();
        expositor.setId(1L);
        expositor.setRazaoSocial("Empresa Teste");
        expositor.setNomeFantasia("Teste");

        vendedor = new Usuario();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");
        vendedor.setEmail("vendedor@teste.com");

        oportunidade = new Oportunidade();
        oportunidade.setId(1L);
        oportunidade.setTitulo("Oportunidade Teste");
        oportunidade.setDescricao("Descrição teste");
        oportunidade.setStatus(Oportunidade.StatusOportunidade.PROSPECCAO);
        oportunidade.setFonte(Oportunidade.FonteOportunidade.INDICACAO);
        oportunidade.setValorEstimado(new BigDecimal("10000.00"));
        oportunidade.setProbabilidadeFechamento(50);
        oportunidade.setDataPrevistaFechamento(LocalDateTime.now().plusMonths(1));
        oportunidade.setExpositor(expositor);
        oportunidade.setVendedor(vendedor);
    }

    @Test
    void testListarOportunidades_Success() {
        List<Oportunidade> oportunidades = Arrays.asList(oportunidade);
        when(oportunidadeRepository.findAll()).thenReturn(oportunidades);

        ResponseEntity<List<Map<String, Object>>> response = oportunidadeController.listarOportunidades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        Map<String, Object> op = response.getBody().get(0);
        assertEquals(1L, op.get("id"));
        assertEquals("Oportunidade Teste", op.get("titulo"));
    }

    @Test
    void testListarOportunidades_ComExpositorEVendedor() {
        List<Oportunidade> oportunidades = Arrays.asList(oportunidade);
        when(oportunidadeRepository.findAll()).thenReturn(oportunidades);

        ResponseEntity<List<Map<String, Object>>> response = oportunidadeController.listarOportunidades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> op = response.getBody().get(0);
        assertTrue(op.containsKey("expositor"));
        assertTrue(op.containsKey("vendedor"));
    }

    @Test
    void testListarOportunidades_SemExpositorEVendedor() {
        oportunidade.setExpositor(null);
        oportunidade.setVendedor(null);
        List<Oportunidade> oportunidades = Arrays.asList(oportunidade);
        when(oportunidadeRepository.findAll()).thenReturn(oportunidades);

        ResponseEntity<List<Map<String, Object>>> response = oportunidadeController.listarOportunidades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> op = response.getBody().get(0);
        assertFalse(op.containsKey("expositor"));
        assertFalse(op.containsKey("vendedor"));
    }

    @Test
    void testListarOportunidades_Exception() {
        when(oportunidadeRepository.findAll()).thenThrow(new RuntimeException("Erro no banco"));

        ResponseEntity<List<Map<String, Object>>> response = oportunidadeController.listarOportunidades();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testBuscarOportunidade_Success() {
        when(oportunidadeRepository.findById(1L)).thenReturn(Optional.of(oportunidade));

        ResponseEntity<Map<String, Object>> response = oportunidadeController.buscarOportunidade(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().get("id"));
    }

    @Test
    void testBuscarOportunidade_NotFound() {
        when(oportunidadeRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = oportunidadeController.buscarOportunidade(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBuscarOportunidade_Exception() {
        when(oportunidadeRepository.findById(1L)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Map<String, Object>> response = oportunidadeController.buscarOportunidade(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testCriarOportunidadesExemplo_Success() {
        List<Expositor> expositores = Arrays.asList(expositor);
        List<Usuario> vendedores = Arrays.asList(vendedor);

        when(expositorRepository.findAll()).thenReturn(expositores);
        when(usuarioRepository.findByAtivoTrue()).thenReturn(vendedores);
        when(oportunidadeRepository.save(any(Oportunidade.class))).thenReturn(oportunidade);

        ResponseEntity<Map<String, Object>> response = oportunidadeController.criarOportunidadesExemplo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("sucesso"));
    }

    @Test
    void testCriarOportunidadesExemplo_SemExpositores() {
        when(expositorRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, Object>> response = oportunidadeController.criarOportunidadesExemplo();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("erro"));
    }
}

