package com.crmshot.controller;

import com.crmshot.dto.ExpositorRequest;
import com.crmshot.entity.Expositor;
import com.crmshot.service.ExpositorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expositores")
@CrossOrigin(origins = "*")
public class ExpositorController {
    
    @Autowired
    private ExpositorService expositorService;
    
    @PostMapping
    public ResponseEntity<?> criarExpositor(@Valid @RequestBody ExpositorRequest request) {
        try {
            if (expositorService.existeCnpj(request.getCnpj())) {
                return ResponseEntity.badRequest()
                    .body("CNPJ já está cadastrado");
            }
            
            Expositor expositor = expositorService.criarExpositor(request);
            return ResponseEntity.ok(expositor);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao criar expositor: " + e.getMessage());
        }
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarExpositores() {
        try {
            List<Expositor> expositores = expositorService.listarExpositores();
            
            // Converter para um formato simples sem referências circulares
            List<Map<String, Object>> expositoresSimples = expositores.stream()
                .map(expositor -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", expositor.getId());
                    map.put("razaoSocial", expositor.getRazaoSocial());
                    map.put("nomeFantasia", expositor.getNomeFantasia());
                    map.put("cnpj", expositor.getCnpj());
                    map.put("email", expositor.getEmail());
                    map.put("telefone", expositor.getTelefone());
                    map.put("celular", expositor.getCelular());
                    map.put("endereco", expositor.getEndereco());
                    map.put("cidade", expositor.getCidade());
                    map.put("estado", expositor.getEstado());
                    map.put("cep", expositor.getCep());
                    map.put("site", expositor.getSite());
                    map.put("descricao", expositor.getDescricao());
                    map.put("status", expositor.getStatus());
                    map.put("dataCadastro", expositor.getDataCadastro());
                    map.put("dataAtualizacao", expositor.getDataAtualizacao());
                    
                    // Apenas informações básicas do vendedor
                    if (expositor.getVendedor() != null) {
                        Map<String, Object> vendedor = new HashMap<>();
                        vendedor.put("id", expositor.getVendedor().getId());
                        vendedor.put("nome", expositor.getVendedor().getNome());
                        vendedor.put("email", expositor.getVendedor().getEmail());
                        map.put("vendedor", vendedor);
                    }
                    
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());
            
            return ResponseEntity.ok(expositoresSimples);
        } catch (Exception e) {
            System.out.println("Erro ao listar expositores: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/paginado")
    public ResponseEntity<Page<Expositor>> listarExpositoresPaginado(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Expositor.StatusExpositor status,
            @RequestParam(required = false) Long vendedorId,
            Pageable pageable) {
        
        Page<Expositor> expositores = expositorService.buscarExpositores(nome, status, vendedorId, pageable);
        return ResponseEntity.ok(expositores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarExpositor(@PathVariable Long id) {
        try {
            Optional<Expositor> expositorOpt = expositorService.buscarPorId(id);
            
            if (expositorOpt.isPresent()) {
                Expositor expositor = expositorOpt.get();
                
                // Converter para um formato simples sem referências circulares
                Map<String, Object> expositorSimples = new HashMap<>();
                expositorSimples.put("id", expositor.getId());
                expositorSimples.put("razaoSocial", expositor.getRazaoSocial());
                expositorSimples.put("nomeFantasia", expositor.getNomeFantasia());
                expositorSimples.put("cnpj", expositor.getCnpj());
                expositorSimples.put("email", expositor.getEmail());
                expositorSimples.put("telefone", expositor.getTelefone());
                expositorSimples.put("celular", expositor.getCelular());
                expositorSimples.put("endereco", expositor.getEndereco());
                expositorSimples.put("cidade", expositor.getCidade());
                expositorSimples.put("estado", expositor.getEstado());
                expositorSimples.put("cep", expositor.getCep());
                expositorSimples.put("site", expositor.getSite());
                expositorSimples.put("descricao", expositor.getDescricao());
                expositorSimples.put("status", expositor.getStatus());
                expositorSimples.put("dataCadastro", expositor.getDataCadastro());
                expositorSimples.put("dataAtualizacao", expositor.getDataAtualizacao());
                
                // Apenas informações básicas do vendedor
                if (expositor.getVendedor() != null) {
                    Map<String, Object> vendedor = new HashMap<>();
                    vendedor.put("id", expositor.getVendedor().getId());
                    vendedor.put("nome", expositor.getVendedor().getNome());
                    vendedor.put("email", expositor.getVendedor().getEmail());
                    expositorSimples.put("vendedor", vendedor);
                }
                
                return ResponseEntity.ok(expositorSimples);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar expositor por ID: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarExpositor(@PathVariable Long id, 
                                              @Valid @RequestBody ExpositorRequest request) {
        try {
            Expositor expositor = expositorService.atualizarExpositor(id, request);
            return ResponseEntity.ok(expositor);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao atualizar expositor: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirExpositor(@PathVariable Long id) {
        try {
            expositorService.excluirExpositor(id);
            return ResponseEntity.ok("Expositor excluído com sucesso");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao excluir expositor: " + e.getMessage());
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Expositor>> listarExpositoresPorStatus(@PathVariable Expositor.StatusExpositor status) {
        List<Expositor> expositores = expositorService.listarExpositoresPorStatus(status);
        return ResponseEntity.ok(expositores);
    }
    
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Expositor>> listarExpositoresPorVendedor(@PathVariable Long vendedorId) {
        List<Expositor> expositores = expositorService.listarExpositoresPorVendedor(vendedorId);
        return ResponseEntity.ok(expositores);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        try {
            String novoStatus = statusData.get("status");
            
            if (novoStatus == null || novoStatus.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Status não pode ser vazio");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Optional<Expositor> expositorOpt = expositorService.buscarPorId(id);
            
            if (!expositorOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("erro", "Expositor não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Expositor expositor = expositorOpt.get();
            Expositor.StatusExpositor statusAnterior = expositor.getStatus();
            
            // Mapear status do frontend para enum do backend
            Expositor.StatusExpositor novoStatusEnum;
            switch (novoStatus) {
                case "Lead":
                    novoStatusEnum = Expositor.StatusExpositor.POTENCIAL;
                    break;
                case "Em Andamento":
                    novoStatusEnum = Expositor.StatusExpositor.ATIVO;
                    break;
                case "Em Negociação":
                    novoStatusEnum = Expositor.StatusExpositor.INATIVO;
                    break;
                case "Stand Fechado":
                    novoStatusEnum = Expositor.StatusExpositor.BLOQUEADO;
                    break;
                default:
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("erro", "Status inválido: " + novoStatus);
                    return ResponseEntity.badRequest().body(errorResponse);
            }
            
            expositor.setStatus(novoStatusEnum);
            expositorService.atualizarExpositor(expositor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Status atualizado com sucesso");
            response.put("id", id);
            response.put("statusAnterior", statusAnterior.name());
            response.put("novoStatus", novoStatusEnum.name());
            response.put("statusFrontend", novoStatus);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("erro", "Erro ao atualizar status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
