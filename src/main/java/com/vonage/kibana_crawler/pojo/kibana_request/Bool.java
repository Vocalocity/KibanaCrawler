package com.vonage.kibana_crawler.pojo.kibana_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Bool {

    @JsonProperty("minimum_should_match")
    private Integer minimumShouldMatch;

    private List<? extends Filter> should;

    /**
     * Example, <br/>
     * <pre>
     *     "filter": [
     *                         {
     *                             "multi_match": {
     *                                 "type": "phrase",
     *                                 "query": "regular login request have succeed for user",
     *                                 "lenient": true
     *                             }
     *                         }
     *               ]
     * </pre>
     */
    private List<? extends Filter> filter;

    @JsonProperty("must_not")
    private List<? extends Filter> mustNot;
}
