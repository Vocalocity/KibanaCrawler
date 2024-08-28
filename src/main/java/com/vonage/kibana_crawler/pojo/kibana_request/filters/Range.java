package com.vonage.kibana_crawler.pojo.kibana_request.filters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vonage.kibana_crawler.deserializers.RangeDeserializer;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonDeserialize(using = RangeDeserializer.class)
@AllArgsConstructor
@Builder
public class Range implements Filter {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RangeParams {

        private String gte;

        private String lte;

        private String format;
    }

    private Map<String, RangeParams> range;
}
