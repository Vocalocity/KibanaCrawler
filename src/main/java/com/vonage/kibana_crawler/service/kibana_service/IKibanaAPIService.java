package com.vonage.kibana_crawler.service.kibana_service;

import com.vonage.kibana_crawler.aspects.annotation.ExecutionTime;
import com.vonage.kibana_crawler.pojo.kibana_request.IKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.impl.KibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_response.KibanaResponse;

import java.util.Queue;

public interface IKibanaAPIService {
    KibanaResponse sendRequest(IKibanaRequest request);

    @ExecutionTime
    void sendRequest(IKibanaRequest kibanaRequest, Queue<KibanaResponse> container);
}
