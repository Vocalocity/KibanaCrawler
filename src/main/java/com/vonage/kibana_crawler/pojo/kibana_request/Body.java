package com.vonage.kibana_crawler.pojo.kibana_request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Body {

    /**
     * List of sorting parameters. For example, <br/>
     * <pre>
     *     [
     *       {
     *         "timestamp": {
     *            "order": "desc",
     *            "unmapped_type": "boolean"
     *          }
     *       }
     *    ]
     * </pre>
     */
    private List<Map<String, Map<String, String>>> sort;

    private Integer size;

    private Boolean version;

    /**
     * Example, <br/>
     * <pre>
     *     "docvalue_fields": [
     *                 {
     *                     "field": "@timestamp",
     *                     "format": "date_time"
     *                 },
     *                 ...,
     *                 ...,
     *                 ...
     *              ]
     * </pre>
     */
    @JsonProperty("docvalue_fields")
    private List<Map<String, String>> docvalueFields;

    private Query query;

    @JsonProperty("search_after")
    private List<Object> searchAfter;

}
