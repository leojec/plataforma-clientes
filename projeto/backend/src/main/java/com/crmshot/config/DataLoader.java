package com.crmshot.config;

import com.crmshot.entity.Usuario;
import com.crmshot.entity.Expositor;
import com.crmshot.entity.Oportunidade;
import com.crmshot.entity.Interacao;
import com.crmshot.repository.UsuarioRepository;
import com.crmshot.repository.ExpositorRepository;
import com.crmshot.repository.OportunidadeRepository;
import com.crmshot.repository.InteracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ExpositorRepository expositorRepository;

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    @Autowired
    private InteracaoRepository interacaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem usuários para evitar limpeza desnecessária
        if (usuarioRepository.count() > 0) {
            System.out.println("✅ Banco já possui dados. DataLoader não executará limpeza.");
            return;
        }
        
        System.out.println("🔄 Primeira execução: Criando usuários básicos...");
        
        // Criar apenas usuários básicos
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@crmshot.com");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setPerfil(Usuario.PerfilUsuario.ADMINISTRADOR);
        admin.setAtivo(true);
        admin.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(admin);

        Usuario vendedor1 = new Usuario();
        vendedor1.setNome("João Silva");
        vendedor1.setEmail("joao@crmshot.com");
        vendedor1.setSenha(passwordEncoder.encode("admin123"));
        vendedor1.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        vendedor1.setAtivo(true);
        vendedor1.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(vendedor1);

        Usuario vendedor2 = new Usuario();
        vendedor2.setNome("Maria Santos");
        vendedor2.setEmail("maria@crmshot.com");
        vendedor2.setSenha(passwordEncoder.encode("admin123"));
        vendedor2.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        vendedor2.setAtivo(true);
        vendedor2.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(vendedor2);

        System.out.println("✅ Banco limpo! Apenas usuários básicos criados.");
    }
}
