package com.crmshot.config;

import com.crmshot.entity.Usuario;
import com.crmshot.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataLoader dataLoader;

    private String defaultAdminPassword;
    private String defaultUserPassword;

    @BeforeEach
    void setUp() {
        defaultAdminPassword = "admin123";
        defaultUserPassword = "user123";
        
        // Usar reflection para injetar as senhas
        try {
            java.lang.reflect.Field field1 = DataLoader.class.getDeclaredField("defaultAdminPassword");
            field1.setAccessible(true);
            field1.set(dataLoader, defaultAdminPassword);
            
            java.lang.reflect.Field field2 = DataLoader.class.getDeclaredField("defaultUserPassword");
            field2.setAccessible(true);
            field2.set(dataLoader, defaultUserPassword);
        } catch (Exception e) {
            // Ignorar se não conseguir
        }
    }

    @Test
    void testRun_WhenUsersExist_ShouldNotCreateUsers() throws Exception {
        when(usuarioRepository.count()).thenReturn(5L);

        dataLoader.run();

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRun_WhenNoUsers_ShouldCreateUsers() throws Exception {
        when(usuarioRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        dataLoader.run();

        verify(usuarioRepository, times(3)).save(any(Usuario.class));
    }

    @Test
    void testRun_WhenAdminPasswordMissing_ShouldNotCreateUsers() throws Exception {
        when(usuarioRepository.count()).thenReturn(0L);
        
        // Simular senha admin vazia
        try {
            java.lang.reflect.Field field = DataLoader.class.getDeclaredField("defaultAdminPassword");
            field.setAccessible(true);
            field.set(dataLoader, "");
        } catch (Exception e) {
            // Ignorar
        }

        dataLoader.run();

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRun_WhenUserPasswordMissing_ShouldNotCreateUsers() throws Exception {
        when(usuarioRepository.count()).thenReturn(0L);
        
        // Simular senha user vazia
        try {
            java.lang.reflect.Field field = DataLoader.class.getDeclaredField("defaultUserPassword");
            field.setAccessible(true);
            field.set(dataLoader, "");
        } catch (Exception e) {
            // Ignorar
        }

        dataLoader.run();

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRun_CreatesCorrectUsers() throws Exception {
        when(usuarioRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        
        dataLoader.run();

        verify(usuarioRepository, times(3)).save(usuarioCaptor.capture());
        
        List<Usuario> usuarios = usuarioCaptor.getAllValues();
        assertEquals(3, usuarios.size());
        
        // Verificar admin
        Usuario admin = usuarios.get(0);
        assertEquals("Administrador", admin.getNome());
        assertEquals("admin@crmshot.com", admin.getEmail());
        assertEquals(Usuario.PerfilUsuario.ADMINISTRADOR, admin.getPerfil());
        assertTrue(admin.getAtivo());
        
        // Verificar vendedores
        Usuario vendedor1 = usuarios.get(1);
        assertEquals("João Silva", vendedor1.getNome());
        assertEquals(Usuario.PerfilUsuario.VENDEDOR, vendedor1.getPerfil());
        
        Usuario vendedor2 = usuarios.get(2);
        assertEquals("Maria Santos", vendedor2.getNome());
        assertEquals(Usuario.PerfilUsuario.VENDEDOR, vendedor2.getPerfil());
    }
}

