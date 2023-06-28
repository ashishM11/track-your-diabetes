package com.tyd.user.module.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

@Configuration
@Profile("aws")
public class AwsDataSourceConfiguration {

    private final ObjectMapper objectMapper;

    @Value("${spring.aws.secretsmanager.endpoint}")
    private String endpoint;

    @Value("${spring.aws.secretsmanager.region}")
    private String region;

    @Value("${aws.secrets.rds}")
    private String rdsSecret;

    @Value("${aws.secrets.mailing}")
    private String mailSecret;

    private static final Logger log = LoggerFactory.getLogger(AwsDataSourceConfiguration.class);

    public AwsDataSourceConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Bean
    public AWSSecretsManager awsSecretsManager() {
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        return clientBuilder.build();
    }

    @Bean
    public GetSecretValueRequest rdsSecretValueRequest() {
        return new GetSecretValueRequest().withSecretId(rdsSecret);
    }

    @Bean
    public GetSecretValueRequest mailSecretValueRequest() {
        return new GetSecretValueRequest().withSecretId(mailSecret);
    }

    @Bean
    public GetSecretValueResult rdsSecretValueResult() {
        try {
            return awsSecretsManager().getSecretValue(rdsSecretValueRequest());
        } catch (ResourceNotFoundException e) {
            log.error("The requested secret " + rdsSecret + " was not found");
        } catch (InvalidRequestException e) {
            log.error("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            log.error("The request had invalid params: " + e.getMessage());
        }
        return null;
    }

    @Bean
    public GetSecretValueResult mailSecretValueResult() {
        try {
            return awsSecretsManager().getSecretValue(mailSecretValueRequest());
        } catch (ResourceNotFoundException e) {
            log.error("The requested secret " + mailSecret + " was not found");
        } catch (InvalidRequestException e) {
            log.error("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            log.error("The request had invalid params: " + e.getMessage());
        }
        return null;
    }

    private JsonNode getJsonNode(GetSecretValueResult getSecretValueResponse){
        String secret = getSecretValueResponse.getSecretString();
        if (secret == null) {
            log.error("The Secret String returned is null");
            return null;
        }
        try {
            return objectMapper.readTree(secret);
        } catch (IOException e) {
            log.error("Exception while retreiving secret values: " + e.getMessage());
        }
        return null;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JsonNode secretsJson = getJsonNode(mailSecretValueResult());
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        assert secretsJson != null;
        mailSender.setUsername(secretsJson.get("tyd-gmail-id").textValue());
        mailSender.setPassword(secretsJson.get("tyd-gmail-password").textValue());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

    @Bean
    public HikariConfig hikariConfig() {
        JsonNode secretsJson = getJsonNode(rdsSecretValueResult());
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
