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
import java.util.Random;

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
        
        // Buscar todos os usuários ativos para incluir no gráfico
        List<com.crmshot.entity.Usuario> usuarios = usuarioRepository.findByAtivoTrue();
        
        // Criar lista de nomes de usuários
        List<String> nomeUsuarios = new ArrayList<>();
        for (com.crmshot.entity.Usuario usuario : usuarios) {
            String nomeFormatado = usuario.getNome().toLowerCase().replace(" ", "_");
            nomeUsuarios.add(nomeFormatado);
        }
        
        // Se não há usuários cadastrados, usar dados padrão
        if (nomeUsuarios.isEmpty()) {
            nomeUsuarios.add("admin");
        }
        
        // Gerar dados fictícios para os últimos 30 dias baseados nos usuários reais
        List<Map<String, Object>> dados = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 29; i >= 0; i--) {
            LocalDateTime data = LocalDateTime.now().minusDays(i);
            String dataFormatada = data.format(DateTimeFormatter.ofPattern("dd/MM"));
            
            Map<String, Object> ponto = new HashMap<>();
            ponto.put("data", dataFormatada);
            
            // Gerar dados aleatórios para cada usuário
            for (int j = 0; j < nomeUsuarios.size(); j++) {
                String nomeUsuario = nomeUsuarios.get(j);
                int quantidade;
                
                // Dar valores diferentes para cada usuário
                if (j == 0) {
                    quantidade = random.nextInt(20) + 5; // Primeiro usuário: 5-24 atividades
                } else {
                    quantidade = random.nextInt(15) + 2; // Outros usuários: 2-16 atividades
                }
                
                ponto.put(nomeUsuario, quantidade);
            }
            
            dados.add(ponto);
        }
        
        grafico.put("dados", dados);
        grafico.put("usuarios", nomeUsuarios);
        return grafico;
    }
}
