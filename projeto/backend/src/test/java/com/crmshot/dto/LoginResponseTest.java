package com.crmshot.dto;

import com.crmshot.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@teste.com");
        usuario.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        usuario.setAtivo(true);
    }

    @Test
    void testDefaultConstructor() {
        LoginResponse response = new LoginResponse();

        assertNull(response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertNull(response.getUsuario());
    }

    @Test
    void testParameterizedConstructor() {
        LoginResponse response = new LoginResponse("token123", usuario);

        assertEquals("token123", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertNotNull(response.getUsuario());
        assertEquals(1L, response.getUsuario().getId());
        assertEquals("Teste Usuario", response.getUsuario().getNome());
    }

    @Test
    void testGettersAndSetters() {
        LoginResponse response = new LoginResponse();

        response.setToken("novoToken");
        response.setTipo("JWT");

        assertEquals("novoToken", response.getToken());
        assertEquals("JWT", response.getTipo());
    }

    @Test
    void testUsuarioInfo() {
        LoginResponse response = new LoginResponse("token", usuario);
        LoginResponse.UsuarioInfo usuarioInfo = response.getUsuario();

        assertNotNull(usuarioInfo);
        assertEquals(1L, usuarioInfo.getId());
        assertEquals("Teste Usuario", usuarioInfo.getNome());
        assertEquals("teste@teste.com", usuarioInfo.getEmail());
        assertEquals("VENDEDOR", usuarioInfo.getPerfil());
        assertTrue(usuarioInfo.getAtivo());
    }

    @Test
    void testUsuarioInfoGettersAndSetters() {
        LoginResponse.UsuarioInfo usuarioInfo = new LoginResponse.UsuarioInfo();

        usuarioInfo.setId(2L);
        usuarioInfo.setNome("Novo Nome");
        usuarioInfo.setEmail("novo@email.com");
        usuarioInfo.setPerfil("ADMINISTRADOR");
        usuarioInfo.setAtivo(false);

        assertEquals(2L, usuarioInfo.getId());
        assertEquals("Novo Nome", usuarioInfo.getNome());
        assertEquals("novo@email.com", usuarioInfo.getEmail());
        assertEquals("ADMINISTRADOR", usuarioInfo.getPerfil());
        assertFalse(usuarioInfo.getAtivo());
    }
}

