package com.crmshot.controller;

import com.crmshot.dto.LoginRequest;
import com.crmshot.dto.LoginResponse;
import com.crmshot.entity.Usuario;
import com.crmshot.security.JwtUtil;
import com.crmshot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());

            if (usuario == null) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }


            if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                return ResponseEntity.badRequest().body("Senha incorreta");
            }


            String token = jwtUtil.generateToken(usuario);


            usuarioService.atualizarUltimoAcesso(usuario.getId());

            return ResponseEntity.ok(new LoginResponse(token, usuario));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro no login: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody Usuario usuario) {
        try {
            if (usuarioService.existeEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Email já está em uso");
            }

            usuarioService.criarUsuario(usuario);
            return ResponseEntity.ok("Usuário criado com sucesso");

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
