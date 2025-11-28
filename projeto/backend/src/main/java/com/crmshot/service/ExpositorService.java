package com.crmshot.service;

import com.crmshot.dto.ExpositorRequest;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Usuario;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpositorService {

    @Autowired
    private ExpositorRepository expositorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Expositor criarExpositor(ExpositorRequest request) {
        Expositor expositor = new Expositor();
        expositor.setRazaoSocial(request.getRazaoSocial());
        expositor.setNomeFantasia(request.getNomeFantasia());
        expositor.setCnpj(request.getCnpj());
        expositor.setEmail(request.getEmail());
        expositor.setTelefone(request.getTelefone());
        expositor.setCelular(request.getCelular());
        expositor.setEndereco(request.getEndereco());
        expositor.setCidade(request.getCidade());
        expositor.setEstado(request.getEstado());
        expositor.setCep(request.getCep());
        expositor.setSite(request.getSite());
        expositor.setDescricao(request.getDescricao());
        expositor.setStatus(request.getStatus());

        if (request.getVendedorId() != null) {
            Usuario vendedor = usuarioRepository.findById(request.getVendedorId())
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            expositor.setVendedor(vendedor);
        }

        return expositorRepository.save(expositor);
    }

    public Expositor atualizarExpositor(Long id, ExpositorRequest request) {
        Expositor expositor = expositorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expositor não encontrado"));

        expositor.setRazaoSocial(request.getRazaoSocial());
        expositor.setNomeFantasia(request.getNomeFantasia());
        expositor.setCnpj(request.getCnpj());
        expositor.setEmail(request.getEmail());
        expositor.setTelefone(request.getTelefone());
        expositor.setCelular(request.getCelular());
        expositor.setEndereco(request.getEndereco());
        expositor.setCidade(request.getCidade());
        expositor.setEstado(request.getEstado());
        expositor.setCep(request.getCep());
        expositor.setSite(request.getSite());
        expositor.setDescricao(request.getDescricao());
        expositor.setStatus(request.getStatus());

        if (request.getVendedorId() != null) {
            Usuario vendedor = usuarioRepository.findById(request.getVendedorId())
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
            expositor.setVendedor(vendedor);
        }

        return expositorRepository.save(expositor);
    }

    public Expositor atualizarExpositor(Expositor expositor) {
        return expositorRepository.save(expositor);
    }

    public List<Expositor> listarExpositores() {
        return expositorRepository.findAll();
    }

    public Page<Expositor> listarExpositores(Pageable pageable) {
        return expositorRepository.findAll(pageable);
    }

    public Page<Expositor> buscarExpositores(String nome, Expositor.StatusExpositor status,
                                           Long vendedorId, Pageable pageable) {
        return expositorRepository.findByFiltros(nome, status, vendedorId, pageable);
    }

    public List<Expositor> listarExpositoresPorStatus(Expositor.StatusExpositor status) {
        return expositorRepository.findByStatus(status);
    }

    public List<Expositor> listarExpositoresPorVendedor(Long vendedorId) {
        return expositorRepository.findByVendedorId(vendedorId);
    }

    public Optional<Expositor> buscarPorId(Long id) {
        return expositorRepository.findById(id);
    }

    public Optional<Expositor> buscarPorCnpj(String cnpj) {
        return expositorRepository.findByCnpj(cnpj);
    }

    public void excluirExpositor(Long id) {
        expositorRepository.deleteById(id);
    }

    public boolean existeCnpj(String cnpj) {
        return expositorRepository.existsByCnpj(cnpj);
    }

    public Long contarExpositoresPorStatus(Expositor.StatusExpositor status) {
        return expositorRepository.countByStatus(status);
    }

    public Long contarExpositoresPorVendedor(Long vendedorId) {
        return expositorRepository.countByVendedor(vendedorId);
    }
}
