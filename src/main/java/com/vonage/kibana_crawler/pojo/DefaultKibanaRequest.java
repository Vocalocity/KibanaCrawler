package com.vonage.kibana_crawler.pojo;

import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.pojo.kibana_request.KibanaRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class DefaultKibanaRequest {

    private static URL DEFAULT_REQUEST_JSON = null;

    private DefaultKibanaRequest() {}

    public static KibanaRequest getInstance() {
        KibanaRequest kibanaRequest = null;
        try {
            if (Objects.isNull(DEFAULT_REQUEST_JSON)) {
                DEFAULT_REQUEST_JSON =  DefaultKibanaRequest.class.getResource(CrawlerConstants.DEFAULT_KIBANA_REQUEST_PATH);
            }
            kibanaRequest = CrawlerConstants.MAPPER.readValue(DEFAULT_REQUEST_JSON, KibanaRequest.class);
        } catch (IOException e) {
            log.info("Failed to read default kibana request. ERROR: {}", e.getMessage());
        }
        return kibanaRequest;
    }
}
