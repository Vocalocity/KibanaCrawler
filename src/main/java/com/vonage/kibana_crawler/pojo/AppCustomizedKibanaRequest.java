package com.vonage.kibana_crawler.pojo;

import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppCustomizedKibanaRequest {

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
}
