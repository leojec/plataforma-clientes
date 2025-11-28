package com.crmshot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class RdsConfig {

    @Autowired(required = false)
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        System.out.println("=".repeat(50));
        System.out.println("🔍 INICIANDO CONFIGURAÇÃO DO DATASOURCE");
        System.out.println("=".repeat(50));
        
        // Verificar todas as variáveis RDS
        String rdsHostname = System.getenv("RDS_HOSTNAME");
        String rdsPort = System.getenv("RDS_PORT");
        String rdsDbName = System.getenv("RDS_DB_NAME");
        String rdsUsername = System.getenv("RDS_USERNAME");
        String rdsPassword = System.getenv("RDS_PASSWORD");
        
        System.out.println("RDS_HOSTNAME: " + (rdsHostname != null ? rdsHostname : "NÃO ENCONTRADO"));
        System.out.println("RDS_PORT: " + (rdsPort != null ? rdsPort : "NÃO ENCONTRADO"));
        System.out.println("RDS_DB_NAME: " + (rdsDbName != null ? rdsDbName : "NÃO ENCONTRADO"));
        System.out.println("RDS_USERNAME: " + (rdsUsername != null ? rdsUsername : "NÃO ENCONTRADO"));
        
        if (rdsHostname != null && !rdsHostname.isEmpty()) {
            String port = (rdsPort != null && !rdsPort.isEmpty()) ? rdsPort : "5432";
            String dbName = (rdsDbName != null && !rdsDbName.isEmpty()) ? rdsDbName : "ebdb";
            String username = (rdsUsername != null && !rdsUsername.isEmpty()) ? rdsUsername : "postgres";
            String password = (rdsPassword != null && !rdsPassword.isEmpty()) ? rdsPassword : "";
            
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", rdsHostname, port, dbName);
            
            System.out.println("✅ CONFIGURANDO DATASOURCE COM RDS:");
            System.out.println("   URL: " + jdbcUrl);
            System.out.println("   Username: " + username);
            System.out.println("=".repeat(50));
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
        
        // Fallback para configuração padrão
        String url = (env != null) ? env.getProperty("spring.datasource.url", "jdbc:postgresql://localhost:5432/crmshot") : "jdbc:postgresql://localhost:5432/crmshot";
        String username = (env != null) ? env.getProperty("spring.datasource.username", "postgres") : "postgres";
        String password = (env != null) ? env.getProperty("spring.datasource.password", "34367746") : "34367746";
        
        System.out.println("⚠️ RDS NÃO ENCONTRADO - USANDO CONFIGURAÇÃO PADRÃO:");
        System.out.println("   URL: " + url);
        System.out.println("   Username: " + username);
        System.out.println("=".repeat(50));
        
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

