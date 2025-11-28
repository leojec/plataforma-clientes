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
        String rdsHostname = System.getenv("RDS_HOSTNAME");
        
        if (rdsHostname != null && !rdsHostname.isEmpty()) {
            return buildElasticBeanstalkConfig(rdsHostname);
        } else {
            return buildAwsRdsConfig();
        }
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
        String hostname = getValueOrDefault(awsRdsHost, "AWS_RDS_HOST");
        String port = getValueOrDefault(awsRdsPort, "AWS_RDS_PORT");
        String database = getValueOrDefault(awsRdsDatabase, "AWS_RDS_DATABASE");
        String username = getValueOrDefault(awsRdsUsername, "AWS_RDS_USERNAME");
        String password = getValueOrDefault(awsRdsPassword, "AWS_RDS_PASSWORD");
        
        port = (port == null || port.isEmpty()) ? DEFAULT_PORT : port;
        database = (database == null || database.isEmpty()) ? DEFAULT_DATABASE : database;
        username = (username == null || username.isEmpty()) ? DEFAULT_USERNAME : username;
        
        logger.info("Usando credenciais RDS configuradas via variáveis de ambiente/propriedades");
        
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
            logger.error("AWS_RDS_HOST não configurado! Configure a variável de ambiente.");
            throw new IllegalStateException("AWS_RDS_HOST não está configurado. Configure a variável de ambiente AWS_RDS_HOST.");
        }
        
        if (config.password == null || config.password.isEmpty()) {
            logger.error("AWS_RDS_PASSWORD não configurado! Configure a variável de ambiente.");
            throw new IllegalStateException("AWS_RDS_PASSWORD não está configurado. Configure a variável de ambiente AWS_RDS_PASSWORD.");
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

