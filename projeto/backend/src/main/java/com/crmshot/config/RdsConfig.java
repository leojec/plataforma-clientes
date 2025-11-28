package com.crmshot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class RdsConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        String rdsHostname = System.getenv("RDS_HOSTNAME");
        
        // Se RDS_HOSTNAME estiver disponível, usar configuração RDS do Elastic Beanstalk
        if (rdsHostname != null && !rdsHostname.isEmpty()) {
            String rdsPort = System.getenv("RDS_PORT");
            String rdsDbName = System.getenv("RDS_DB_NAME");
            String rdsUsername = System.getenv("RDS_USERNAME");
            String rdsPassword = System.getenv("RDS_PASSWORD");
            
            String port = (rdsPort != null && !rdsPort.isEmpty()) ? rdsPort : "5432";
            String dbName = (rdsDbName != null && !rdsDbName.isEmpty()) ? rdsDbName : "crmshot";
            String username = (rdsUsername != null && !rdsUsername.isEmpty()) ? rdsUsername : "postgres";
            String password = (rdsPassword != null && !rdsPassword.isEmpty()) ? rdsPassword : "";
            
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", rdsHostname, port, dbName);
            
            System.out.println("🔗 Conectando ao RDS: " + rdsHostname + ":" + port + "/" + dbName);
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
        
        // Se não estiver no RDS, usar configuração padrão do application.yml
        String url = env.getProperty("spring.datasource.url", "jdbc:postgresql://localhost:5432/crmshot");
        String username = env.getProperty("spring.datasource.username", "postgres");
        String password = env.getProperty("spring.datasource.password", "34367746");
        
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

