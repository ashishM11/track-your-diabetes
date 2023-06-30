package com.tyd.user.module.config;

import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.tyd.user.module.utils.Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@AllArgsConstructor
@Profile("aws")
public class AwsDataSourceConfiguration {

    private final Utils utils;
    private final GetSecretValueResult rdsSecretValueResult;

    @Bean
    public HikariConfig hikariConfig() {
        JsonNode secretsJson = utils.getJsonNode(rdsSecretValueResult);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://tyd-db-instance.cpai3a5patud.ap-south-1.rds.amazonaws.com:5432/track_your_diabetes");
        assert secretsJson != null;
        hikariConfig.setUsername(secretsJson.get("username").textValue());
        hikariConfig.setPassword(secretsJson.get("password").textValue());
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.addDataSourceProperty("autoCommit", true);
        hikariConfig.addDataSourceProperty("connectionTimeout", 2000);
        hikariConfig.addDataSourceProperty("idleTimeout", 10000);
        hikariConfig.addDataSourceProperty("maxLifetime", 1000000);
        hikariConfig.addDataSourceProperty("maximumPoolSize", 10);
        hikariConfig.addDataSourceProperty("poolName", "tyd-user-module-cp");
        hikariConfig.addDataSourceProperty("readOnly", false);
        hikariConfig.addDataSourceProperty("transactionIsolation", true);
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        return hikariConfig;
    }

    @Bean
    public HikariDataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }


}
