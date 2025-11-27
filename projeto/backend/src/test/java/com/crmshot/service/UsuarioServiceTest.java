package com.crmshot.service;

import com.crmshot.entity.Usuario;
import com.crmshot.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        UserDetails result = usuarioService.loadUserByUsername("teste@teste.com");

        assertNotNull(result);
        assertEquals("teste@teste.com", result.getUsername());
        verify(usuarioRepository, times(1)).findByEmail("teste@teste.com");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(usuarioRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioService.loadUserByUsername("naoexiste@teste.com");
        });
    }

    @Test
    void testCriarUsuario() {
        when(passwordEncoder.encode("senha123")).thenReturn("encoded_senha");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.criarUsuario(usuario);

        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarUsuarios();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testListarUsuariosAtivos() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByAtivoTrue()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarUsuariosAtivos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).findByAtivoTrue();
    }

    @Test
    void testListarUsuariosPorPerfil() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByPerfil(Usuario.PerfilUsuario.VENDEDOR)).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarUsuariosPorPerfil(Usuario.PerfilUsuario.VENDEDOR);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).findByPerfil(Usuario.PerfilUsuario.VENDEDOR);
    }

    @Test
    void testBuscarPorId_Success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_NotFound() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioService.buscarPorId(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testBuscarPorEmail_Success() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.buscarPorEmail("teste@teste.com");

        assertTrue(result.isPresent());
        assertEquals("teste@teste.com", result.get().getEmail());
    }

    @Test
    void testAtualizarUsuario_ComSenha() {
        usuario.setSenha("novaSenha");
        when(passwordEncoder.encode("novaSenha")).thenReturn("encoded_novaSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.atualizarUsuario(usuario);

        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode("novaSenha");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testAtualizarUsuario_SemSenha() {
        usuario.setSenha(null);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.atualizarUsuario(usuario);

        assertNotNull(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testDesativarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.desativarUsuario(1L);

        assertFalse(usuario.getAtivo());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testDesativarUsuario_NotFound() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.desativarUsuario(999L);
        });
    }

    @Test
    void testAtivarUsuario() {
        usuario.setAtivo(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.ativarUsuario(1L);

        assertTrue(usuario.getAtivo());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testAtualizarUltimoAcesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.atualizarUltimoAcesso(1L);

        assertNotNull(usuario.getUltimoAcesso());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testExisteEmail_True() {
        when(usuarioRepository.existsByEmail("teste@teste.com")).thenReturn(true);

        boolean result = usuarioService.existeEmail("teste@teste.com");

        assertTrue(result);
        verify(usuarioRepository, times(1)).existsByEmail("teste@teste.com");
    }

    @Test
    void testExisteEmail_False() {
        when(usuarioRepository.existsByEmail("naoexiste@teste.com")).thenReturn(false);

        boolean result = usuarioService.existeEmail("naoexiste@teste.com");

        assertFalse(result);
    }

    @Test
    void testContarUsuariosAtivos() {
        when(usuarioRepository.countUsuariosAtivos()).thenReturn(5L);

        Long result = usuarioService.contarUsuariosAtivos();

        assertEquals(5L, result);
        verify(usuarioRepository, times(1)).countUsuariosAtivos();
    }

    @Test
    void testContarUsuariosAtivosPorPerfil() {
        when(usuarioRepository.countUsuariosAtivosPorPerfil(Usuario.PerfilUsuario.VENDEDOR)).thenReturn(3L);

        Long result = usuarioService.contarUsuariosAtivosPorPerfil(Usuario.PerfilUsuario.VENDEDOR);

        assertEquals(3L, result);
        verify(usuarioRepository, times(1)).countUsuariosAtivosPorPerfil(Usuario.PerfilUsuario.VENDEDOR);
    }

    @Test
    void testFindByEmail_Success() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByEmail("teste@teste.com");

        assertNotNull(result);
        assertEquals("teste@teste.com", result.getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(usuarioRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        Usuario result = usuarioService.findByEmail("naoexiste@teste.com");

        assertNull(result);
    }
}



