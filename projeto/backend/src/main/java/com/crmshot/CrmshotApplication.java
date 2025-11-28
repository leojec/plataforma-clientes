package com.crmshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CrmshotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmshotApplication.class, args);
    }

}
