package com.vonage.kibana_crawler.pojo.kibana_request.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vonage.kibana_crawler.pojo.kibana_request.IKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.Params;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class KibanaRequest implements IKibanaRequest {

    private Params params;

    @Override
    public String getJSONRequest() throws JsonProcessingException {
        return CrawlerConstants.MAPPER.writeValueAsString(this);
    }

    @Override
    public void addSearchAfter(List<Object> searchAfter) {
        this.params.getBody().setSearchAfter(searchAfter);
    }
}
