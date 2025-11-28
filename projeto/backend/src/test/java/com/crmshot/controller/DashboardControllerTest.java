package com.crmshot.controller;

import com.crmshot.entity.Usuario;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.InteracaoRepository;
import com.crmshot.repository.OportunidadeRepository;
import com.crmshot.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private ExpositorRepository expositorRepository;

    @Mock
    private InteracaoRepository interacaoRepository;

    @Mock
    private OportunidadeRepository oportunidadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        // Setup básico
    }

    @Test
    void testGetDashboardStats() {
        when(expositorRepository.countByDataCadastroAfter(any(LocalDateTime.class))).thenReturn(5L);
        when(interacaoRepository.countByDataCriacaoAfter(any(LocalDateTime.class))).thenReturn(10L);
        when(interacaoRepository.somarMetrosQuadradosVendidos()).thenReturn(100.0);
        when(interacaoRepository.somarValorPropostasAbertas()).thenReturn(50000.0);
        when(interacaoRepository.contarPropostasGanhas()).thenReturn(3L);
        when(interacaoRepository.somarValorPropostasGanhas()).thenReturn(30000.0);

        Map<String, Object> stats = dashboardController.getDashboardStats();

        assertNotNull(stats);
        assertEquals(5L, stats.get("novosClientes"));
        assertEquals(10L, stats.get("qtdAtividades"));
        assertEquals(100.0, stats.get("metrosQuadradosVendidos"));
        assertEquals(3L, stats.get("qtdGanhos"));
    }

    @Test
    void testGetDashboardStats_ComValoresNull() {
        when(expositorRepository.countByDataCadastroAfter(any(LocalDateTime.class))).thenReturn(0L);
        when(interacaoRepository.countByDataCriacaoAfter(any(LocalDateTime.class))).thenReturn(0L);
        when(interacaoRepository.somarMetrosQuadradosVendidos()).thenReturn(null);
        when(interacaoRepository.somarValorPropostasAbertas()).thenReturn(null);
        when(interacaoRepository.contarPropostasGanhas()).thenReturn(0L);
        when(interacaoRepository.somarValorPropostasGanhas()).thenReturn(null);

        Map<String, Object> stats = dashboardController.getDashboardStats();

        assertNotNull(stats);
        assertEquals(0.0, stats.get("metrosQuadradosVendidos"));
        assertNotNull(stats.get("valorPropostasAbertas"));
        assertNotNull(stats.get("valorGanho"));
    }

    @Test
    void testGetAtividadesGrafico() {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario1 = new Usuario();
        usuario1.setNome("João Silva");
        usuarios.add(usuario1);

        when(usuarioRepository.findByAtivoTrue()).thenReturn(usuarios);

        Map<String, Object> grafico = dashboardController.getAtividadesGrafico();

        assertNotNull(grafico);
        assertTrue(grafico.containsKey("dados"));
        assertTrue(grafico.containsKey("usuarios"));
        assertNotNull(grafico.get("dados"));
        assertNotNull(grafico.get("usuarios"));
    }

    @Test
    void testGetAtividadesGrafico_SemUsuarios() {
        when(usuarioRepository.findByAtivoTrue()).thenReturn(new ArrayList<>());

        Map<String, Object> grafico = dashboardController.getAtividadesGrafico();

        assertNotNull(grafico);
        assertTrue(grafico.containsKey("usuarios"));
        @SuppressWarnings("unchecked")
        List<String> usuarios = (List<String>) grafico.get("usuarios");
        assertEquals("admin", usuarios.get(0));
    }
}

