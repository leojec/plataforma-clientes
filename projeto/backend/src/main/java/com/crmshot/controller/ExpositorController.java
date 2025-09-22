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
        Optional<Expositor> expositor = expositorService.buscarPorId(id);
        
        if (expositor.isPresent()) {
            return ResponseEntity.ok(expositor.get());
        } else {
            return ResponseEntity.notFound().build();
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
}
