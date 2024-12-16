package com.vonage.kibana_crawler.pojo.kibana_request.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vonage.kibana_crawler.mappers.AppCustomizedKibanaRequestToKibanaRequestMapper;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import com.vonage.kibana_crawler.pojo.kibana_request.IKibanaRequest;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppCustomizedKibanaRequest implements IKibanaRequest {

    private String index;

    private Integer size;

    private List<Filter> filters;

    private List<Filter> mustNot;

    private List<Filter> should;

    private List<Object> searchAfter;

    private boolean pagination;

    public int getMinimumShouldMatch(){
        return CollectionUtils.isEmpty(should) ? 0 : 1;
    }

    @Override
    public String getJSONRequest() throws JsonProcessingException {
        return CrawlerConstants.MAPPER.writeValueAsString(AppCustomizedKibanaRequestToKibanaRequestMapper.toKibanaRequest(this));
    }

    @Override
    public void addSearchAfter(List<Object> searchAfter) {

    }
}
