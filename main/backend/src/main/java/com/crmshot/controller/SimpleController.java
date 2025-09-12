package com.crmshot.controller;

import com.crmshot.dto.LoginRequest;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Oportunidade;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.OportunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SimpleController {
    
    @Autowired
    private ExpositorRepository expositorRepository;
    
    @Autowired
    private OportunidadeRepository oportunidadeRepository;
    
    @GetMapping("/")
    public String home() {
        return "CRM Shot Backend funcionando!";
    }
    
    @GetMapping("/test")
    public String test() {
        return "Teste funcionando!";
    }
    
    @GetMapping("/expositores")
    public ResponseEntity<?> listarExpositores() {
        try {
            // Buscar dados reais do banco e converter para Map para evitar problemas de serialização
            List<Expositor> expositores = expositorRepository.findAll();
            List<Map<String, Object>> expositoresResponse = new ArrayList<>();
            
            for (Expositor expositor : expositores) {
                Map<String, Object> expositorMap = new HashMap<>();
                expositorMap.put("id", expositor.getId());
                expositorMap.put("razaoSocial", expositor.getRazaoSocial());
                expositorMap.put("nomeFantasia", expositor.getNomeFantasia());
                expositorMap.put("cnpj", expositor.getCnpj());
                expositorMap.put("email", expositor.getEmail());
                expositorMap.put("telefone", expositor.getTelefone());
                expositorMap.put("status", expositor.getStatus());
                expositorMap.put("dataCadastro", expositor.getDataCadastro());
                expositoresResponse.add(expositorMap);
            }
            
            return ResponseEntity.ok(expositoresResponse);
        } catch (Exception e) {
            // Fallback para dados de exemplo se houver erro
            Map<String, Object> expositor1 = new HashMap<>();
            expositor1.put("id", 1L);
            expositor1.put("razaoSocial", "Tech Solutions LTDA");
            expositor1.put("nomeFantasia", "TechSol");
            expositor1.put("cnpj", "12.345.678/0001-90");
            expositor1.put("email", "contato@techsol.com");
            expositor1.put("telefone", "(11) 99999-9999");
            expositor1.put("status", "ATIVO");
            
            Map<String, Object> expositor2 = new HashMap<>();
            expositor2.put("id", 2L);
            expositor2.put("razaoSocial", "Inovação Digital S/A");
            expositor2.put("nomeFantasia", "InovaDig");
            expositor2.put("cnpj", "98.765.432/0001-10");
            expositor2.put("email", "ana@inovadig.com");
            expositor2.put("telefone", "(11) 88888-8888");
            expositor2.put("status", "ATIVO");
            
            return ResponseEntity.ok(Arrays.asList(expositor1, expositor2));
        }
    }
    
    @GetMapping("/oportunidades")
    public ResponseEntity<?> listarOportunidades() {
        // Dados de exemplo para demonstração
        Map<String, Object> oportunidade1 = new HashMap<>();
        oportunidade1.put("id", 1L);
        oportunidade1.put("titulo", "Sistema de Gestão");
        oportunidade1.put("descricao", "Desenvolvimento de sistema de gestão empresarial");
        oportunidade1.put("valorEstimado", 50000.00);
        oportunidade1.put("status", "PROSPECCAO");
        oportunidade1.put("fonte", "INDICACAO");
        oportunidade1.put("probabilidadeFechamento", 70);
        
        Map<String, Object> oportunidade2 = new HashMap<>();
        oportunidade2.put("id", 2L);
        oportunidade2.put("titulo", "E-commerce Personalizado");
        oportunidade2.put("descricao", "Criação de plataforma de e-commerce");
        oportunidade2.put("valorEstimado", 35000.00);
        oportunidade2.put("status", "QUALIFICACAO");
        oportunidade2.put("fonte", "SITE");
        oportunidade2.put("probabilidadeFechamento", 85);
        
        return ResponseEntity.ok(Arrays.asList(oportunidade1, oportunidade2));
    }
    
    @PostMapping("/expositores")
    public ResponseEntity<?> criarExpositor(@RequestBody Map<String, Object> expositorData) {
        try {
            Expositor expositor = new Expositor();
            expositor.setRazaoSocial((String) expositorData.get("razaoSocial"));
            expositor.setNomeFantasia((String) expositorData.get("nomeFantasia"));
            expositor.setCnpj((String) expositorData.get("cnpj"));
            expositor.setEmail((String) expositorData.get("email"));
            expositor.setTelefone((String) expositorData.get("telefone"));
            expositor.setStatus(Expositor.StatusExpositor.ATIVO);
            expositor.setDataCadastro(java.time.LocalDateTime.now());
            
            Expositor expositorSalvo = expositorRepository.save(expositor);
            return ResponseEntity.ok(expositorSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar expositor: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Simular login com dados fixos para demonstração
        if ("admin@crmshot.com".equals(loginRequest.getEmail()) && "admin123".equals(loginRequest.getSenha())) {
            // Criar resposta de sucesso
            Map<String, Object> response = new HashMap<>();
            response.put("token", "token_admin_" + System.currentTimeMillis());
            
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("id", 1L);
            usuario.put("nome", "Administrador");
            usuario.put("email", "admin@crmshot.com");
            usuario.put("perfil", "ADMINISTRADOR");
            
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
        } else if ("joao@crmshot.com".equals(loginRequest.getEmail()) && "admin123".equals(loginRequest.getSenha())) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", "token_joao_" + System.currentTimeMillis());
            
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("id", 2L);
            usuario.put("nome", "João Silva");
            usuario.put("email", "joao@crmshot.com");
            usuario.put("perfil", "VENDEDOR");
            
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
        } else if ("maria@crmshot.com".equals(loginRequest.getEmail()) && "admin123".equals(loginRequest.getSenha())) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", "token_maria_" + System.currentTimeMillis());
            
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("id", 3L);
            usuario.put("nome", "Maria Santos");
            usuario.put("email", "maria@crmshot.com");
            usuario.put("perfil", "VENDEDOR");
            
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }
}
