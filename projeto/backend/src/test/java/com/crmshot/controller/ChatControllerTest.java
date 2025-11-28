package com.crmshot.controller;

import com.crmshot.entity.Expositor;
import com.crmshot.entity.Interacao;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.InteracaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private InteracaoRepository interacaoRepository;

    @Mock
    private ExpositorRepository expositorRepository;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testProcessarPergunta_ProximaReuniao() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "qual a próxima reunião?");

        when(interacaoRepository.findProximasAcoes(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("resposta"));
    }

    @Test
    void testProcessarPergunta_QuantosLeads() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "quantos leads temos?");

        when(expositorRepository.count()).thenReturn(10L);
        when(expositorRepository.countByStatus(any())).thenReturn(5L);

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testProcessarPergunta_AtividadesHoje() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "o que tenho hoje?");

        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_ValorPropostas() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "qual o valor das propostas?");

        when(interacaoRepository.somarValorPropostasAbertas()).thenReturn(1000.0);
        when(interacaoRepository.somarValorPropostasGanhas()).thenReturn(500.0);
        when(interacaoRepository.contarPropostasAbertas()).thenReturn(5L);
        when(interacaoRepository.contarPropostasGanhas()).thenReturn(2L);

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_UltimaAtividade() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "qual a última atividade?");

        Interacao interacao = new Interacao();
        interacao.setId(1L);
        interacao.setDataCriacao(LocalDateTime.now());
        interacao.setAssunto("Teste");
        Expositor expositor = new Expositor();
        expositor.setNomeFantasia("Teste");
        interacao.setExpositor(expositor);
        interacao.setTipo(Interacao.TipoInteracao.EMAIL);

        when(interacaoRepository.findAll()).thenReturn(Arrays.asList(interacao));

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_AtividadesPendentes() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "quais atividades pendentes?");

        when(interacaoRepository.findProximasAcoes(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_MetrosVendidos() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "quantos metros vendidos?");

        when(interacaoRepository.somarMetrosQuadradosVendidos()).thenReturn(100.0);

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_Resumo() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "resumo geral");

        when(expositorRepository.count()).thenReturn(10L);
        when(interacaoRepository.count()).thenReturn(20L);
        when(interacaoRepository.somarValorPropostasAbertas()).thenReturn(1000.0);
        when(interacaoRepository.somarMetrosQuadradosVendidos()).thenReturn(100.0);
        when(interacaoRepository.contarPropostasGanhas()).thenReturn(5L);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_PerguntaDesconhecida() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "pergunta desconhecida");

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("resposta").contains("Desculpe"));
    }

    @Test
    void testProcessarPergunta_Exception() {
        Map<String, String> request = new HashMap<>();


        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("resposta").contains("erro") ||
                   response.getBody().get("resposta").contains("Desculpe"));
    }

    @Test
    void testProcessarPergunta_ProximaReuniao_ComReunioes() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "quando é a próxima reunião?");

        Interacao interacao = new Interacao();
        interacao.setId(1L);
        interacao.setTipo(Interacao.TipoInteracao.REUNIAO);
        interacao.setDataProximaAcao(LocalDateTime.now().plusDays(1));
        interacao.setAssunto("Reunião Teste");
        Expositor expositor = new Expositor();
        expositor.setNomeFantasia("Empresa Teste");
        interacao.setExpositor(expositor);

        when(interacaoRepository.findProximasAcoes(any(LocalDateTime.class))).thenReturn(Arrays.asList(interacao));

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("resposta").contains("próxima reunião"));
    }

    @Test
    void testProcessarPergunta_QuantosLeads_ComDados() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "quantidade de leads");

        when(expositorRepository.count()).thenReturn(10L);
        when(expositorRepository.countByStatus(Expositor.StatusExpositor.ATIVO)).thenReturn(5L);
        when(expositorRepository.countByStatus(Expositor.StatusExpositor.POTENCIAL)).thenReturn(3L);

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("resposta").contains("10"));
    }

    @Test
    void testProcessarPergunta_AtividadesHoje_ComAtividades() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "atividades hoje");

        Interacao interacao = new Interacao();
        interacao.setId(1L);
        interacao.setTipo(Interacao.TipoInteracao.EMAIL);
        interacao.setDataProximaAcao(LocalDateTime.now());
        interacao.setAssunto("Teste");
        interacao.setConcluida(false);

        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(Arrays.asList(interacao));

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_UltimaAtividade_ComAtividades() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "último contato");

        Interacao interacao = new Interacao();
        interacao.setId(1L);
        interacao.setDataCriacao(LocalDateTime.now());
        interacao.setAssunto("Teste");
        interacao.setTipo(Interacao.TipoInteracao.EMAIL);
        Expositor expositor = new Expositor();
        expositor.setRazaoSocial("Empresa Teste");
        interacao.setExpositor(expositor);

        when(interacaoRepository.findAll()).thenReturn(Arrays.asList(interacao));

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_AtividadesPendentes_ComPendentes() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "tarefas pendentes");

        Interacao interacao = new Interacao();
        interacao.setId(1L);
        interacao.setDataProximaAcao(LocalDateTime.now().plusDays(1));
        interacao.setAssunto("Teste");
        interacao.setConcluida(false);

        when(interacaoRepository.findProximasAcoes(any(LocalDateTime.class))).thenReturn(Arrays.asList(interacao));

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProcessarPergunta_MetrosVendidos_ComValor() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "metros quadrados vendidos");

        when(interacaoRepository.somarMetrosQuadradosVendidos()).thenReturn(150.5);

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("resposta").contains("150.50"));
    }

    @Test
    void testProcessarPergunta_MetrosVendidos_Null() {
        Map<String, String> request = new HashMap<>();
        request.put("pergunta", "area vendida");

        when(interacaoRepository.somarMetrosQuadradosVendidos()).thenReturn(null);

        ResponseEntity<Map<String, String>> response = chatController.processarPergunta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

