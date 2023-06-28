package com.tyd.user.module.utils;

import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@AllArgsConstructor
public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private final ObjectMapper objectMapper;

    public JsonNode getJsonNode(GetSecretValueResult getSecretValueResponse){
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
}
