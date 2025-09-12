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
        // Criar usuários de teste
        if (usuarioRepository.count() == 0) {
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

            // Criar expositores de teste
            Expositor expositor1 = new Expositor();
            expositor1.setRazaoSocial("Tech Solutions LTDA");
            expositor1.setNomeFantasia("TechSol");
            expositor1.setCnpj("12.345.678/0001-90");
            expositor1.setEmail("contato@techsol.com");
            expositor1.setTelefone("(11) 99999-9999");
            expositor1.setStatus(Expositor.StatusExpositor.ATIVO);
            expositor1.setDataCadastro(LocalDateTime.now());
            expositor1.setVendedor(vendedor1);
            expositorRepository.save(expositor1);

            Expositor expositor2 = new Expositor();
            expositor2.setRazaoSocial("Inovação Digital S/A");
            expositor2.setNomeFantasia("InovaDig");
            expositor2.setCnpj("98.765.432/0001-10");
            expositor2.setEmail("ana@inovadig.com");
            expositor2.setTelefone("(11) 88888-8888");
            expositor2.setStatus(Expositor.StatusExpositor.ATIVO);
            expositor2.setDataCadastro(LocalDateTime.now());
            expositor2.setVendedor(vendedor2);
            expositorRepository.save(expositor2);

            // Criar oportunidades de teste
            Oportunidade oportunidade1 = new Oportunidade();
            oportunidade1.setTitulo("Sistema de Gestão");
            oportunidade1.setDescricao("Desenvolvimento de sistema de gestão empresarial");
            oportunidade1.setExpositor(expositor1);
            oportunidade1.setVendedor(vendedor1);
            oportunidade1.setValorEstimado(new BigDecimal("50000.00"));
            oportunidade1.setStatus(Oportunidade.StatusOportunidade.PROSPECCAO);
            oportunidade1.setFonte(Oportunidade.FonteOportunidade.INDICACAO);
            oportunidade1.setDataCriacao(LocalDateTime.now());
            oportunidade1.setDataPrevistaFechamento(LocalDateTime.now().plusMonths(2));
            oportunidade1.setProbabilidadeFechamento(70);
            oportunidadeRepository.save(oportunidade1);

            Oportunidade oportunidade2 = new Oportunidade();
            oportunidade2.setTitulo("E-commerce Personalizado");
            oportunidade2.setDescricao("Criação de plataforma de e-commerce");
            oportunidade2.setExpositor(expositor2);
            oportunidade2.setVendedor(vendedor2);
            oportunidade2.setValorEstimado(new BigDecimal("35000.00"));
            oportunidade2.setStatus(Oportunidade.StatusOportunidade.QUALIFICACAO);
            oportunidade2.setFonte(Oportunidade.FonteOportunidade.SITE);
            oportunidade2.setDataCriacao(LocalDateTime.now());
            oportunidade2.setDataPrevistaFechamento(LocalDateTime.now().plusMonths(1));
            oportunidade2.setProbabilidadeFechamento(85);
            oportunidadeRepository.save(oportunidade2);

            System.out.println("✅ Dados de teste criados com sucesso!");
        }
    }
}
