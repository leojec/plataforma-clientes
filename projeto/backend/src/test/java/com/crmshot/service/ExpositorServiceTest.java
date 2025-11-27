package com.crmshot.service;

import com.crmshot.dto.ExpositorRequest;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpositorServiceTest {

    @Mock
    private ExpositorRepository expositorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ExpositorService expositorService;

    private Expositor expositor;
    private ExpositorRequest request;
    private Usuario vendedor;

    @BeforeEach
    void setUp() {
        vendedor = new Usuario();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");
        vendedor.setEmail("vendedor@teste.com");

        expositor = new Expositor();
        expositor.setId(1L);
        expositor.setRazaoSocial("Empresa Teste LTDA");
        expositor.setNomeFantasia("Empresa Teste");
        expositor.setCnpj("12345678000190");
        expositor.setEmail("empresa@teste.com");
        expositor.setStatus(Expositor.StatusExpositor.ATIVO);
        expositor.setVendedor(vendedor);

        request = new ExpositorRequest();
        request.setRazaoSocial("Empresa Teste LTDA");
        request.setNomeFantasia("Empresa Teste");
        request.setCnpj("12345678000190");
        request.setEmail("empresa@teste.com");
        request.setVendedorId(1L);
    }

    @Test
    void testCriarExpositor_Success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(expositorRepository.save(any(Expositor.class))).thenReturn(expositor);

        Expositor result = expositorService.criarExpositor(request);

        assertNotNull(result);
        assertEquals("Empresa Teste LTDA", result.getRazaoSocial());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(expositorRepository, times(1)).save(any(Expositor.class));
    }

    @Test
    void testCriarExpositor_SemVendedor() {
        request.setVendedorId(null);
        when(expositorRepository.save(any(Expositor.class))).thenReturn(expositor);

        Expositor result = expositorService.criarExpositor(request);

        assertNotNull(result);
        verify(usuarioRepository, never()).findById(anyLong());
        verify(expositorRepository, times(1)).save(any(Expositor.class));
    }

    @Test
    void testCriarExpositor_VendedorNaoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            request.setVendedorId(999L);
            expositorService.criarExpositor(request);
        });
    }

    @Test
    void testAtualizarExpositor_Success() {
        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(expositorRepository.save(any(Expositor.class))).thenReturn(expositor);

        Expositor result = expositorService.atualizarExpositor(1L, request);

        assertNotNull(result);
        verify(expositorRepository, times(1)).findById(1L);
        verify(expositorRepository, times(1)).save(any(Expositor.class));
    }

    @Test
    void testAtualizarExpositor_NotFound() {
        when(expositorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            expositorService.atualizarExpositor(999L, request);
        });
    }

    @Test
    void testAtualizarExpositor_Entity() {
        when(expositorRepository.save(any(Expositor.class))).thenReturn(expositor);

        Expositor result = expositorService.atualizarExpositor(expositor);

        assertNotNull(result);
        verify(expositorRepository, times(1)).save(expositor);
    }

    @Test
    void testListarExpositores() {
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorRepository.findAll()).thenReturn(expositores);

        List<Expositor> result = expositorService.listarExpositores();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(expositorRepository, times(1)).findAll();
    }

    @Test
    void testListarExpositores_Paginado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Expositor> page = new PageImpl<>(Arrays.asList(expositor), pageable, 1);
        when(expositorRepository.findAll(pageable)).thenReturn(page);

        Page<Expositor> result = expositorService.listarExpositores(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(expositorRepository, times(1)).findAll(pageable);
    }

    @Test
    void testBuscarExpositores() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Expositor> page = new PageImpl<>(Arrays.asList(expositor), pageable, 1);
        when(expositorRepository.findByFiltros("Empresa", Expositor.StatusExpositor.ATIVO, 1L, pageable))
                .thenReturn(page);

        Page<Expositor> result = expositorService.buscarExpositores("Empresa", Expositor.StatusExpositor.ATIVO, 1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(expositorRepository, times(1)).findByFiltros("Empresa", Expositor.StatusExpositor.ATIVO, 1L, pageable);
    }

    @Test
    void testListarExpositoresPorStatus() {
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorRepository.findByStatus(Expositor.StatusExpositor.ATIVO)).thenReturn(expositores);

        List<Expositor> result = expositorService.listarExpositoresPorStatus(Expositor.StatusExpositor.ATIVO);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(expositorRepository, times(1)).findByStatus(Expositor.StatusExpositor.ATIVO);
    }

    @Test
    void testListarExpositoresPorVendedor() {
        List<Expositor> expositores = Arrays.asList(expositor);
        when(expositorRepository.findByVendedorId(1L)).thenReturn(expositores);

        List<Expositor> result = expositorService.listarExpositoresPorVendedor(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(expositorRepository, times(1)).findByVendedorId(1L);
    }

    @Test
    void testBuscarPorId_Success() {
        when(expositorRepository.findById(1L)).thenReturn(Optional.of(expositor));

        Optional<Expositor> result = expositorService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(expositorRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_NotFound() {
        when(expositorRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Expositor> result = expositorService.buscarPorId(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testBuscarPorCnpj_Success() {
        when(expositorRepository.findByCnpj("12345678000190")).thenReturn(Optional.of(expositor));

        Optional<Expositor> result = expositorService.buscarPorCnpj("12345678000190");

        assertTrue(result.isPresent());
        assertEquals("12345678000190", result.get().getCnpj());
        verify(expositorRepository, times(1)).findByCnpj("12345678000190");
    }

    @Test
    void testExcluirExpositor() {
        doNothing().when(expositorRepository).deleteById(1L);

        expositorService.excluirExpositor(1L);

        verify(expositorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExisteCnpj_True() {
        when(expositorRepository.existsByCnpj("12345678000190")).thenReturn(true);

        boolean result = expositorService.existeCnpj("12345678000190");

        assertTrue(result);
        verify(expositorRepository, times(1)).existsByCnpj("12345678000190");
    }

    @Test
    void testExisteCnpj_False() {
        when(expositorRepository.existsByCnpj("99999999999999")).thenReturn(false);

        boolean result = expositorService.existeCnpj("99999999999999");

        assertFalse(result);
    }

    @Test
    void testContarExpositoresPorStatus() {
        when(expositorRepository.countByStatus(Expositor.StatusExpositor.ATIVO)).thenReturn(5L);

        Long result = expositorService.contarExpositoresPorStatus(Expositor.StatusExpositor.ATIVO);

        assertEquals(5L, result);
        verify(expositorRepository, times(1)).countByStatus(Expositor.StatusExpositor.ATIVO);
    }

    @Test
    void testContarExpositoresPorVendedor() {
        when(expositorRepository.countByVendedor(1L)).thenReturn(3L);

        Long result = expositorService.contarExpositoresPorVendedor(1L);

        assertEquals(3L, result);
        verify(expositorRepository, times(1)).countByVendedor(1L);
    }
}



