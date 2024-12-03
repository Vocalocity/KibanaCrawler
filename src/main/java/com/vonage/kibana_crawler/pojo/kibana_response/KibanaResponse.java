package com.vonage.kibana_crawler.pojo.kibana_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KibanaResponse {

    private ResponseType responseType;

    @JsonProperty("rawResponse")
    private RawResponse rawResponse;

    public enum ResponseType {
        VALID,
        INVALID,
        UNAUTHORIZED,
    }

    public static KibanaResponse invalidResponse(){
        return new KibanaResponse(ResponseType.INVALID, null);
    }

    public static KibanaResponse unauthorizedResponse(){
        return new KibanaResponse(ResponseType.UNAUTHORIZED, null);
    }
}
