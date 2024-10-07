package com.vonage.kibana_crawler.pojo.kibana_request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class KibanaRequest {

    private Params params;

    private String rawRequest;

    public static KibanaRequest fromJson(String json) {
        KibanaRequest request = new KibanaRequest();
        request.setRawRequest(json);
        return request;
    }

    @Override
    public String toString() {
        if(Objects.nonNull(rawRequest) && !rawRequest.isEmpty()){
            return rawRequest;
        }
        try {
            return CrawlerConstants.MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
