package com.crmshot.controller;

import com.crmshot.entity.Interacao;
import com.crmshot.entity.Oportunidade;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatoriosControllerTest {

    @Mock
    private ExpositorRepository expositorRepository;

    @Mock
    private InteracaoRepository interacaoRepository;

    @Mock
    private OportunidadeRepository oportunidadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RelatoriosController relatoriosController;

    private Oportunidade oportunidade;

    @BeforeEach
    void setUp() {
        oportunidade = new Oportunidade();
        oportunidade.setId(1L);
        oportunidade.setTitulo("Teste");
        oportunidade.setStatus(Oportunidade.StatusOportunidade.PROSPECCAO);
        oportunidade.setValorEstimado(new BigDecimal("10000.00"));
        oportunidade.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void testGetOportunidadesPorStatus() {
        List<Oportunidade> oportunidades = Arrays.asList(oportunidade);
        when(oportunidadeRepository.findAll()).thenReturn(oportunidades);

        Map<String, Object> resultado = relatoriosController.getOportunidadesPorStatus();

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("dados"));
        assertNotNull(resultado.get("dados"));
    }

    @Test
    void testGetVendasPorMes() {
        oportunidade.setStatus(Oportunidade.StatusOportunidade.FECHADA_GANHA);
        List<Oportunidade> oportunidades = Arrays.asList(oportunidade);
        when(oportunidadeRepository.findAll()).thenReturn(oportunidades);

        Map<String, Object> resultado = relatoriosController.getVendasPorMes();

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("dados"));
    }

    @Test
    void testGetPerformanceVendedores() {
        Usuario vendedor = new Usuario();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");
        vendedor.setAtivo(true);

        List<Usuario> vendedores = Arrays.asList(vendedor);
        when(usuarioRepository.findByAtivoTrue()).thenReturn(vendedores);
        when(oportunidadeRepository.findByVendedorId(1L)).thenReturn(new ArrayList<>());

        Map<String, Object> resultado = relatoriosController.getPerformanceVendedores();

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("dados"));
    }

    @Test
    void testGetAtividadesPorPeriodo() {
        Interacao interacao = new Interacao();
        interacao.setId(1L);
        interacao.setDataCriacao(LocalDateTime.now().minusMonths(1));

        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findAll()).thenReturn(interacoes);

        Map<String, Object> resultado = relatoriosController.getAtividadesPorPeriodo(6);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("dados"));
        assertTrue(resultado.containsKey("totalAtividades"));
    }

    @Test
    void testGetResumoExecutivo() {
        when(expositorRepository.count()).thenReturn(10L);
        when(oportunidadeRepository.count()).thenReturn(20L);
        when(interacaoRepository.count()).thenReturn(30L);
        when(oportunidadeRepository.findAll()).thenReturn(Arrays.asList(oportunidade));

        Map<String, Object> resultado = relatoriosController.getResumoExecutivo();

        assertNotNull(resultado);
        assertEquals(10L, resultado.get("totalExpositores"));
        assertEquals(20L, resultado.get("totalOportunidades"));
        assertEquals(30L, resultado.get("totalInteracoes"));
    }
}

