package com.crmshot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cnpj")
@CrossOrigin(origins = "*")
public class CnpjController {

    @GetMapping("/{cnpj}")
    public ResponseEntity<?> buscarCNPJ(@PathVariable String cnpj) {
        try {
            // Remover formatação do CNPJ
            String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
            
            // Validar se tem 14 dígitos
            if (cnpjLimpo.length() != 14) {
                Map<String, String> erro = new HashMap<>();
                erro.put("status", "ERROR");
                erro.put("message", "CNPJ deve ter 14 dígitos");
                return ResponseEntity.badRequest().body(erro);
            }
            
            // Criar RestTemplate para fazer a chamada
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://www.receitaws.com.br/v1/cnpj/" + cnpjLimpo;
            
            // Fazer a chamada para a API da ReceitaWS
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            // Retornar os dados
            return ResponseEntity.ok(response.getBody());
            
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("status", "ERROR");
            erro.put("message", "Erro ao buscar dados do CNPJ: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }
}


