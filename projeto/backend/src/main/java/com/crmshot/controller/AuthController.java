package com.crmshot.controller;

import com.crmshot.dto.LoginRequest;
import com.crmshot.dto.LoginResponse;
import com.crmshot.entity.Usuario;
import com.crmshot.security.JwtUtil;
import com.crmshot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Buscar usuário por email
            Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());
            
            if (usuario == null) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            // Verificar senha com criptografia
            if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                return ResponseEntity.badRequest().body("Senha incorreta");
            }
            
            // Gerar token simples
            String token = "token_" + usuario.getId() + "_" + System.currentTimeMillis();
            
            // Atualizar último acesso
            usuarioService.atualizarUltimoAcesso(usuario.getId());
            
            return ResponseEntity.ok(new LoginResponse(token, usuario));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro no login: " + e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario usuario) {
        try {
            if (usuarioService.existeEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Email já está em uso");
            }
            
            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            return ResponseEntity.ok("Usuário criado com sucesso");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
