package com.crmshot.controller;

import com.crmshot.entity.Oportunidade;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.OportunidadeRepository;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/oportunidades")
@CrossOrigin(origins = "*")
public class OportunidadeController {
    
    private static final Logger logger = LoggerFactory.getLogger(OportunidadeController.class);
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_STATUS = "status";
    private static final String KEY_VALOR_ESTIMADO = "valorEstimado";
    
    @Autowired
    private OportunidadeRepository oportunidadeRepository;
    
    @Autowired
    private ExpositorRepository expositorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarOportunidades() {
        try {
            List<Oportunidade> oportunidades = oportunidadeRepository.findAll();
            
            List<Map<String, Object>> oportunidadesSimples = oportunidades.stream()
                .map(oportunidade -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", oportunidade.getId());
                    map.put(KEY_TITULO, oportunidade.getTitulo());
                    map.put("descricao", oportunidade.getDescricao());
                    map.put(KEY_STATUS, oportunidade.getStatus());
                    map.put("fonte", oportunidade.getFonte());
                    map.put(KEY_VALOR_ESTIMADO, oportunidade.getValorEstimado());
                    map.put("probabilidadeFechamento", oportunidade.getProbabilidadeFechamento());
                    map.put("dataPrevistaFechamento", oportunidade.getDataPrevistaFechamento());
                    map.put("dataCriacao", oportunidade.getDataCriacao());
                    
                    // Informações do expositor
                    if (oportunidade.getExpositor() != null) {
                        Map<String, Object> expositor = new HashMap<>();
                        expositor.put("id", oportunidade.getExpositor().getId());
                        expositor.put("nomeFantasia", oportunidade.getExpositor().getNomeFantasia());
                        expositor.put("razaoSocial", oportunidade.getExpositor().getRazaoSocial());
                        map.put("expositor", expositor);
                    }
                    
                    // Informações do vendedor
                    if (oportunidade.getVendedor() != null) {
                        Map<String, Object> vendedor = new HashMap<>();
                        vendedor.put("id", oportunidade.getVendedor().getId());
                        vendedor.put("nome", oportunidade.getVendedor().getNome());
                        vendedor.put("email", oportunidade.getVendedor().getEmail());
                        map.put("vendedor", vendedor);
                    }
                    
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());
            
            return ResponseEntity.ok(oportunidadesSimples);
        } catch (Exception e) {
            logger.error("Erro ao listar oportunidades: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/criar-exemplo")
    public ResponseEntity<Map<String, Object>> criarOportunidadesExemplo() {
        try {
            // Buscar expositores e vendedores existentes
            List<Expositor> expositores = expositorRepository.findAll();
            List<Usuario> vendedores = usuarioRepository.findByAtivoTrue();
            
            if (expositores.isEmpty() || vendedores.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Não há expositores ou vendedores cadastrados");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            List<Oportunidade> oportunidadesCriadas = new ArrayList<>();
            
            // Criar oportunidades de exemplo
            Oportunidade op1 = new Oportunidade();
            op1.setTitulo("Stand Principal - Feira ABC 2025");
            op1.setDescricao("Oportunidade para stand principal na feira ABC 2025");
            op1.setExpositor(expositores.get(0));
            op1.setVendedor(vendedores.get(0));
            op1.setStatus(Oportunidade.StatusOportunidade.PROSPECCAO);
            op1.setFonte(Oportunidade.FonteOportunidade.INDICACAO);
            op1.setValorEstimado(new BigDecimal("50000.00"));
            op1.setProbabilidadeFechamento(25);
            op1.setDataPrevistaFechamento(LocalDateTime.now().plusMonths(2));
            oportunidadesCriadas.add(oportunidadeRepository.save(op1));
            
            Oportunidade op2 = new Oportunidade();
            op2.setTitulo("Stand Secundário - Evento XYZ");
            op2.setDescricao("Oportunidade para stand secundário no evento XYZ");
            op2.setExpositor(expositores.size() > 1 ? expositores.get(1) : expositores.get(0));
            op2.setVendedor(vendedores.get(0));
            op2.setStatus(Oportunidade.StatusOportunidade.QUALIFICACAO);
            op2.setFonte(Oportunidade.FonteOportunidade.SITE);
            op2.setValorEstimado(new BigDecimal("25000.00"));
            op2.setProbabilidadeFechamento(50);
            op2.setDataPrevistaFechamento(LocalDateTime.now().plusMonths(1));
            oportunidadesCriadas.add(oportunidadeRepository.save(op2));
            
            Oportunidade op3 = new Oportunidade();
            op3.setTitulo("Stand Premium - Feira Internacional");
            op3.setDescricao("Oportunidade para stand premium na feira internacional");
            op3.setExpositor(expositores.get(0));
            op3.setVendedor(vendedores.get(0));
            op3.setStatus(Oportunidade.StatusOportunidade.PROPOSTA);
            op3.setFonte(Oportunidade.FonteOportunidade.TELEFONE);
            op3.setValorEstimado(new BigDecimal("100000.00"));
            op3.setProbabilidadeFechamento(75);
            op3.setDataPrevistaFechamento(LocalDateTime.now().plusMonths(3));
            oportunidadesCriadas.add(oportunidadeRepository.save(op3));
            
            Oportunidade op4 = new Oportunidade();
            op4.setTitulo("Stand Básico - Evento Regional");
            op4.setDescricao("Oportunidade para stand básico no evento regional");
            op4.setExpositor(expositores.size() > 1 ? expositores.get(1) : expositores.get(0));
            op4.setVendedor(vendedores.get(0));
            op4.setStatus(Oportunidade.StatusOportunidade.FECHADA_GANHA);
            op4.setFonte(Oportunidade.FonteOportunidade.EMAIL);
            op4.setValorEstimado(new BigDecimal("15000.00"));
            op4.setProbabilidadeFechamento(100);
            op4.setDataPrevistaFechamento(LocalDateTime.now().minusDays(10));
            op4.setDataFechamento(LocalDateTime.now().minusDays(5));
            oportunidadesCriadas.add(oportunidadeRepository.save(op4));
            
            Oportunidade op5 = new Oportunidade();
            op5.setTitulo("Stand Corporativo - Feira Empresarial");
            op5.setDescricao("Oportunidade para stand corporativo na feira empresarial");
            op5.setExpositor(expositores.get(0));
            op5.setVendedor(vendedores.get(0));
            op5.setStatus(Oportunidade.StatusOportunidade.FECHADA_PERDIDA);
            op5.setFonte(Oportunidade.FonteOportunidade.REDE_SOCIAL);
            op5.setValorEstimado(new BigDecimal("30000.00"));
            op5.setProbabilidadeFechamento(0);
            op5.setDataPrevistaFechamento(LocalDateTime.now().minusDays(20));
            op5.setDataFechamento(LocalDateTime.now().minusDays(15));
            oportunidadesCriadas.add(oportunidadeRepository.save(op5));
            
            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Oportunidades de exemplo criadas com sucesso");
            response.put("totalCriadas", oportunidadesCriadas.size());
            response.put("oportunidades", oportunidadesCriadas.stream().map(op -> {
                Map<String, Object> opData = new HashMap<>();
                opData.put("id", op.getId());
                opData.put(KEY_TITULO, op.getTitulo());
                opData.put(KEY_STATUS, op.getStatus());
                opData.put(KEY_VALOR_ESTIMADO, op.getValorEstimado());
                return opData;
            }).collect(java.util.stream.Collectors.toList()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao criar oportunidades de exemplo: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarOportunidade(@PathVariable Long id) {
        try {
            Optional<Oportunidade> oportunidadeOpt = oportunidadeRepository.findById(id);
            
            if (oportunidadeOpt.isPresent()) {
                Oportunidade oportunidade = oportunidadeOpt.get();
                
                Map<String, Object> oportunidadeSimples = new HashMap<>();
                oportunidadeSimples.put("id", oportunidade.getId());
                oportunidadeSimples.put(KEY_TITULO, oportunidade.getTitulo());
                oportunidadeSimples.put("descricao", oportunidade.getDescricao());
                oportunidadeSimples.put(KEY_STATUS, oportunidade.getStatus());
                oportunidadeSimples.put("fonte", oportunidade.getFonte());
                oportunidadeSimples.put(KEY_VALOR_ESTIMADO, oportunidade.getValorEstimado());
                oportunidadeSimples.put("probabilidadeFechamento", oportunidade.getProbabilidadeFechamento());
                oportunidadeSimples.put("dataPrevistaFechamento", oportunidade.getDataPrevistaFechamento());
                oportunidadeSimples.put("dataCriacao", oportunidade.getDataCriacao());
                
                // Informações do expositor
                if (oportunidade.getExpositor() != null) {
                    Map<String, Object> expositor = new HashMap<>();
                    expositor.put("id", oportunidade.getExpositor().getId());
                    expositor.put("nomeFantasia", oportunidade.getExpositor().getNomeFantasia());
                    expositor.put("razaoSocial", oportunidade.getExpositor().getRazaoSocial());
                    oportunidadeSimples.put("expositor", expositor);
                }
                
                // Informações do vendedor
                if (oportunidade.getVendedor() != null) {
                    Map<String, Object> vendedor = new HashMap<>();
                    vendedor.put("id", oportunidade.getVendedor().getId());
                    vendedor.put("nome", oportunidade.getVendedor().getNome());
                    vendedor.put("email", oportunidade.getVendedor().getEmail());
                    oportunidadeSimples.put("vendedor", vendedor);
                }
                
                return ResponseEntity.ok(oportunidadeSimples);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar oportunidade: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
