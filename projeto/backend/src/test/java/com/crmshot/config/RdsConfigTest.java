package com.crmshot.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {
    "app.rds.host=test-host",
    "app.rds.password=test-pass",
    "app.rds.port=5432",
    "app.rds.database=testdb",
    "app.rds.username=testuser"
})
class RdsConfigTest {

    @Test
    void testRdsConfig_UsesDefaultHost_WhenHostnameMissing() {

        RdsConfig rdsConfig = new RdsConfig();
        ReflectionTestUtils.setField(rdsConfig, "awsRdsHost", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPassword", "test-pass");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPort", "5432");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsDatabase", "testdb");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsUsername", "testuser");


        DataSource dataSource = rdsConfig.dataSource();
        assertNotNull(dataSource);
    }

    @Test
    void testRdsConfig_UsesDefaultPassword_WhenPasswordMissing() {

        RdsConfig rdsConfig = new RdsConfig();
        ReflectionTestUtils.setField(rdsConfig, "awsRdsHost", "test-host");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPassword", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPort", "5432");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsDatabase", "testdb");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsUsername", "testuser");


        DataSource dataSource = rdsConfig.dataSource();
        assertNotNull(dataSource);
    }

    @Test
    void testRdsConfig_WithValidConfig() {
        RdsConfig rdsConfig = new RdsConfig();
        ReflectionTestUtils.setField(rdsConfig, "awsRdsHost", "test-host");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPassword", "test-pass");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPort", "5432");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsDatabase", "testdb");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsUsername", "testuser");

        DataSource dataSource = rdsConfig.dataSource();

        assertNotNull(dataSource);
    }

    @Test
    void testRdsConfig_WithDefaultValues() {
        RdsConfig rdsConfig = new RdsConfig();
        ReflectionTestUtils.setField(rdsConfig, "awsRdsHost", "test-host");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPassword", "test-pass");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPort", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsDatabase", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsUsername", "");

        DataSource dataSource = rdsConfig.dataSource();

        assertNotNull(dataSource);
    }

    @Test
    void testRdsConfig_WithAllDefaults() {

        RdsConfig rdsConfig = new RdsConfig();
        ReflectionTestUtils.setField(rdsConfig, "awsRdsHost", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPassword", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsPort", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsDatabase", "");
        ReflectionTestUtils.setField(rdsConfig, "awsRdsUsername", "");


        DataSource dataSource = rdsConfig.dataSource();
        assertNotNull(dataSource);
    }
}

