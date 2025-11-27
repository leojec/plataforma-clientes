package com.crmshot.security;

import com.crmshot.entity.Usuario;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private Usuario usuario;
    private String secret = "mySecretKey123456789012345678901234567890";
    private Long expiration = TimeUnit.HOURS.toMillis(24); // 24 horas

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", expiration);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@teste.com");
        usuario.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(usuario);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(usuario);
        String username = jwtUtil.extractUsername(token);
        
        assertEquals(usuario.getEmail(), username);
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken(usuario);
        Date expirationDate = jwtUtil.extractExpiration(token);
        
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtil.generateToken(usuario);
        boolean isValid = jwtUtil.validateToken(token, usuario);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUser() {
        String token = jwtUtil.generateToken(usuario);
        
        Usuario outroUsuario = new Usuario();
        outroUsuario.setEmail("outro@teste.com");
        
        boolean isValid = jwtUtil.validateToken(token, outroUsuario);
        
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_WithoutUserDetails() {
        String token = jwtUtil.generateToken(usuario);
        boolean isValid = jwtUtil.validateToken(token);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtUtil.validateToken(invalidToken);
        
        assertFalse(isValid);
    }

    @Test
    void testExtractClaim() {
        String token = jwtUtil.generateToken(usuario);
        String subject = jwtUtil.extractClaim(token, Claims::getSubject);
        
        assertEquals(usuario.getEmail(), subject);
    }


    @Test
    void testValidateToken_EmptyToken() {
        boolean isValid = jwtUtil.validateToken("");
        
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_NullToken() {
        boolean isValid = jwtUtil.validateToken((String) null);
        
        assertFalse(isValid);
    }

    @Test
    void testExtractUsername_InvalidToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername("invalid.token");
        });
    }

    @Test
    void testExtractExpiration_InvalidToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractExpiration("invalid.token");
        });
    }

    @Test
    void testExtractClaim_InvalidToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractClaim("invalid.token", Claims::getSubject);
        });
    }
}



