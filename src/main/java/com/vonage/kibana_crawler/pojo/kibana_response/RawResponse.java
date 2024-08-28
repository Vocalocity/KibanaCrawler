package com.vonage.kibana_crawler.pojo.kibana_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawResponse {

    @JsonProperty("hits")
    private Hits hits;

}
