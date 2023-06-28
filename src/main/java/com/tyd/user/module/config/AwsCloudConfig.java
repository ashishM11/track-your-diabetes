package com.tyd.user.module.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.tyd.user.module.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Profile("aws")
public class AwsCloudConfig {

    private final Utils utils;
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${spring.aws.secretsmanager.endpoint}")
    private String endpoint;

    @Value("${spring.aws.secretsmanager.region}")
    private String region;

    @Value("${aws.secrets.rds}")
    private String rdsSecret;

    @Value("${aws.secrets.mailing}")
    private String mailSecret;

    public AwsCloudConfig(Utils utils) {
        this.utils = utils;
    }

    @Bean
    @Profile("aws")
    public AWSSecretsManager awsSecretsManager() {
        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        return clientBuilder.build();
    }

    @Bean("rdsSecretValueRequest")
    @Profile("aws")
    public GetSecretValueRequest rdsSecretValueRequest() {
        return new GetSecretValueRequest().withSecretId(rdsSecret);
    }

    @Bean("mailSecretValueRequest")
    @Profile("aws")
    public GetSecretValueRequest mailSecretValueRequest() {
        return new GetSecretValueRequest().withSecretId(mailSecret);
    }

    @Bean("rdsSecretValueResult")
    @Profile("aws")
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

    @Bean("mailSecretValueResult")
    @Profile("aws")
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



    @Bean
    @Profile("aws")
    public JavaMailSender mailSender() {
        JsonNode secretsJson = utils.getJsonNode(mailSecretValueResult());
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
}
