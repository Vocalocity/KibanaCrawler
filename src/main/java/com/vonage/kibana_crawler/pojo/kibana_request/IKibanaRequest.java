package com.vonage.kibana_crawler.pojo.kibana_request;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IKibanaRequest {

    String getJSONRequest() throws JsonProcessingException;

    void addSearchAfter(List<Object> searchAfter);

}
