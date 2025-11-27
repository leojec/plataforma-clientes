package com.crmshot.controller;

import com.crmshot.dto.LoginRequest;
import com.crmshot.entity.Usuario;
import com.crmshot.security.JwtUtil;
import com.crmshot.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Usuario usuario;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("$2a$10$encodedPassword");
        usuario.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        usuario.setAtivo(true);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@teste.com");
        loginRequest.setSenha("senha123");
    }

    @Test
    void testLogin_Success() {
        when(usuarioService.findByEmail("teste@teste.com")).thenReturn(usuario);
        when(passwordEncoder.matches("senha123", usuario.getSenha())).thenReturn(true);
        doNothing().when(usuarioService).atualizarUltimoAcesso(1L);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(usuarioService, times(1)).findByEmail("teste@teste.com");
        verify(passwordEncoder, times(1)).matches("senha123", usuario.getSenha());
        verify(usuarioService, times(1)).atualizarUltimoAcesso(1L);
    }

    @Test
    void testLogin_UsuarioNaoEncontrado() {
        when(usuarioService.findByEmail("naoexiste@teste.com")).thenReturn(null);
        loginRequest.setEmail("naoexiste@teste.com");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário não encontrado", response.getBody());
        verify(usuarioService, times(1)).findByEmail("naoexiste@teste.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLogin_SenhaIncorreta() {
        when(usuarioService.findByEmail("teste@teste.com")).thenReturn(usuario);
        when(passwordEncoder.matches("senha123", usuario.getSenha())).thenReturn(false);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Senha incorreta", response.getBody());
        verify(usuarioService, times(1)).findByEmail("teste@teste.com");
        verify(passwordEncoder, times(1)).matches("senha123", usuario.getSenha());
    }

    @Test
    void testLogin_Exception() {
        when(usuarioService.findByEmail("teste@teste.com")).thenThrow(new RuntimeException("Erro no banco"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro no login"));
    }

    @Test
    void testRegister_Success() {
        when(usuarioService.existeEmail("novo@teste.com")).thenReturn(false);
        when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(usuario);

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Novo Usuario");
        novoUsuario.setEmail("novo@teste.com");
        novoUsuario.setSenha("senha123");
        novoUsuario.setPerfil(Usuario.PerfilUsuario.VENDEDOR);

        ResponseEntity<?> response = authController.register(novoUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário criado com sucesso", response.getBody());
        verify(usuarioService, times(1)).existeEmail("novo@teste.com");
        verify(usuarioService, times(1)).criarUsuario(any(Usuario.class));
    }

    @Test
    void testRegister_EmailJaExiste() {
        when(usuarioService.existeEmail("existente@teste.com")).thenReturn(true);

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail("existente@teste.com");

        ResponseEntity<?> response = authController.register(novoUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email já está em uso", response.getBody());
        verify(usuarioService, times(1)).existeEmail("existente@teste.com");
        verify(usuarioService, never()).criarUsuario(any(Usuario.class));
    }

    @Test
    void testRegister_Exception() {
        when(usuarioService.existeEmail("novo@teste.com")).thenReturn(false);
        when(usuarioService.criarUsuario(any(Usuario.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail("novo@teste.com");

        ResponseEntity<?> response = authController.register(novoUsuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao criar usuário"));
    }
}

