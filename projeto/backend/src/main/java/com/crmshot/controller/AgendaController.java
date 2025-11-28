package com.crmshot.controller;

import com.crmshot.entity.Interacao;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.InteracaoRepository;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.UsuarioRepository;
import com.crmshot.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = "*")
public class AgendaController {

    private static final Logger logger = LoggerFactory.getLogger(AgendaController.class);
    private static final String KEY_DESCRICAO = "descricao";
    private static final String KEY_LEAD_PREFIX = "lead-";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SUCESSO = "sucesso";
    private static final String KEY_MENSAGEM = "mensagem";
    private static final String TIPO_PROPOSTA = "Proposta";

    private final InteracaoRepository interacaoRepository;
    private final ExpositorRepository expositorRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AgendaController(
            InteracaoRepository interacaoRepository,
            ExpositorRepository expositorRepository,
            UsuarioRepository usuarioRepository,
            JwtUtil jwtUtil) {
        this.interacaoRepository = interacaoRepository;
        this.expositorRepository = expositorRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/atividades/lead/{leadId}")
    public ResponseEntity<List<Map<String, Object>>> getAtividadesPorLead(@PathVariable Long leadId) {
        try {

            List<Interacao> interacoes = interacaoRepository.findByExpositorId(leadId);

            List<Map<String, Object>> atividades = new ArrayList<>();


            for (Interacao interacao : interacoes) {
                Map<String, Object> atividade = new HashMap<>();
                atividade.put("id", interacao.getId());
                atividade.put("data", interacao.getDataCriacao() != null ?
                    interacao.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
                atividade.put("tipo", mapTipoInteracao(interacao.getTipo()));
                atividade.put(KEY_DESCRICAO, interacao.getDescricao());
                atividade.put("agendamento", interacao.getDataProximaAcao() != null ?
                    "Sim - " + interacao.getDataProximaAcao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Não");
                atividade.put("usuario", interacao.getUsuario() != null ? interacao.getUsuario().getNome() : "Administrador");
                atividade.put("link", "");
                atividades.add(atividade);
            }

            return ResponseEntity.ok(atividades);
        } catch (Exception e) {
            logger.error("Erro ao buscar atividades do lead: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/atividades")
    public Map<String, Object> getAtividadesAgenda(
            @RequestParam(required = false) String data,
            @RequestParam(required = false, defaultValue = "dia") String modo) {

        LocalDate dataConsulta = parseDataConsulta(data);
        List<Interacao> interacoes = buscarInteracoesDoDia(dataConsulta);
        List<Map<String, Object>> atividades = converterInteracoesParaAtividades(interacoes, dataConsulta);

        return buildResponseAtividades(atividades);
    }

    private LocalDate parseDataConsulta(String data) {
        return data != null ? LocalDate.parse(data.substring(0, 10)) : LocalDate.now();
    }

    private List<Interacao> buscarInteracoesDoDia(LocalDate dataConsulta) {
        return interacaoRepository.findByDataProximaAcao(dataConsulta.atStartOfDay());
    }

    private List<Map<String, Object>> converterInteracoesParaAtividades(List<Interacao> interacoes, LocalDate dataConsulta) {
        List<Map<String, Object>> atividades = new ArrayList<>();

        for (Interacao interacao : interacoes) {
            Map<String, Object> atividade = criarMapaAtividade(interacao, dataConsulta);
            atividades.add(atividade);
        }

        return atividades;
    }

    private Map<String, Object> criarMapaAtividade(Interacao interacao, LocalDate dataConsulta) {
        Map<String, Object> atividade = new HashMap<>();
        atividade.put("id", interacao.getId());
        atividade.put("titulo", buildTituloAtividade(interacao));
        atividade.put(KEY_DESCRICAO, interacao.getDescricao());
        atividade.put("tipo", mapTipoInteracao(interacao.getTipo()));
        atividade.put("horario", formatHorario(interacao.getDataProximaAcao()));
        atividade.put("data", dataConsulta.toString());
        atividade.put("leadNome", getNomeExpositor(interacao));
        atividade.put("leadId", KEY_LEAD_PREFIX + getExpositorId(interacao));
        atividade.put(KEY_STATUS, interacao.getConcluida() ? "concluida" : "agendada");
        atividade.put("usuario", interacao.getUsuario() != null ? interacao.getUsuario().getNome() : "Não atribuído");
        return atividade;
    }

    private String buildTituloAtividade(Interacao interacao) {
        return interacao.getTipo().name() + " - " + getNomeExpositor(interacao);
    }

    private String getNomeExpositor(Interacao interacao) {
        if (interacao.getExpositor() == null) {
            return "Lead";
        }
        return interacao.getExpositor().getNomeFantasia() != null ?
            interacao.getExpositor().getNomeFantasia() :
            interacao.getExpositor().getRazaoSocial();
    }

    private String getExpositorId(Interacao interacao) {
        return interacao.getExpositor() != null ?
            interacao.getExpositor().getId().toString() : "0";
    }

    private String formatHorario(LocalDateTime dataProximaAcao) {
        return dataProximaAcao != null ?
            dataProximaAcao.format(DateTimeFormatter.ofPattern("HH:mm")) : "00:00";
    }

    private Map<String, Object> buildResponseAtividades(List<Map<String, Object>> atividades) {
        Map<String, Object> response = new HashMap<>();
        response.put("atividades", atividades);
        response.put("total", atividades.size());
        response.put("concluidas", contarAtividadesPorStatus(atividades, "concluida"));
        response.put("pendentes", contarAtividadesPorStatus(atividades, "agendada"));
        return response;
    }

    private int contarAtividadesPorStatus(List<Map<String, Object>> atividades, String status) {
        return (int) atividades.stream()
            .filter(a -> status.equals(a.get(KEY_STATUS)))
            .count();
    }

    private String mapTipoInteracao(Interacao.TipoInteracao tipo) {
        switch (tipo) {
            case LIGACAO:
                return "Ligação";
            case EMAIL:
                return "Email";
            case REUNIAO:
                return "Reunião";
            case WHATSAPP:
                return "Contato WhatsApp";
            case PROPOSTA:
                return TIPO_PROPOSTA;
            case FECHADO:
                return "Fechado";
            default:
                return "Outros";
        }
    }

    @PostMapping("/atividades")
    public ResponseEntity<Map<String, Object>> salvarAtividade(
            @RequestBody Map<String, Object> atividadeData,
            HttpServletRequest httpRequest) {
        try {
            AtividadeRequest request = extractAtividadeRequest(atividadeData);
            Interacao.TipoInteracao tipo = mapStringToTipoInteracao(request.tipoAtividade);

            Expositor expositor = buscarExpositor(request.leadId);
            Usuario usuario = obterUsuarioLogado(httpRequest);

            Interacao novaInteracao = criarInteracao(request, tipo, expositor, usuario);
            processarCamposEspecificos(novaInteracao, request, tipo);
            configurarDataProximaAcao(novaInteracao, request);

            Interacao atividadeSalva = interacaoRepository.save(novaInteracao);
            return buildSuccessResponse(atividadeSalva.getId());

        } catch (Exception e) {
            logger.error("Erro ao salvar atividade: {}", e.getMessage(), e);
            return buildErrorResponse("Erro ao salvar atividade: " + e.getMessage());
        }
    }

    private AtividadeRequest extractAtividadeRequest(Map<String, Object> atividadeData) {
        AtividadeRequest request = new AtividadeRequest();
        request.tipoAtividade = (String) atividadeData.get("tipoAtividade");
        request.descricao = (String) atividadeData.get(KEY_DESCRICAO);
        request.dataAgendamento = (String) atividadeData.get("dataAgendamento");
        request.horarioAgendamento = (String) atividadeData.get("horarioAgendamento");
        request.leadId = (String) atividadeData.get("leadId");
        request.valorPropostaObj = atividadeData.get("valorProposta");
        request.metrosQuadradosObj = atividadeData.get("metrosQuadrados");
        return request;
    }

    private Expositor buscarExpositor(String leadId) {
        Long expositorId = extractExpositorId(leadId);
        Optional<Expositor> expositorOpt = expositorRepository.findById(expositorId != null ? expositorId : 1L);
        if (!expositorOpt.isPresent()) {
            throw new IllegalArgumentException("Lead não encontrado");
        }
        return expositorOpt.get();
    }

    private Usuario obterUsuarioLogado(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                String email = jwtUtil.extractUsername(jwt);
                
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
                if (usuarioOpt.isPresent()) {
                    return usuarioOpt.get();
                }
            }
            
            logger.warn("Não foi possível obter usuário do token JWT, usando fallback");
            List<Usuario> usuarios = usuarioRepository.findByAtivoTrue();
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            }
            
            throw new IllegalArgumentException("Nenhum usuário encontrado");
        } catch (Exception e) {
            logger.error("Erro ao obter usuário logado: {}", e.getMessage(), e);
        List<Usuario> usuarios = usuarioRepository.findByAtivoTrue();
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            }
            throw new IllegalArgumentException("Nenhum usuário encontrado");
        }
    }

    private Interacao criarInteracao(AtividadeRequest request, Interacao.TipoInteracao tipo,
                                     Expositor expositor, Usuario usuario) {
        Interacao novaInteracao = new Interacao();
        novaInteracao.setExpositor(expositor);
        novaInteracao.setUsuario(usuario);
        novaInteracao.setTipo(tipo);
        novaInteracao.setAssunto(request.tipoAtividade + " - " + getNomeExpositor(expositor));
        return novaInteracao;
    }

    private String getNomeExpositor(Expositor expositor) {
        return expositor.getNomeFantasia() != null ?
            expositor.getNomeFantasia() :
            expositor.getRazaoSocial();
    }

    private void processarCamposEspecificos(Interacao interacao, AtividadeRequest request,
                                           Interacao.TipoInteracao tipo) {
        if (tipo == Interacao.TipoInteracao.PROPOSTA || tipo == Interacao.TipoInteracao.FECHADO) {
            processarValorProposta(interacao, request.valorPropostaObj);
            processarMetrosQuadrados(interacao, request.metrosQuadradosObj);
            processarDescricaoProposta(interacao, request.descricao, tipo);

            if (tipo == Interacao.TipoInteracao.FECHADO) {
                interacao.setConcluida(true);
            }
        } else {
            interacao.setDescricao(request.descricao != null ? request.descricao : "Sem descrição");
        }
    }

    private void processarValorProposta(Interacao interacao, Object valorPropostaObj) {
        if (valorPropostaObj != null) {
            try {
                String valorStr = valorPropostaObj.toString().replace(",", ".");
                Double valor = Double.parseDouble(valorStr);
                interacao.setValorProposta(valor);
            } catch (NumberFormatException e) {
                interacao.setValorProposta(0.0);
            }
        }
    }

    private void processarMetrosQuadrados(Interacao interacao, Object metrosQuadradosObj) {
        if (metrosQuadradosObj != null) {
            try {
                String metrosStr = metrosQuadradosObj.toString().replace(",", ".");
                Double metros = Double.parseDouble(metrosStr);
                interacao.setMetrosQuadrados(metros);
            } catch (NumberFormatException e) {
                interacao.setMetrosQuadrados(0.0);
            }
        }
    }

    private void processarDescricaoProposta(Interacao interacao, String descricao, Interacao.TipoInteracao tipo) {
        if (descricao == null || descricao.isEmpty()) {
            String tipoNome = (tipo == Interacao.TipoInteracao.FECHADO) ? "Negócio fechado" : TIPO_PROPOSTA;
            descricao = String.format("%s no valor de R$ %.2f para área de %.2f m²",
                tipoNome,
                interacao.getValorProposta(),
                interacao.getMetrosQuadrados());
        }
        interacao.setDescricao(descricao);
    }

    private void configurarDataProximaAcao(Interacao interacao, AtividadeRequest request) {
        if (request.dataAgendamento != null && request.horarioAgendamento != null) {
            try {
                LocalDate data = LocalDate.parse(request.dataAgendamento);
                String[] horaParts = request.horarioAgendamento.split(":");
                int hora = Integer.parseInt(horaParts[0]);
                int minuto = Integer.parseInt(horaParts[1]);
                interacao.setDataProximaAcao(data.atTime(hora, minuto));
            } catch (Exception e) {
                interacao.setDataProximaAcao(LocalDateTime.now().plusHours(1));
            }
        } else {
            interacao.setDataProximaAcao(LocalDateTime.now().plusHours(1));
        }
    }

    private Long extractExpositorId(String leadId) {
        if (leadId == null) {
            return 1L;
        }

        if (leadId.startsWith(KEY_LEAD_PREFIX)) {
            try {
                return Long.parseLong(leadId.replace(KEY_LEAD_PREFIX, ""));
            } catch (NumberFormatException e) {
                return 1L;
            }
        }

        try {
            return Long.parseLong(leadId);
        } catch (NumberFormatException e) {
            return 1L;
        }
    }

    private ResponseEntity<Map<String, Object>> buildSuccessResponse(Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put(KEY_SUCESSO, true);
        response.put(KEY_MENSAGEM, "Atividade salva com sucesso");
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String mensagem) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("erro", mensagem);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(boolean sucesso, String mensagem) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(KEY_SUCESSO, sucesso);
        errorResponse.put(KEY_MENSAGEM, mensagem);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private static class AtividadeRequest {
        String tipoAtividade;
        String descricao;
        String dataAgendamento;
        String horarioAgendamento;
        String leadId;
        Object valorPropostaObj;
        Object metrosQuadradosObj;
    }

    @PutMapping("/atividades/{id}/concluir")
    public ResponseEntity<Map<String, Object>> marcarComoConcluida(@PathVariable Long id) {
        try {
            Optional<Interacao> interacaoOpt = interacaoRepository.findById(id);

            if (!interacaoOpt.isPresent()) {
                return buildErrorResponse(false, "Atividade não encontrada");
            }

            Interacao interacao = interacaoOpt.get();
            interacao.setConcluida(true);
            interacao.setDataAtualizacao(LocalDateTime.now());

            interacaoRepository.save(interacao);

            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCESSO, true);
            response.put(KEY_MENSAGEM, "Atividade marcada como concluída");
            response.put("id", id);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(false, "Erro ao marcar atividade como concluída: " + e.getMessage());
        }
    }

    private Interacao.TipoInteracao mapStringToTipoInteracao(String tipo) {
        switch (tipo) {
            case "Ligação":
                return Interacao.TipoInteracao.LIGACAO;
            case "Email":
                return Interacao.TipoInteracao.EMAIL;
            case "Reunião":
                return Interacao.TipoInteracao.REUNIAO;
            case "Contato WhatsApp":
                return Interacao.TipoInteracao.WHATSAPP;
            case TIPO_PROPOSTA:
                return Interacao.TipoInteracao.PROPOSTA;
            case "Fechado":
                return Interacao.TipoInteracao.FECHADO;
            default:
                return Interacao.TipoInteracao.OUTROS;
        }
    }
}
