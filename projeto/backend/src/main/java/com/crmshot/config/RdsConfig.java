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
    private static final String SEPARATOR = "============================================================";
    private static final String DEFAULT_PORT = "5432";
    private static final String DEFAULT_DATABASE_EB = "ebdb";
    private static final String DEFAULT_DATABASE = "crmshot";
    private static final String DEFAULT_USERNAME = "postgres";
    
    // Credenciais padrão do RDS (podem ser sobrescritas por variáveis de ambiente)
    private static final String DEFAULT_RDS_HOST = "database-2.cvmowqi02j3c.us-east-2.rds.amazonaws.com";
    private static final String DEFAULT_RDS_PASSWORD = "34367746";

    @Value("${app.rds.host:${AWS_RDS_HOST:${RDS_HOSTNAME:}}}")
    private String awsRdsHost;

    @Value("${app.rds.database:${AWS_RDS_DATABASE:${RDS_DB_NAME:}}}")
    private String awsRdsDatabase;

    @Value("${app.rds.username:${AWS_RDS_USERNAME:${RDS_USERNAME:}}}")
    private String awsRdsUsername;

    @Value("${app.rds.password:${AWS_RDS_PASSWORD:${RDS_PASSWORD:}}}")
    private String awsRdsPassword;

    @Value("${app.rds.port:${AWS_RDS_PORT:${RDS_PORT:5432}}}")
    private String awsRdsPort;


    @Bean
    @Primary
    public DataSource dataSource() {
        logDataSourceStart();
        
        RdsConnectionConfig config = buildRdsConnectionConfig();
        validateConfig(config);
        logConnectionDetails(config);
        
        return createDataSource(config);
    }

    private void logDataSourceStart() {
        logger.info(SEPARATOR);
        logger.info("INICIANDO CONFIGURAÇÃO DO DATASOURCE");
        logger.info(SEPARATOR);
    }

    private RdsConnectionConfig buildRdsConnectionConfig() {
        // 1. Verificar variáveis do Elastic Beanstalk primeiro (RDS_HOSTNAME é automático quando RDS está conectado)
        String rdsHostname = System.getenv("RDS_HOSTNAME");
        
        if (rdsHostname != null && !rdsHostname.isEmpty()) {
            logger.info("RDS_HOSTNAME encontrado: {}", rdsHostname);
            return buildElasticBeanstalkConfig(rdsHostname);
        }
        
        // 2. Usar configuração padrão (valores do código ou variáveis de ambiente se configuradas)
        logger.info("RDS_HOSTNAME não encontrado, usando configuração padrão do código");
        return buildAwsRdsConfig();
    }

    private RdsConnectionConfig buildElasticBeanstalkConfig(String hostname) {
        String port = getEnvOrDefault("RDS_PORT", DEFAULT_PORT);
        String database = getEnvOrDefault("RDS_DB_NAME", DEFAULT_DATABASE_EB);
        String username = getEnvOrDefault("RDS_USERNAME", DEFAULT_USERNAME);
        String password = getEnvOrDefault("RDS_PASSWORD", "");
        
        logger.info("Usando variáveis RDS do Elastic Beanstalk");
        
        return new RdsConnectionConfig(hostname, port, database, username, password);
    }

    private RdsConnectionConfig buildAwsRdsConfig() {
        // Tentar obter valores de variáveis de ambiente ou propriedades, senão usar padrões
        String hostname = getValueOrDefault(awsRdsHost, "AWS_RDS_HOST");
        if (hostname == null || hostname.isEmpty()) {
            hostname = DEFAULT_RDS_HOST;
            logger.info("Usando host padrão do RDS: {}", hostname);
        }
        
        String port = getValueOrDefault(awsRdsPort, "AWS_RDS_PORT");
        port = (port == null || port.isEmpty()) ? DEFAULT_PORT : port;
        
        String database = getValueOrDefault(awsRdsDatabase, "AWS_RDS_DATABASE");
        database = (database == null || database.isEmpty()) ? DEFAULT_DATABASE : database;
        
        String username = getValueOrDefault(awsRdsUsername, "AWS_RDS_USERNAME");
        username = (username == null || username.isEmpty()) ? DEFAULT_USERNAME : username;
        
        String password = getValueOrDefault(awsRdsPassword, "AWS_RDS_PASSWORD");
        if (password == null || password.isEmpty()) {
            password = DEFAULT_RDS_PASSWORD;
            logger.info("Usando senha padrão do RDS");
        }
        
        logger.info("Usando credenciais RDS configuradas (variáveis de ambiente ou padrões do código)");
        
        return new RdsConnectionConfig(hostname, port, database, username, password);
    }

    private String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private String getValueOrDefault(String propertyValue, String envVar) {
        if (propertyValue != null && !propertyValue.isEmpty()) {
            return propertyValue;
        }
        return System.getenv(envVar);
    }

    private void validateConfig(RdsConnectionConfig config) {
        if (config.hostname == null || config.hostname.isEmpty()) {
            logger.error(SEPARATOR);
            logger.error("ERRO: Host do banco de dados não configurado!");
            logger.error(SEPARATOR);
            logger.error("Verifique se as credenciais padrão estão configuradas no código ou");
            logger.error("configure variáveis de ambiente (RDS_HOSTNAME ou AWS_RDS_HOST)");
            logger.error(SEPARATOR);
            throw new IllegalStateException(
                "Host do banco de dados não está configurado. " +
                "Consulte os logs para mais detalhes."
            );
        }
        
        if (config.password == null || config.password.isEmpty()) {
            logger.error(SEPARATOR);
            logger.error("ERRO: Senha do banco de dados não configurada!");
            logger.error(SEPARATOR);
            logger.error("Verifique se a senha padrão está configurada no código ou");
            logger.error("configure variáveis de ambiente (RDS_PASSWORD ou AWS_RDS_PASSWORD)");
            logger.error(SEPARATOR);
            throw new IllegalStateException(
                "Senha do banco de dados não está configurada. " +
                "Consulte os logs para mais detalhes."
            );
        }
    }

    private void logConnectionDetails(RdsConnectionConfig config) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", config.hostname, config.port, config.database);
        
        logger.info("Host: {}", config.hostname);
        logger.info("Port: {}", config.port);
        logger.info("Database: {}", config.database);
        logger.info("Username: {}", config.username);
        logger.info("JDBC URL: {}", jdbcUrl);
        logger.info(SEPARATOR);
    }

    private DataSource createDataSource(RdsConnectionConfig config) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", config.hostname, config.port, config.database);
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(config.username)
                .password(config.password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    private static class RdsConnectionConfig {
        final String hostname;
        final String port;
        final String database;
        final String username;
        final String password;

        RdsConnectionConfig(String hostname, String port, String database, String username, String password) {
            this.hostname = hostname;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
        }
    }
}
