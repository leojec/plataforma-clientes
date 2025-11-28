package com.crmshot.config;

import com.crmshot.entity.Usuario;
import com.crmshot.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultAdminPassword;
    private final String defaultUserPassword;

    public DataLoader(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.default.admin.password:${DEFAULT_ADMIN_PASSWORD:}}") String defaultAdminPassword,
            @Value("${app.default.user.password:${DEFAULT_USER_PASSWORD:}}") String defaultUserPassword) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultAdminPassword = defaultAdminPassword;
        this.defaultUserPassword = defaultUserPassword;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem usuários para evitar limpeza desnecessária
        if (usuarioRepository.count() > 0) {
            logger.info("Banco já possui dados. DataLoader não executará limpeza.");
            return;
        }
        
        logger.info("Primeira execução: Criando usuários básicos...");
        
        // Validar que as senhas padrão estão configuradas
        if (defaultAdminPassword == null || defaultAdminPassword.isEmpty()) {
            logger.warn("DEFAULT_ADMIN_PASSWORD não configurado. Usuários padrão não serão criados.");
            logger.warn("Configure a variável de ambiente DEFAULT_ADMIN_PASSWORD para criar usuários padrão.");
            return;
        }
        
        if (defaultUserPassword == null || defaultUserPassword.isEmpty()) {
            logger.warn("DEFAULT_USER_PASSWORD não configurado. Usuários padrão não serão criados.");
            logger.warn("Configure a variável de ambiente DEFAULT_USER_PASSWORD para criar usuários padrão.");
            return;
        }
        
        // Criar apenas usuários básicos
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@crmshot.com");
        admin.setSenha(passwordEncoder.encode(defaultAdminPassword));
        admin.setPerfil(Usuario.PerfilUsuario.ADMINISTRADOR);
        admin.setAtivo(true);
        admin.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(admin);

        Usuario vendedor1 = new Usuario();
        vendedor1.setNome("João Silva");
        vendedor1.setEmail("joao@crmshot.com");
        vendedor1.setSenha(passwordEncoder.encode(defaultUserPassword));
        vendedor1.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        vendedor1.setAtivo(true);
        vendedor1.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(vendedor1);

        Usuario vendedor2 = new Usuario();
        vendedor2.setNome("Maria Santos");
        vendedor2.setEmail("maria@crmshot.com");
        vendedor2.setSenha(passwordEncoder.encode(defaultUserPassword));
        vendedor2.setPerfil(Usuario.PerfilUsuario.VENDEDOR);
        vendedor2.setAtivo(true);
        vendedor2.setDataCriacao(LocalDateTime.now());
        usuarioRepository.save(vendedor2);

        logger.info("Banco limpo! Apenas usuários básicos criados.");
    }
}
