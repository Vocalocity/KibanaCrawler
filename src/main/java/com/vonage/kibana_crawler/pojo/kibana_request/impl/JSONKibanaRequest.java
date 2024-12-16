package com.vonage.kibana_crawler.pojo.kibana_request.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vonage.kibana_crawler.pojo.kibana_request.IKibanaRequest;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JSONKibanaRequest implements IKibanaRequest {

    private final String jsonRequest;

    JSONKibanaRequest(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    JSONKibanaRequest(File requestFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(requestFile));
        String line;
        StringBuilder requestBuilder = new StringBuilder();
        while((line = reader.readLine()) != null) {
            requestBuilder.append(line);
        }
        this.jsonRequest = requestBuilder.toString();
    }

    @Override
    public String getJSONRequest() {
        return jsonRequest;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addSearchAfter(List<Object> searchAfter) {
        try {
            Map<String, Object> requestMap = CrawlerConstants.MAPPER.readValue(jsonRequest, new TypeReference<Map<String, Object>>() {});
            ((Map<String, Object>)((Map<String, Object>)requestMap.get("params")).get("body")).put("searchAfter", searchAfter);
        } catch (Exception e) {
            log.error("Failed to add search after. Message {}", e.getMessage());
        }
    }
}
