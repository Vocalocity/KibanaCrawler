package com.vonage.kibana_crawler.pojo.kibana_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Source {

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("fqdn")
    private String fqdn;

    @JsonProperty("namespace_name")
    private String namespace;

    @JsonProperty("pod_name")
    private String podName;

    @JsonProperty("pod_id")
    private String podId;
}
