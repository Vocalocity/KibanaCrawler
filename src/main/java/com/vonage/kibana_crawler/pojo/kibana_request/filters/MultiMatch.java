package com.vonage.kibana_crawler.pojo.kibana_request.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vonage.kibana_crawler.deserializers.MultiMatchDeserializer;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = MultiMatchDeserializer.class)
@Builder
public class MultiMatch implements Filter {

    @JsonProperty("multi_match")
    private MultiMatchKeys multiMatch;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MultiMatchKeys{
        private String type;

        private String query;

        private Boolean lenient = true;

        public String getQuery() {
            if(StringUtils.isEmpty(query)){
                throw new InvalidParameterException("'query' is required.");
            }
            return query;
        }
    }
}
