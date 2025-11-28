package com.crmshot.controller;

import com.crmshot.entity.Interacao;
import com.crmshot.entity.Expositor;
import com.crmshot.repository.InteracaoRepository;
import com.crmshot.repository.ExpositorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private InteracaoRepository interacaoRepository;

    @Autowired
    private ExpositorRepository expositorRepository;

    @PostMapping("/perguntar")
    public ResponseEntity<Map<String, String>> processarPergunta(@RequestBody Map<String, String> request) {
        String pergunta = request.get("pergunta");
        if (pergunta == null || pergunta.trim().isEmpty()) {
            return buildErrorResponse();
        }
        pergunta = pergunta.toLowerCase();
        
        try {
            String resposta = processarPerguntaInterna(pergunta);
            return buildRespostaResponse(resposta);
        } catch (Exception e) {
            return buildErrorResponse();
        }
    }

    private String processarPerguntaInterna(String pergunta) {
        if (isPerguntaProximaReuniao(pergunta)) {
            return buscarProximaReuniao();
        } else if (isPerguntaQuantosLeads(pergunta)) {
            return contarLeads();
        } else if (isPerguntaAtividadesHoje(pergunta)) {
            return buscarAtividadesHoje();
        } else if (isPerguntaValorPropostas(pergunta)) {
            return buscarValorPropostas();
        } else if (isPerguntaUltimaAtividade(pergunta)) {
            return buscarUltimaAtividade();
        } else if (isPerguntaAtividadesPendentes(pergunta)) {
            return buscarAtividadesPendentes();
        } else if (isPerguntaMetrosVendidos(pergunta)) {
            return buscarMetrosVendidos();
        } else if (isPerguntaResumo(pergunta)) {
            return gerarResumo();
        } else {
            return getMensagemAjuda();
        }
    }

    private boolean isPerguntaProximaReuniao(String pergunta) {
        return pergunta.contains("próxima reunião") || pergunta.contains("proxima reuniao") || 
               pergunta.contains("próximo encontro") || 
               (pergunta.contains("quando") && pergunta.contains("reunião"));
    }

    private boolean isPerguntaQuantosLeads(String pergunta) {
        return pergunta.contains("quantos leads") || pergunta.contains("quantidade de leads") ||
               pergunta.contains("quantos clientes") || pergunta.contains("total de leads");
    }

    private boolean isPerguntaAtividadesHoje(String pergunta) {
        return pergunta.contains("atividades hoje") || pergunta.contains("atividade hoje") ||
               pergunta.contains("o que tenho hoje");
    }

    private boolean isPerguntaValorPropostas(String pergunta) {
        return pergunta.contains("propostas") && (pergunta.contains("valor") || pergunta.contains("quanto"));
    }

    private boolean isPerguntaUltimaAtividade(String pergunta) {
        return pergunta.contains("última atividade") || pergunta.contains("ultima atividade") ||
               pergunta.contains("último contato") || pergunta.contains("ultimo contato");
    }

    private boolean isPerguntaAtividadesPendentes(String pergunta) {
        return pergunta.contains("atividades pendentes") || pergunta.contains("atividade pendente") ||
               pergunta.contains("tarefas pendentes");
    }

    private boolean isPerguntaMetrosVendidos(String pergunta) {
        return pergunta.contains("m²") || pergunta.contains("metros") || pergunta.contains("area vendida");
    }

    private boolean isPerguntaResumo(String pergunta) {
        return pergunta.contains("resumo") || pergunta.contains("overview") || pergunta.contains("visão geral");
    }

    private String getMensagemAjuda() {
        return "Desculpe, não entendi sua pergunta. Tente perguntar sobre:\n\n" +
               "• Próximas reuniões\n" +
               "• Quantidade de leads\n" +
               "• Atividades de hoje\n" +
               "• Valor de propostas\n" +
               "• Última atividade\n" +
               "• Atividades pendentes\n" +
               "• Metros quadrados vendidos\n" +
               "• Resumo geral";
    }

    private ResponseEntity<Map<String, String>> buildRespostaResponse(String resposta) {
        Map<String, String> response = new HashMap<>();
        response.put("resposta", resposta);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse() {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("resposta", "Desculpe, ocorreu um erro ao processar sua pergunta. Tente novamente.");
        return ResponseEntity.ok(errorResponse);
    }

    private String buscarProximaReuniao() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limite = agora.plusDays(30);
        
        List<Interacao> proximasInteracoes = interacaoRepository.findProximasAcoes(limite)
                .stream()
                .filter(i -> i.getTipo() == Interacao.TipoInteracao.REUNIAO)
                .sorted(Comparator.comparing(Interacao::getDataProximaAcao))
                .collect(Collectors.toList());

        if (proximasInteracoes.isEmpty()) {
            return "Você não tem reuniões agendadas para os próximos 30 dias.";
        }

        Interacao proximaReuniao = proximasInteracoes.get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataFormatada = proximaReuniao.getDataProximaAcao().format(formatter);
        
        return String.format("Sua próxima reunião é no dia %s com %s.\n\nAssunto: %s",
                dataFormatada,
                proximaReuniao.getExpositor().getNomeFantasia() != null ? 
                    proximaReuniao.getExpositor().getNomeFantasia() : 
                    proximaReuniao.getExpositor().getRazaoSocial(),
                proximaReuniao.getAssunto());
    }

    private String contarLeads() {
        long totalLeads = expositorRepository.count();
        long leadsAtivos = expositorRepository.countByStatus(Expositor.StatusExpositor.ATIVO);
        long leadsPotenciais = expositorRepository.countByStatus(Expositor.StatusExpositor.POTENCIAL);
        
        return String.format("Você tem um total de %d leads cadastrados:\n\n" +
                           "• %d leads ativos\n" +
                           "• %d leads potenciais\n\n" +
                           "Continue prospectando! 💪",
                           totalLeads, leadsAtivos, leadsPotenciais);
    }

    private String buscarAtividadesHoje() {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        List<Interacao> atividadesHoje = interacaoRepository.findByDataProximaAcao(LocalDateTime.now())
                .stream()
                .filter(i -> !i.getConcluida())
                .collect(Collectors.toList());

        if (atividadesHoje.isEmpty()) {
            return "Você não tem atividades agendadas para hoje. Aproveite para prospectar novos leads! 🎯";
        }

        StringBuilder resposta = new StringBuilder(String.format("Você tem %d atividade(s) para hoje:\n\n", atividadesHoje.size()));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Interacao atividade : atividadesHoje) {
            resposta.append(String.format("• %s - %s (%s)\n",
                    atividade.getDataProximaAcao().format(formatter),
                    atividade.getAssunto(),
                    mapTipoInteracao(atividade.getTipo())));
        }

        return resposta.toString();
    }

    private String buscarValorPropostas() {
        Double valorAbertas = interacaoRepository.somarValorPropostasAbertas();
        Double valorGanhas = interacaoRepository.somarValorPropostasGanhas();
        Long qtdAbertas = interacaoRepository.contarPropostasAbertas();
        Long qtdGanhas = interacaoRepository.contarPropostasGanhas();
        
        return String.format("💰 Situação das propostas:\n\n" +
                           "Propostas em aberto:\n" +
                           "• %d proposta(s)\n" +
                           "• Valor total: R$ %.2f\n\n" +
                           "Propostas fechadas:\n" +
                           "• %d proposta(s)\n" +
                           "• Valor total: R$ %.2f\n\n" +
                           "Continue trabalhando nas propostas abertas! 🚀",
                           qtdAbertas, valorAbertas,
                           qtdGanhas, valorGanhas);
    }

    private String buscarUltimaAtividade() {
        List<Interacao> atividades = interacaoRepository.findAll();
        if (atividades.isEmpty()) {
            return "Nenhuma atividade registrada ainda.";
        }

        Interacao ultima = atividades.stream()
                .max(Comparator.comparing(Interacao::getDataCriacao))
                .orElse(null);

        if (ultima == null) {
            return "Nenhuma atividade encontrada.";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return String.format("Sua última atividade foi:\n\n" +
                           "📅 %s\n" +
                           "🏢 %s\n" +
                           "📝 %s\n" +
                           "Tipo: %s",
                           ultima.getDataCriacao().format(formatter),
                           ultima.getExpositor().getNomeFantasia() != null ? 
                               ultima.getExpositor().getNomeFantasia() : 
                               ultima.getExpositor().getRazaoSocial(),
                           ultima.getAssunto(),
                           mapTipoInteracao(ultima.getTipo()));
    }

    private String buscarAtividadesPendentes() {
        LocalDateTime agora = LocalDateTime.now();
        List<Interacao> pendentes = interacaoRepository.findProximasAcoes(agora.plusDays(7))
                .stream()
                .filter(i -> !i.getConcluida())
                .sorted(Comparator.comparing(Interacao::getDataProximaAcao))
                .limit(5)
                .collect(Collectors.toList());

        if (pendentes.isEmpty()) {
            return "Parabéns! Você não tem atividades pendentes para os próximos 7 dias. ✅";
        }

        StringBuilder resposta = new StringBuilder(String.format("Você tem %d atividade(s) pendente(s):\n\n", pendentes.size()));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");
        for (Interacao atividade : pendentes) {
            resposta.append(String.format("• %s - %s\n",
                    atividade.getDataProximaAcao().format(formatter),
                    atividade.getAssunto()));
        }

        return resposta.toString();
    }

    private String buscarMetrosVendidos() {
        Double metrosVendidos = interacaoRepository.somarMetrosQuadradosVendidos();
        return String.format("📏 Total de área vendida: %.2f m²\n\n" +
                           "Excelente trabalho! Continue assim! 🎉",
                           metrosVendidos != null ? metrosVendidos : 0.0);
    }

    private String gerarResumo() {
        long totalLeads = expositorRepository.count();
        long totalAtividades = interacaoRepository.count();
        Double valorPropostas = interacaoRepository.somarValorPropostasAbertas();
        Double metrosVendidos = interacaoRepository.somarMetrosQuadradosVendidos();
        Long qtdGanhos = interacaoRepository.contarPropostasGanhas();
        
        LocalDateTime agora = LocalDateTime.now();
        long atividadesHoje = interacaoRepository.findByDataProximaAcao(agora)
                .stream()
                .filter(i -> !i.getConcluida())
                .count();

        return String.format("📊 Resumo do CRM:\n\n" +
                           "👥 Total de leads: %d\n" +
                           "📋 Total de atividades: %d\n" +
                           "📅 Atividades hoje: %d\n\n" +
                           "💰 Propostas em aberto: R$ %.2f\n" +
                           "✅ Negócios fechados: %d\n" +
                           "📏 Área vendida: %.2f m²\n\n" +
                           "Continue o ótimo trabalho! 🚀",
                           totalLeads, totalAtividades, atividadesHoje,
                           valorPropostas != null ? valorPropostas : 0.0,
                           qtdGanhos,
                           metrosVendidos != null ? metrosVendidos : 0.0);
    }

    private String mapTipoInteracao(Interacao.TipoInteracao tipo) {
        switch (tipo) {
            case LIGACAO: return "Ligação";
            case EMAIL: return "Email";
            case REUNIAO: return "Reunião";
            case WHATSAPP: return "WhatsApp";
            case PROPOSTA: return "Proposta";
            case FECHADO: return "Fechado";
            default: return "Outros";
        }
    }
}


