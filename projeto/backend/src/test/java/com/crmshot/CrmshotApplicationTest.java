package com.crmshot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@SpringBootTest
@ActiveProfiles("test")
class CrmshotApplicationTest {

    @Test
    void contextLoads() {
        // Teste básico para verificar se o contexto Spring carrega corretamente
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public DataSource testDataSource() {
            org.springframework.boot.jdbc.DataSourceBuilder<?> builder = 
                org.springframework.boot.jdbc.DataSourceBuilder.create();
            builder.url("jdbc:h2:mem:testdb");
            builder.driverClassName("org.h2.Driver");
            builder.username("sa");
            builder.password("");
            return builder.build();
        }
    }
}

