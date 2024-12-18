package com.vonage.kibana_crawler.mappers;

import com.vonage.kibana_crawler.pojo.DefaultKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.Bool;
import com.vonage.kibana_crawler.utilities.KibanaRequestHelper;
import com.vonage.kibana_crawler.pojo.kibana_request.impl.AppCustomizedKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.impl.KibanaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class AppCustomizedKibanaRequestToKibanaRequestMapper {

    public static KibanaRequest toKibanaRequest(AppCustomizedKibanaRequest request) {
        KibanaRequest defaultKibanaRequest = DefaultKibanaRequest.getInstance();
        if(Objects.isNull(defaultKibanaRequest)){
            log.error("Cannot proceed further. Unable to find default kibana request.");
        }
        defaultKibanaRequest.getParams().setIndex(request.getIndex());
        defaultKibanaRequest.getParams().getBody().setSize(request.getSize());
        defaultKibanaRequest.getParams().getBody().setSearchAfter(request.getSearchAfter());
        Bool bool = KibanaRequestHelper.getBool(defaultKibanaRequest);
        bool.setMustNot(request.getMustNot());
        bool.setFilter(request.getFilters());
        bool.setShould(request.getShould());
        bool.setMinimumShouldMatch(request.getMinimumShouldMatch());
        return defaultKibanaRequest;
    }
}
