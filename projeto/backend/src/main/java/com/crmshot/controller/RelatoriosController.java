package com.crmshot.controller;

import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.InteracaoRepository;
import com.crmshot.repository.OportunidadeRepository;
import com.crmshot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatoriosController {

    @Autowired
    private ExpositorRepository expositorRepository;

    @Autowired
    private InteracaoRepository interacaoRepository;

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/oportunidades-por-status")
    public Map<String, Object> getOportunidadesPorStatus() {
        Map<String, Object> resultado = new HashMap<>();
        
        // Buscar todas as oportunidades e agrupar por status
        List<com.crmshot.entity.Oportunidade> oportunidades = oportunidadeRepository.findAll();
        
        Map<com.crmshot.entity.Oportunidade.StatusOportunidade, Long> contagemPorStatus = oportunidades.stream()
            .collect(Collectors.groupingBy(
                com.crmshot.entity.Oportunidade::getStatus,
                Collectors.counting()
            ));
        
        List<Map<String, Object>> dados = new ArrayList<>();
        
        // Mapear os status para cores e nomes em português
        Map<com.crmshot.entity.Oportunidade.StatusOportunidade, Map<String, Object>> statusInfo = new HashMap<>();
        statusInfo.put(com.crmshot.entity.Oportunidade.StatusOportunidade.PROSPECCAO, 
            Map.of("nome", "Lead", "cor", "#3B82F6"));
        statusInfo.put(com.crmshot.entity.Oportunidade.StatusOportunidade.QUALIFICACAO, 
            Map.of("nome", "Em Andamento", "cor", "#F59E0B"));
        statusInfo.put(com.crmshot.entity.Oportunidade.StatusOportunidade.PROPOSTA, 
            Map.of("nome", "Em Negociação", "cor", "#8B5CF6"));
        statusInfo.put(com.crmshot.entity.Oportunidade.StatusOportunidade.NEGOCIACAO, 
            Map.of("nome", "Em Negociação", "cor", "#8B5CF6"));
        statusInfo.put(com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_GANHA, 
            Map.of("nome", "Stand Fechado", "cor", "#10B981"));
        statusInfo.put(com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_PERDIDA, 
            Map.of("nome", "Perdida", "cor", "#EF4444"));
        
        for (Map.Entry<com.crmshot.entity.Oportunidade.StatusOportunidade, Long> entry : contagemPorStatus.entrySet()) {
            Map<String, Object> statusData = statusInfo.get(entry.getKey());
            if (statusData != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", statusData.get("nome"));
                item.put("value", entry.getValue());
                item.put("color", statusData.get("cor"));
                dados.add(item);
            }
        }
        
        resultado.put("dados", dados);
        return resultado;
    }

    @GetMapping("/vendas-por-mes")
    public Map<String, Object> getVendasPorMes() {
        Map<String, Object> resultado = new HashMap<>();
        
        // Buscar oportunidades fechadas e ganhas dos últimos 6 meses
        LocalDateTime dataLimite = LocalDateTime.now().minusMonths(6);
        List<com.crmshot.entity.Oportunidade> oportunidades = oportunidadeRepository.findAll();
        
        // Filtrar apenas oportunidades fechadas e ganhas dos últimos 6 meses
        List<com.crmshot.entity.Oportunidade> oportunidadesFechadas = oportunidades.stream()
            .filter(op -> op.getStatus() == com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_GANHA)
            .filter(op -> op.getDataCriacao().isAfter(dataLimite))
            .collect(Collectors.toList());
        
        // Agrupar por mês
        Map<String, BigDecimal> vendasPorMes = new LinkedHashMap<>();
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        
        // Inicializar todos os meses com zero
        for (int i = 0; i < 6; i++) {
            LocalDateTime mesAtual = LocalDateTime.now().minusMonths(i);
            String mesNome = meses[mesAtual.getMonthValue() - 1];
            vendasPorMes.put(mesNome, BigDecimal.ZERO);
        }
        
        // Somar valores por mês
        for (com.crmshot.entity.Oportunidade oportunidade : oportunidadesFechadas) {
            String mesNome = meses[oportunidade.getDataCriacao().getMonthValue() - 1];
            BigDecimal valorAtual = vendasPorMes.get(mesNome);
            BigDecimal valorOportunidade = oportunidade.getValorEstimado() != null ? 
                oportunidade.getValorEstimado() : BigDecimal.ZERO;
            vendasPorMes.put(mesNome, valorAtual.add(valorOportunidade));
        }
        
        List<Map<String, Object>> dados = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : vendasPorMes.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("mes", entry.getKey());
            item.put("vendas", entry.getValue().doubleValue());
            dados.add(item);
        }
        
        resultado.put("dados", dados);
        return resultado;
    }

    @GetMapping("/performance-vendedores")
    public Map<String, Object> getPerformanceVendedores() {
        Map<String, Object> resultado = new HashMap<>();
        
        // Buscar todos os vendedores
        List<com.crmshot.entity.Usuario> vendedores = usuarioRepository.findByAtivoTrue();
        
        List<Map<String, Object>> dados = new ArrayList<>();
        
        for (com.crmshot.entity.Usuario vendedor : vendedores) {
            // Buscar oportunidades do vendedor
            List<com.crmshot.entity.Oportunidade> oportunidades = oportunidadeRepository.findByVendedorId(vendedor.getId());
            
            // Calcular métricas
            long totalOportunidades = oportunidades.size();
            long oportunidadesGanhas = oportunidades.stream()
                .filter(op -> op.getStatus() == com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_GANHA)
                .count();
            
            BigDecimal totalVendas = oportunidades.stream()
                .filter(op -> op.getStatus() == com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_GANHA)
                .map(op -> op.getValorEstimado() != null ? op.getValorEstimado() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal ticketMedio = totalOportunidades > 0 ? 
                totalVendas.divide(BigDecimal.valueOf(totalOportunidades), 2, java.math.RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;
            
            double taxaConversao = totalOportunidades > 0 ? 
                (double) oportunidadesGanhas / totalOportunidades * 100 : 0;
            
            Map<String, Object> vendedorData = new HashMap<>();
            vendedorData.put("vendedor", vendedor.getNome());
            vendedorData.put("vendas", totalVendas.doubleValue());
            vendedorData.put("oportunidades", totalOportunidades);
            vendedorData.put("ticketMedio", ticketMedio.doubleValue());
            vendedorData.put("conversao", Math.round(taxaConversao));
            
            dados.add(vendedorData);
        }
        
        resultado.put("dados", dados);
        return resultado;
    }

    @GetMapping("/debug-oportunidades")
    public Map<String, Object> getDebugOportunidades() {
        Map<String, Object> resultado = new HashMap<>();
        
        // Buscar todas as oportunidades
        List<com.crmshot.entity.Oportunidade> todasOportunidades = oportunidadeRepository.findAll();
        
        resultado.put("totalOportunidades", todasOportunidades.size());
        resultado.put("oportunidades", todasOportunidades.stream().map(op -> {
            Map<String, Object> opData = new HashMap<>();
            opData.put("id", op.getId());
            opData.put("titulo", op.getTitulo());
            opData.put("status", op.getStatus());
            opData.put("valorEstimado", op.getValorEstimado());
            opData.put("vendedor", op.getVendedor() != null ? op.getVendedor().getNome() : "N/A");
            return opData;
        }).collect(Collectors.toList()));
        
        return resultado;
    }

    @GetMapping("/atividades-por-periodo")
    public Map<String, Object> getAtividadesPorPeriodo(@RequestParam(defaultValue = "6") int meses) {
        Map<String, Object> resultado = new HashMap<>();
        
        // Calcular período
        LocalDateTime dataInicio = LocalDateTime.now().minusMonths(meses);
        LocalDateTime dataFim = LocalDateTime.now();
        
        // Buscar atividades por período
        List<com.crmshot.entity.Interacao> atividades = interacaoRepository.findAll().stream()
            .filter(interacao -> interacao.getDataCriacao().isAfter(dataInicio) && 
                               interacao.getDataCriacao().isBefore(dataFim))
            .collect(java.util.stream.Collectors.toList());
        
        // Agrupar por mês
        Map<String, Long> atividadesPorMes = new LinkedHashMap<>();
        String[] mesesNomes = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        
        for (int i = 0; i < meses; i++) {
            LocalDateTime mesAtual = LocalDateTime.now().minusMonths(i);
            String mesNome = mesesNomes[mesAtual.getMonthValue() - 1];
            atividadesPorMes.put(mesNome, 0L);
        }
        
        // Contar atividades por mês
        for (com.crmshot.entity.Interacao atividade : atividades) {
            String mesNome = mesesNomes[atividade.getDataCriacao().getMonthValue() - 1];
            atividadesPorMes.put(mesNome, atividadesPorMes.get(mesNome) + 1);
        }
        
        List<Map<String, Object>> dados = new ArrayList<>();
        for (Map.Entry<String, Long> entry : atividadesPorMes.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("mes", entry.getKey());
            item.put("atividades", entry.getValue());
            dados.add(item);
        }
        
        resultado.put("dados", dados);
        resultado.put("totalAtividades", atividades.size());
        resultado.put("periodo", meses + " meses");
        
        return resultado;
    }

    @GetMapping("/resumo-executivo")
    public Map<String, Object> getResumoExecutivo() {
        Map<String, Object> resultado = new HashMap<>();
        
        // Buscar dados gerais
        long totalExpositores = expositorRepository.count();
        long totalOportunidades = oportunidadeRepository.count();
        long totalInteracoes = interacaoRepository.count();
        
        // Calcular valores
        List<com.crmshot.entity.Oportunidade> oportunidades = oportunidadeRepository.findAll();
        BigDecimal valorTotalEstimado = oportunidades.stream()
            .map(op -> op.getValorEstimado() != null ? op.getValorEstimado() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal valorGanho = oportunidades.stream()
            .filter(op -> op.getStatus() == com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_GANHA)
            .map(op -> op.getValorEstimado() != null ? op.getValorEstimado() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal valorEmAberto = oportunidades.stream()
            .filter(op -> op.getStatus() != com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_GANHA &&
                         op.getStatus() != com.crmshot.entity.Oportunidade.StatusOportunidade.FECHADA_PERDIDA)
            .map(op -> op.getValorEstimado() != null ? op.getValorEstimado() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        resultado.put("totalExpositores", totalExpositores);
        resultado.put("totalOportunidades", totalOportunidades);
        resultado.put("totalInteracoes", totalInteracoes);
        resultado.put("valorTotalEstimado", valorTotalEstimado.doubleValue());
        resultado.put("valorGanho", valorGanho.doubleValue());
        resultado.put("valorEmAberto", valorEmAberto.doubleValue());
        
        return resultado;
    }
}
