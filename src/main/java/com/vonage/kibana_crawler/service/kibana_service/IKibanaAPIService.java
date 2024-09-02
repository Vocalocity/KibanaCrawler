package com.vonage.kibana_crawler.service.kibana_service;

import com.vonage.kibana_crawler.aspects.annotation.ExecutionTime;
import com.vonage.kibana_crawler.pojo.AppCustomizedKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_response.KibanaResponse;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public interface IKibanaAPIService {

    KibanaResponse sendRequest(AppCustomizedKibanaRequest request);

    void sendRequest(AppCustomizedKibanaRequest customizedKibanaRequest, Queue<KibanaResponse> container);
}
