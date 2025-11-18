package com.crmshot.controller;

import com.crmshot.entity.Interacao;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.InteracaoRepository;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private InteracaoRepository interacaoRepository;
    
    @Autowired
    private ExpositorRepository expositorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/atividades/lead/{leadId}")
    public ResponseEntity<List<Map<String, Object>>> getAtividadesPorLead(@PathVariable Long leadId) {
        try {
            // Buscar todas as interações do lead
            List<Interacao> interacoes = interacaoRepository.findByExpositorId(leadId);
            
            List<Map<String, Object>> atividades = new ArrayList<>();
            
            // Converter interações para formato da agenda
            for (Interacao interacao : interacoes) {
                Map<String, Object> atividade = new HashMap<>();
                atividade.put("id", interacao.getId());
                atividade.put("data", interacao.getDataCriacao() != null ? 
                    interacao.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
                atividade.put("tipo", mapTipoInteracao(interacao.getTipo()));
                atividade.put("descricao", interacao.getDescricao());
                atividade.put("agendamento", interacao.getDataProximaAcao() != null ? 
                    "Sim - " + interacao.getDataProximaAcao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Não");
                atividade.put("usuario", interacao.getUsuario() != null ? interacao.getUsuario().getNome() : "Administrador");
                atividade.put("link", ""); // Campo para links futuros
                atividades.add(atividade);
            }
            
            return ResponseEntity.ok(atividades);
        } catch (Exception e) {
            System.out.println("Erro ao buscar atividades do lead: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/atividades")
    public Map<String, Object> getAtividadesAgenda(
            @RequestParam(required = false) String data,
            @RequestParam(required = false, defaultValue = "dia") String modo) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Definir data de consulta
        LocalDate dataConsulta = data != null ? 
            LocalDate.parse(data.substring(0, 10)) : LocalDate.now();
        
        // Buscar atividades reais do banco de dados
        LocalDateTime inicioData = dataConsulta.atStartOfDay();
        LocalDateTime fimData = dataConsulta.plusDays(1).atStartOfDay();
        
        // Buscar todas as interações (atividades) do dia usando query otimizada
        List<Interacao> interacoes = interacaoRepository.findByDataProximaAcao(dataConsulta.atStartOfDay());
        
        List<Map<String, Object>> atividades = new ArrayList<>();
        
        // Converter interações para formato da agenda
        for (Interacao interacao : interacoes) {
            Map<String, Object> atividade = new HashMap<>();
            atividade.put("id", interacao.getId());
            atividade.put("titulo", interacao.getTipo().name() + " - " + 
                (interacao.getExpositor() != null ? 
                    (interacao.getExpositor().getNomeFantasia() != null ? 
                        interacao.getExpositor().getNomeFantasia() : 
                        interacao.getExpositor().getRazaoSocial()) : "Lead"));
            atividade.put("descricao", interacao.getDescricao());
            atividade.put("tipo", mapTipoInteracao(interacao.getTipo()));
            atividade.put("horario", interacao.getDataProximaAcao() != null ? 
                interacao.getDataProximaAcao().format(DateTimeFormatter.ofPattern("HH:mm")) : "00:00");
            atividade.put("data", dataConsulta.toString());
            atividade.put("leadNome", interacao.getExpositor() != null ? 
                (interacao.getExpositor().getNomeFantasia() != null ? 
                    interacao.getExpositor().getNomeFantasia() : 
                    interacao.getExpositor().getRazaoSocial()) : "Lead");
            atividade.put("leadId", "lead-" + (interacao.getExpositor() != null ? 
                interacao.getExpositor().getId() : "0"));
            atividade.put("status", interacao.getConcluida() ? "concluida" : "agendada");
            atividades.add(atividade);
        }
        
        // Não adicionar dados fictícios - mostrar apenas atividades reais cadastradas
        
        response.put("atividades", atividades);
        response.put("total", atividades.size());
        response.put("concluidas", (int) atividades.stream()
            .filter(a -> "concluida".equals(a.get("status")))
            .count());
        response.put("pendentes", (int) atividades.stream()
            .filter(a -> "agendada".equals(a.get("status")))
            .count());
        
        return response;
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
                return "Proposta";
            case FECHADO:
                return "Fechado";
            default:
                return "Outros";
        }
    }
    
    @PostMapping("/atividades")
    public ResponseEntity<Map<String, Object>> salvarAtividade(@RequestBody Map<String, Object> atividadeData) {
        try {
            // Extrair dados da atividade
            String tipoAtividade = (String) atividadeData.get("tipoAtividade");
            String descricao = (String) atividadeData.get("descricao");
            String dataAgendamento = (String) atividadeData.get("dataAgendamento");
            String horarioAgendamento = (String) atividadeData.get("horarioAgendamento");
            String leadId = (String) atividadeData.get("leadId");
            
            // Campos específicos de Proposta
            Object valorPropostaObj = atividadeData.get("valorProposta");
            Object metrosQuadradosObj = atividadeData.get("metrosQuadrados");
            
            // Mapear tipo de atividade
            Interacao.TipoInteracao tipo = mapStringToTipoInteracao(tipoAtividade);
            
            // Buscar expositor (lead)
            Long expositorId = null;
            if (leadId != null) {
                if (leadId.startsWith("lead-")) {
                    try {
                        expositorId = Long.parseLong(leadId.replace("lead-", ""));
                    } catch (NumberFormatException e) {
                        // Se não conseguir converter, usar ID 1 como padrão
                        expositorId = 1L;
                    }
                } else {
                    // Se não tem prefixo "lead-", tentar converter diretamente
                    try {
                        expositorId = Long.parseLong(leadId);
                    } catch (NumberFormatException e) {
                        expositorId = 1L;
                    }
                }
            } else {
                expositorId = 1L;
            }
            
            Optional<Expositor> expositorOpt = expositorRepository.findById(expositorId != null ? expositorId : 1L);
            if (!expositorOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Lead não encontrado");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Buscar usuário (usar o primeiro ativo)
            List<Usuario> usuarios = usuarioRepository.findByAtivoTrue();
            if (usuarios.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Nenhum usuário ativo encontrado");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Criar nova interação
            Interacao novaInteracao = new Interacao();
            novaInteracao.setExpositor(expositorOpt.get());
            novaInteracao.setUsuario(usuarios.get(0)); // Usar primeiro usuário ativo
            novaInteracao.setTipo(tipo);
            novaInteracao.setAssunto(tipoAtividade + " - " + 
                (expositorOpt.get().getNomeFantasia() != null ? 
                    expositorOpt.get().getNomeFantasia() : 
                    expositorOpt.get().getRazaoSocial()));
            
            // Se for PROPOSTA ou FECHADO, processar campos específicos
            if (tipo == Interacao.TipoInteracao.PROPOSTA || tipo == Interacao.TipoInteracao.FECHADO) {
                // Processar valor da proposta
                if (valorPropostaObj != null) {
                    try {
                        String valorStr = valorPropostaObj.toString().replace(",", ".");
                        Double valor = Double.parseDouble(valorStr);
                        novaInteracao.setValorProposta(valor);
                    } catch (NumberFormatException e) {
                        novaInteracao.setValorProposta(0.0);
                    }
                }
                
                // Processar metros quadrados
                if (metrosQuadradosObj != null) {
                    try {
                        String metrosStr = metrosQuadradosObj.toString().replace(",", ".");
                        Double metros = Double.parseDouble(metrosStr);
                        novaInteracao.setMetrosQuadrados(metros);
                    } catch (NumberFormatException e) {
                        novaInteracao.setMetrosQuadrados(0.0);
                    }
                }
                
                // Para proposta/fechado, a descrição é opcional, usar valor formatado se não tiver descrição
                if (descricao == null || descricao.isEmpty()) {
                    String tipoNome = (tipo == Interacao.TipoInteracao.FECHADO) ? "Negócio fechado" : "Proposta";
                    descricao = String.format("%s no valor de R$ %.2f para área de %.2f m²", 
                        tipoNome,
                        novaInteracao.getValorProposta(), 
                        novaInteracao.getMetrosQuadrados());
                }
                
                // Se for FECHADO, marcar como concluído automaticamente
                if (tipo == Interacao.TipoInteracao.FECHADO) {
                    novaInteracao.setConcluida(true);
                }
            }
            
            novaInteracao.setDescricao(descricao != null ? descricao : "Sem descrição");
            
            // Configurar data e horário da próxima ação
            if (dataAgendamento != null && horarioAgendamento != null) {
                try {
                    LocalDate data = LocalDate.parse(dataAgendamento);
                    String[] horaParts = horarioAgendamento.split(":");
                    int hora = Integer.parseInt(horaParts[0]);
                    int minuto = Integer.parseInt(horaParts[1]);
                    
                    LocalDateTime dataProximaAcao = data.atTime(hora, minuto);
                    novaInteracao.setDataProximaAcao(dataProximaAcao);
                } catch (Exception e) {
                    // Se houver erro no parsing, usar data/hora atual + 1 hora
                    novaInteracao.setDataProximaAcao(LocalDateTime.now().plusHours(1));
                }
            } else {
                // Se não informado, agendar para 1 hora à frente
                novaInteracao.setDataProximaAcao(LocalDateTime.now().plusHours(1));
            }
            
            // Salvar no banco
            Interacao atividadeSalva = interacaoRepository.save(novaInteracao);
            
            // Retornar resposta de sucesso
            Map<String, Object> response = new HashMap<>();
            response.put("id", atividadeSalva.getId());
            response.put("sucesso", true);
            response.put("mensagem", "Atividade salva com sucesso");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao salvar atividade: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/atividades/{id}/concluir")
    public ResponseEntity<Map<String, Object>> marcarComoConcluida(@PathVariable Long id) {
        try {
            Optional<Interacao> interacaoOpt = interacaoRepository.findById(id);
            
            if (!interacaoOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("sucesso", false);
                errorResponse.put("mensagem", "Atividade não encontrada");
                return ResponseEntity.notFound().build();
            }
            
            Interacao interacao = interacaoOpt.get();
            interacao.setConcluida(true);
            interacao.setDataAtualizacao(LocalDateTime.now());
            
            interacaoRepository.save(interacao);
            
            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Atividade marcada como concluída");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("sucesso", false);
            errorResponse.put("mensagem", "Erro ao marcar atividade como concluída: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
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
            case "Proposta":
                return Interacao.TipoInteracao.PROPOSTA;
            case "Fechado":
                return Interacao.TipoInteracao.FECHADO;
            default:
                return Interacao.TipoInteracao.OUTROS;
        }
    }
}
