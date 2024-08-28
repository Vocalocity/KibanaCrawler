package com.vonage.kibana_crawler.mappers;

import com.vonage.kibana_crawler.pojo.DefaultKibanaRequest;
import com.vonage.kibana_crawler.utilities.KibanaRequestHelper;
import com.vonage.kibana_crawler.pojo.AppCustomizedKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.KibanaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class AppCustomizedKibanaRequestToKibanaRequestMapper {

    public KibanaRequest toKibanaRequest(AppCustomizedKibanaRequest request) {
        KibanaRequest defaultKibanaRequest = DefaultKibanaRequest.getInstance();
        if(Objects.isNull(defaultKibanaRequest)){
            log.error("Cannot proceed further. Unable to find default kibana request.");
        }
        defaultKibanaRequest.getParams().setIndex(request.getIndex());
        defaultKibanaRequest.getParams().getBody().setSize(request.getSize());
        defaultKibanaRequest.getParams().getBody().setSearchAfter(request.getSearchAfter());
        KibanaRequestHelper.getBool(defaultKibanaRequest).setMustNot(request.getMustNot());
        KibanaRequestHelper.getBool(defaultKibanaRequest).setFilter(request.getFilters());
        KibanaRequestHelper.getBool(defaultKibanaRequest).setShould(request.getShould());
        KibanaRequestHelper.getBool(defaultKibanaRequest).setMinimumShouldMatch(request.getMinimumShouldMatch());
        return defaultKibanaRequest;
    }
}
