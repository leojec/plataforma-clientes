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
import java.util.List;
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
    
    @GetMapping
    public ResponseEntity<Page<Expositor>> listarExpositores(
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
