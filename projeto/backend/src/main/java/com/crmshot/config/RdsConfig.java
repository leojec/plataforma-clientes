package com.crmshot.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class RdsConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        System.out.println("=".repeat(60));
        System.out.println("🔍 INICIANDO CONFIGURAÇÃO DO DATASOURCE");
        System.out.println("=".repeat(60));
        
        // Verificar se as variáveis RDS estão disponíveis (Elastic Beanstalk)
        String rdsHostname = System.getenv("RDS_HOSTNAME");
        
        // Credenciais do RDS AWS configuradas no código
        String awsRdsHost = "database-2.cvmowqi02j3c.us-east-2.rds.amazonaws.com";
        String awsRdsDatabase = "crmshot";
        String awsRdsUsername = "postgres";
        String awsRdsPassword = "34367746";
        String awsRdsPort = "5432";
        
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
            
            System.out.println("✅ Usando variáveis RDS do Elastic Beanstalk");
        } else {
            // Usar credenciais RDS configuradas no código
            finalHostname = awsRdsHost;
            finalPort = awsRdsPort;
            finalDatabase = awsRdsDatabase;
            finalUsername = awsRdsUsername;
            finalPassword = awsRdsPassword;
            
            System.out.println("✅ Usando credenciais RDS configuradas no código");
        }
        
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", finalHostname, finalPort, finalDatabase);
        
        System.out.println("   Host: " + finalHostname);
        System.out.println("   Port: " + finalPort);
        System.out.println("   Database: " + finalDatabase);
        System.out.println("   Username: " + finalUsername);
        System.out.println("   JDBC URL: " + jdbcUrl);
        System.out.println("=".repeat(60));
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(finalUsername)
                .password(finalPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

