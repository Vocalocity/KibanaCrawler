package com.vonage.kibana_crawler.extractors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;

import java.util.Map;

final public class MessageExtractors {

    public static Map<String, String> getRequestLogMap(String requestLog){
        String[] split = requestLog.split("\\[com.vocalocity.hdap.logging.RequestLogFilter]");
        if(split.length < 2){
            throw new IllegalArgumentException("Invalid request log format: " + requestLog);
        }
        try {
            return CrawlerConstants.MAPPER.readValue(split[1].trim(), new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
