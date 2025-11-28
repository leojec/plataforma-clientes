package com.crmshot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class RdsConfig {

    private static final Logger logger = LoggerFactory.getLogger(RdsConfig.class);

    @Value("${app.rds.host:${AWS_RDS_HOST:}}")
    private String awsRdsHost;

    @Value("${app.rds.database:${AWS_RDS_DATABASE:}}")
    private String awsRdsDatabase;

    @Value("${app.rds.username:${AWS_RDS_USERNAME:}}")
    private String awsRdsUsername;

    @Value("${app.rds.password:${AWS_RDS_PASSWORD:}}")
    private String awsRdsPassword;

    @Value("${app.rds.port:${AWS_RDS_PORT:5432}}")
    private String awsRdsPort;

    @Bean
    @Primary
    public DataSource dataSource() {
        logger.info("============================================================");
        logger.info("INICIANDO CONFIGURAÇÃO DO DATASOURCE");
        logger.info("============================================================");
        
        // Verificar se as variáveis RDS estão disponíveis (Elastic Beanstalk)
        String rdsHostname = System.getenv("RDS_HOSTNAME");
        
        String finalHostname;
        String finalDatabase;
        String finalUsername;
        String finalPassword;
        String finalPort;
        
        if (rdsHostname != null && !rdsHostname.isEmpty()) {
            // Usar variáveis RDS do Elastic Beanstalk se disponíveis
            finalHostname = rdsHostname;
            finalPort = System.getenv("RDS_PORT");
            finalDatabase = System.getenv("RDS_DB_NAME");
            finalUsername = System.getenv("RDS_USERNAME");
            finalPassword = System.getenv("RDS_PASSWORD");
            
            finalPort = (finalPort != null && !finalPort.isEmpty()) ? finalPort : "5432";
            finalDatabase = (finalDatabase != null && !finalDatabase.isEmpty()) ? finalDatabase : "ebdb";
            finalUsername = (finalUsername != null && !finalUsername.isEmpty()) ? finalUsername : "postgres";
            finalPassword = (finalPassword != null && !finalPassword.isEmpty()) ? finalPassword : "";
            
            logger.info("Usando variáveis RDS do Elastic Beanstalk");
        } else {
            // Usar credenciais RDS configuradas via variáveis de ambiente ou propriedades
            finalHostname = (awsRdsHost != null && !awsRdsHost.isEmpty()) ? awsRdsHost : System.getenv("AWS_RDS_HOST");
            finalPort = (awsRdsPort != null && !awsRdsPort.isEmpty()) ? awsRdsPort : System.getenv("AWS_RDS_PORT");
            finalDatabase = (awsRdsDatabase != null && !awsRdsDatabase.isEmpty()) ? awsRdsDatabase : System.getenv("AWS_RDS_DATABASE");
            finalUsername = (awsRdsUsername != null && !awsRdsUsername.isEmpty()) ? awsRdsUsername : System.getenv("AWS_RDS_USERNAME");
            finalPassword = (awsRdsPassword != null && !awsRdsPassword.isEmpty()) ? awsRdsPassword : System.getenv("AWS_RDS_PASSWORD");
            
            // Validações obrigatórias
            if (finalHostname == null || finalHostname.isEmpty()) {
                logger.error("AWS_RDS_HOST não configurado! Configure a variável de ambiente.");
                throw new IllegalStateException("AWS_RDS_HOST não está configurado. Configure a variável de ambiente AWS_RDS_HOST.");
            }
            
            if (finalPassword == null || finalPassword.isEmpty()) {
                logger.error("AWS_RDS_PASSWORD não configurado! Configure a variável de ambiente.");
                throw new IllegalStateException("AWS_RDS_PASSWORD não está configurado. Configure a variável de ambiente AWS_RDS_PASSWORD.");
            }
            
            if (finalPort == null || finalPort.isEmpty()) {
                finalPort = "5432";
            }
            
            if (finalDatabase == null || finalDatabase.isEmpty()) {
                finalDatabase = "crmshot";
            }
            
            if (finalUsername == null || finalUsername.isEmpty()) {
                finalUsername = "postgres";
            }
            
            logger.info("Usando credenciais RDS configuradas via variáveis de ambiente/propriedades");
        }
        
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", finalHostname, finalPort, finalDatabase);
        
        logger.info("Host: {}", finalHostname);
        logger.info("Port: {}", finalPort);
        logger.info("Database: {}", finalDatabase);
        logger.info("Username: {}", finalUsername);
        logger.info("JDBC URL: {}", jdbcUrl);
        logger.info("============================================================");
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(finalUsername)
                .password(finalPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

