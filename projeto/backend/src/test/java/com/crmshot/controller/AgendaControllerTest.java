package com.crmshot.controller;

import com.crmshot.entity.Expositor;
import com.crmshot.entity.Interacao;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.InteracaoRepository;
import com.crmshot.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTest {

    @Mock
    private InteracaoRepository interacaoRepository;

    @Mock
    private ExpositorRepository expositorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AgendaController agendaController;

    private Interacao interacao;
    private Expositor expositor;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        expositor = new Expositor();
        expositor.setId(1L);
        expositor.setRazaoSocial("Empresa Teste");
        expositor.setNomeFantasia("Teste");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");

        interacao = new Interacao();
        interacao.setId(1L);
        interacao.setDescricao("Descrição teste");
        interacao.setTipo(Interacao.TipoInteracao.EMAIL);
        interacao.setDataCriacao(LocalDateTime.now());
        interacao.setDataProximaAcao(LocalDateTime.now().plusDays(1));
        interacao.setExpositor(expositor);
        interacao.setUsuario(usuario);
        interacao.setConcluida(false);
    }

    @Test
    void testGetAtividadesPorLead_Success() {
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByExpositorId(1L)).thenReturn(interacoes);

        ResponseEntity<List<Map<String, Object>>> response = agendaController.getAtividadesPorLead(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAtividadesPorLead_Exception() {
        when(interacaoRepository.findByExpositorId(1L)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<List<Map<String, Object>>> response = agendaController.getAtividadesPorLead(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAtividadesAgenda_ComData() {
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda("2024-01-15", "dia");

        assertNotNull(response);
        assertTrue(response.containsKey("atividades"));
        assertTrue(response.containsKey("total"));
    }

    @Test
    void testGetAtividadesAgenda_SemData() {
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertTrue(response.containsKey("atividades"));
    }

    @Test
    void testGetAtividadesAgenda_ComInteracoesConcluidas() {
        interacao.setConcluida(true);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertEquals(1, response.get("concluidas"));
        assertEquals(0, response.get("pendentes"));
    }

    @Test
    void testGetAtividadesAgenda_ComInteracaoSemExpositor() {
        interacao.setExpositor(null);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertEquals("Lead", ((List<Map<String, Object>>) response.get("atividades")).get(0).get("leadNome"));
    }

    @Test
    void testGetAtividadesAgenda_ComInteracaoSemNomeFantasia() {
        expositor.setNomeFantasia(null);
        interacao.setExpositor(expositor);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertEquals("Empresa Teste", ((List<Map<String, Object>>) response.get("atividades")).get(0).get("leadNome"));
    }

    @Test
    void testGetAtividadesAgenda_ComInteracaoSemDataProximaAcao() {
        interacao.setDataProximaAcao(null);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertEquals("00:00", ((List<Map<String, Object>>) response.get("atividades")).get(0).get("horario"));
    }

    @Test
    void testSalvarAtividade_Success() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("descricao", "Teste");
        atividadeData.put("dataAgendamento", "2024-01-15");
        atividadeData.put("horarioAgendamento", "10:00");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("sucesso"));
    }

    @Test
    void testSalvarAtividade_LeadNaoEncontrado() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("leadId", "lead-999");

        when(expositorRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_Proposta() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Proposta");
        atividadeData.put("valorProposta", "10000.00");
        atividadeData.put("metrosQuadrados", "50.0");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testMarcarComoConcluida_Success() {
        when(interacaoRepository.findById(1L)).thenReturn(Optional.of(interacao));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.marcarComoConcluida(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("sucesso"));
    }

    @Test
    void testMarcarComoConcluida_NotFound() {
        when(interacaoRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = agendaController.marcarComoConcluida(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAtividadesPorLead_ComInteracaoSemUsuario() {
        interacao.setUsuario(null);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByExpositorId(1L)).thenReturn(interacoes);

        ResponseEntity<List<Map<String, Object>>> response = agendaController.getAtividadesPorLead(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Administrador", response.getBody().get(0).get("usuario"));
    }

    @Test
    void testGetAtividadesPorLead_ComInteracaoSemDataProximaAcao() {
        interacao.setDataProximaAcao(null);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByExpositorId(1L)).thenReturn(interacoes);

        ResponseEntity<List<Map<String, Object>>> response = agendaController.getAtividadesPorLead(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Não", response.getBody().get(0).get("agendamento"));
    }

    @Test
    void testGetAtividadesAgenda_ComInteracoesPendentes() {
        interacao.setConcluida(false);
        List<Interacao> interacoes = Arrays.asList(interacao);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertEquals(0, response.get("concluidas"));
        assertEquals(1, response.get("pendentes"));
    }

    @Test
    void testSalvarAtividade_ComFechado() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Fechado");
        atividadeData.put("valorProposta", "10000.00");
        atividadeData.put("metrosQuadrados", "50.0");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_SemDataAgendamento() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_LeadIdSemPrefixo() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("leadId", "1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_SemLeadId() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_PropostaSemDescricao() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Proposta");
        atividadeData.put("valorProposta", "10000.00");
        atividadeData.put("metrosQuadrados", "50.0");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_Exception() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testMarcarComoConcluida_Exception() {
        when(interacaoRepository.findById(1L)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<Map<String, Object>> response = agendaController.marcarComoConcluida(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAtividadesAgenda_ComDiferentesTiposInteracao() {
        Interacao ligacao = new Interacao();
        ligacao.setId(2L);
        ligacao.setTipo(Interacao.TipoInteracao.LIGACAO);
        ligacao.setDataProximaAcao(LocalDateTime.now());
        ligacao.setConcluida(false);
        ligacao.setExpositor(expositor);

        Interacao reuniao = new Interacao();
        reuniao.setId(3L);
        reuniao.setTipo(Interacao.TipoInteracao.REUNIAO);
        reuniao.setDataProximaAcao(LocalDateTime.now());
        reuniao.setConcluida(false);
        reuniao.setExpositor(expositor);

        Interacao whatsapp = new Interacao();
        whatsapp.setId(4L);
        whatsapp.setTipo(Interacao.TipoInteracao.WHATSAPP);
        whatsapp.setDataProximaAcao(LocalDateTime.now());
        whatsapp.setConcluida(false);
        whatsapp.setExpositor(expositor);

        Interacao proposta = new Interacao();
        proposta.setId(5L);
        proposta.setTipo(Interacao.TipoInteracao.PROPOSTA);
        proposta.setDataProximaAcao(LocalDateTime.now());
        proposta.setConcluida(false);
        proposta.setExpositor(expositor);

        Interacao fechado = new Interacao();
        fechado.setId(6L);
        fechado.setTipo(Interacao.TipoInteracao.FECHADO);
        fechado.setDataProximaAcao(LocalDateTime.now());
        fechado.setConcluida(false);
        fechado.setExpositor(expositor);

        Interacao outros = new Interacao();
        outros.setId(7L);
        outros.setTipo(Interacao.TipoInteracao.OUTROS);
        outros.setDataProximaAcao(LocalDateTime.now());
        outros.setConcluida(false);
        outros.setExpositor(expositor);

        List<Interacao> interacoes = Arrays.asList(ligacao, reuniao, whatsapp, proposta, fechado, outros);
        when(interacaoRepository.findByDataProximaAcao(any(LocalDateTime.class))).thenReturn(interacoes);

        Map<String, Object> response = agendaController.getAtividadesAgenda(null, "dia");

        assertNotNull(response);
        assertEquals(6, ((List<?>) response.get("atividades")).size());
    }

    @Test
    void testSalvarAtividade_ComDataAgendamentoInvalida() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("dataAgendamento", "data-invalida");
        atividadeData.put("horarioAgendamento", "10:00");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_ComHorarioInvalido() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("dataAgendamento", "2024-01-15");
        atividadeData.put("horarioAgendamento", "horario-invalido");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_ComValorPropostaInvalido() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Proposta");
        atividadeData.put("valorProposta", "invalido");
        atividadeData.put("metrosQuadrados", "50.0");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_ComMetrosQuadradosInvalido() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Proposta");
        atividadeData.put("valorProposta", "10000.00");
        atividadeData.put("metrosQuadrados", "invalido");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_UsuarioNaoEncontrado() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Email");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSalvarAtividade_DiferentesTiposAtividade() {
        String[] tipos = {"Ligação", "Email", "Reunião", "Contato WhatsApp", "Proposta", "Fechado", "Outros"};
        
        for (String tipo : tipos) {
            Map<String, Object> atividadeData = new HashMap<>();
            atividadeData.put("tipoAtividade", tipo);
            atividadeData.put("leadId", "lead-1");

            when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
            when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
            when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

            ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void testSalvarAtividade_ComValorPropostaComVirgula() {
        Map<String, Object> atividadeData = new HashMap<>();
        atividadeData.put("tipoAtividade", "Proposta");
        atividadeData.put("valorProposta", "10.000,50");
        atividadeData.put("metrosQuadrados", "50,5");
        atividadeData.put("leadId", "lead-1");

        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findByAtivoTrue()).thenReturn(Arrays.asList(usuario));
        when(interacaoRepository.save(any(Interacao.class))).thenReturn(interacao);

        ResponseEntity<Map<String, Object>> response = agendaController.salvarAtividade(atividadeData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

