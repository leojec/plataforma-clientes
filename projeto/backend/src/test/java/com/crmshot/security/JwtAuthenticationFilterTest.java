package com.crmshot.security;

import com.crmshot.entity.Usuario;
import com.crmshot.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Usuario usuario;
    private String validToken;
    private String secret = "mySecretKey123456789012345678901234567890";
    private Long expiration = TimeUnit.HOURS.toMillis(24);

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@teste.com");
        usuario.setPerfil(Usuario.PerfilUsuario.VENDEDOR);


        JwtUtil realJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(realJwtUtil, "secret", secret);
        ReflectionTestUtils.setField(realJwtUtil, "expiration", expiration);
        validToken = realJwtUtil.generateToken(usuario);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.extractUsername(validToken)).thenReturn(usuario.getEmail());
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(jwtUtil.validateToken(validToken, usuario)).thenReturn(true);


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, times(1)).extractUsername(validToken);
        verify(jwtUtil, times(1)).validateToken(validToken, usuario);
    }

    @Test
    void testDoFilterInternal_WithoutAuthorizationHeader() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn(null);


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).extractUsername(anyString());
    }

    @Test
    void testDoFilterInternal_WithInvalidAuthorizationFormat() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token123");


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).extractUsername(anyString());
    }

    @Test
    void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {

        String invalidToken = "invalid.token.here";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtUtil.extractUsername(invalidToken)).thenThrow(new RuntimeException("Invalid token"));


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithUserNotFound() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.extractUsername(validToken)).thenReturn("nonexistent@teste.com");
        when(usuarioRepository.findByEmail("nonexistent@teste.com")).thenReturn(Optional.empty());


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).validateToken(anyString(), any(UserDetails.class));
    }

    @Test
    void testDoFilterInternal_WithInvalidTokenValidation() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.extractUsername(validToken)).thenReturn(usuario.getEmail());
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(jwtUtil.validateToken(validToken, usuario)).thenReturn(false);


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, times(1)).validateToken(validToken, usuario);
    }

    @Test
    void testDoFilterInternal_WhenAuthenticationAlreadyExists() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.extractUsername(validToken)).thenReturn(usuario.getEmail());


        SecurityContextHolder.getContext().setAuthentication(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                usuario, null, usuario.getAuthorities()
            )
        );


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(jwtUtil, never()).validateToken(anyString(), any(UserDetails.class));
    }

    @Test
    void testDoFilterInternal_WithEmptyBearerToken() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn("Bearer ");


        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}

