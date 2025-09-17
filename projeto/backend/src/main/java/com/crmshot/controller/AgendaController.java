package com.crmshot.controller;

import com.crmshot.repository.InteracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = "*")
public class AgendaController {

    @Autowired
    private InteracaoRepository interacaoRepository;

    @GetMapping("/atividades")
    public Map<String, Object> getAtividadesAgenda(
            @RequestParam(required = false) String data,
            @RequestParam(required = false, defaultValue = "dia") String modo) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Por enquanto, retornar dados fictícios baseados na data
        LocalDateTime dataConsulta = data != null ? 
            LocalDateTime.parse(data) : LocalDateTime.now();
        
        List<Map<String, Object>> atividades = new ArrayList<>();
        
        // Gerar algumas atividades fictícias para demonstração
        Map<String, Object> atividade1 = new HashMap<>();
        atividade1.put("id", 1);
        atividade1.put("titulo", "Ligação - João Silva");
        atividade1.put("descricao", "Follow-up da proposta apresentada");
        atividade1.put("tipo", "Ligação");
        atividade1.put("horario", "09:00");
        atividade1.put("data", dataConsulta.toLocalDate().toString());
        atividade1.put("leadNome", "João Silva");
        atividade1.put("leadId", "lead-1");
        atividade1.put("status", "agendada");
        atividades.add(atividade1);
        
        Map<String, Object> atividade2 = new HashMap<>();
        atividade2.put("id", 2);
        atividade2.put("titulo", "Reunião - Maria Santos");
        atividade2.put("descricao", "Apresentação de nova proposta");
        atividade2.put("tipo", "Reunião");
        atividade2.put("horario", "14:30");
        atividade2.put("data", dataConsulta.toLocalDate().toString());
        atividade2.put("leadNome", "Maria Santos");
        atividade2.put("leadId", "lead-2");
        atividade2.put("status", "agendada");
        atividades.add(atividade2);
        
        Map<String, Object> atividade3 = new HashMap<>();
        atividade3.put("id", 3);
        atividade3.put("titulo", "Email - Pedro Costa");
        atividade3.put("descricao", "Envio de documentação complementar");
        atividade3.put("tipo", "Email");
        atividade3.put("horario", "16:00");
        atividade3.put("data", dataConsulta.toLocalDate().toString());
        atividade3.put("leadNome", "Pedro Costa");
        atividade3.put("leadId", "lead-3");
        atividade3.put("status", "concluida");
        atividades.add(atividade3);
        
        response.put("atividades", atividades);
        response.put("total", atividades.size());
        response.put("concluidas", atividades.stream().mapToInt(a -> 
            "concluida".equals(a.get("status")) ? 1 : 0).sum());
        response.put("pendentes", atividades.stream().mapToInt(a -> 
            "agendada".equals(a.get("status")) ? 1 : 0).sum());
        
        return response;
    }
}
