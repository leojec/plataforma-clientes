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

    private static final Random RANDOM = new Random();

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

        // Quantidade de m² vendidos (soma de metros quadrados das atividades FECHADO)
        Double metrosQuadradosVendidos = interacaoRepository.somarMetrosQuadradosVendidos();
        stats.put("metrosQuadradosVendidos", metrosQuadradosVendidos != null ? metrosQuadradosVendidos : 0.0);

        // Valor de propostas em aberto (propostas não concluídas)
        Double valorPropostasAbertasDouble = interacaoRepository.somarValorPropostasAbertas();
        BigDecimal valorPropostasAbertas = BigDecimal.valueOf(valorPropostasAbertasDouble != null ? valorPropostasAbertasDouble : 0.0);
        stats.put("valorPropostasAbertas", valorPropostasAbertas);

        // Quantidade de ganhos (propostas concluídas)
        long qtdGanhos = interacaoRepository.contarPropostasGanhas();
        stats.put("qtdGanhos", qtdGanhos);

        // Valor ganho (propostas concluídas)
        Double valorGanhoDouble = interacaoRepository.somarValorPropostasGanhas();
        BigDecimal valorGanho = BigDecimal.valueOf(valorGanhoDouble != null ? valorGanhoDouble : 0.0);
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
                    quantidade = RANDOM.nextInt(20) + 5; // Primeiro usuário: 5-24 atividades
                } else {
                    quantidade = RANDOM.nextInt(15) + 2; // Outros usuários: 2-16 atividades
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
