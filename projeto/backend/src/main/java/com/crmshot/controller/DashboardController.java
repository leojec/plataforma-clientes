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

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ExpositorRepository expositorRepository;

    @Autowired
    private InteracaoRepository interacaoRepository;

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Novos clientes (expositores) - últimos 30 dias
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);
        long novosClientes = expositorRepository.countByDataCadastroAfter(dataLimite);
        stats.put("novosClientes", novosClientes);

        // Quantidade de atividades (interações) - últimos 30 dias
        long qtdAtividades = interacaoRepository.countByDataCriacaoAfter(dataLimite);
        stats.put("qtdAtividades", qtdAtividades);

        // Quantidade de perdas (oportunidades perdidas) - usando 0 por enquanto
        long qtdPerdas = 0; // oportunidadeRepository.countByStatus(Oportunidade.StatusOportunidade.PERDIDA);
        stats.put("qtdPerdas", qtdPerdas);

        // Valor de propostas em aberto - usando 0 por enquanto
        BigDecimal valorPropostasAbertas = BigDecimal.ZERO;
        stats.put("valorPropostasAbertas", valorPropostasAbertas);

        // Quantidade de ganhos (oportunidades fechadas) - usando 0 por enquanto
        long qtdGanhos = 0; // oportunidadeRepository.countByStatus(Oportunidade.StatusOportunidade.FECHADA);
        stats.put("qtdGanhos", qtdGanhos);

        // Valor ganho (oportunidades fechadas) - usando 0 por enquanto
        BigDecimal valorGanho = BigDecimal.ZERO;
        stats.put("valorGanho", valorGanho);

        return stats;
    }

    @GetMapping("/atividades-grafico")
    public Map<String, Object> getAtividadesGrafico() {
        Map<String, Object> grafico = new HashMap<>();
        
        // Buscar atividades dos últimos 30 dias agrupadas por dia e usuário
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFim = LocalDateTime.now();
        
        // Dados fictícios para o gráfico (você pode implementar a consulta real depois)
        List<Map<String, Object>> dados = new ArrayList<>();
        
        // Gerar dados para os últimos 30 dias
        for (int i = 29; i >= 0; i--) {
            LocalDateTime data = LocalDateTime.now().minusDays(i);
            Map<String, Object> ponto = new HashMap<>();
            ponto.put("data", data.format(DateTimeFormatter.ofPattern("dd/MM")));
            ponto.put("simoni", (int)(Math.random() * 30) + 5); // Dados fictícios
            ponto.put("leonardo", (int)(Math.random() * 15) + 2); // Dados fictícios
            dados.add(ponto);
        }
        
        grafico.put("dados", dados);
        return grafico;
    }
}
