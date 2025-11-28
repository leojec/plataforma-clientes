package com.crmshot.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
public class RdsConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(@org.springframework.lang.NonNull ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        
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
            
            System.out.println("🔗 Usando variáveis RDS do Elastic Beanstalk");
        } else {
            // Usar credenciais RDS configuradas no código
            finalHostname = awsRdsHost;
            finalPort = awsRdsPort;
            finalDatabase = awsRdsDatabase;
            finalUsername = awsRdsUsername;
            finalPassword = awsRdsPassword;
            
            System.out.println("🔗 Usando credenciais RDS configuradas no código");
        }
        
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", finalHostname, finalPort, finalDatabase);
        
        System.out.println("   URL: " + jdbcUrl);
        System.out.println("   Database: " + finalDatabase);
        System.out.println("   Username: " + finalUsername);
        
        // Adicionar propriedades RDS ao Environment antes do Spring criar o DataSource
        Map<String, Object> rdsProperties = new HashMap<>();
        rdsProperties.put("spring.datasource.url", jdbcUrl);
        rdsProperties.put("spring.datasource.username", finalUsername);
        rdsProperties.put("spring.datasource.password", finalPassword);
        
        environment.getPropertySources().addFirst(
            new MapPropertySource("rds-config", rdsProperties)
        );
    }
}

