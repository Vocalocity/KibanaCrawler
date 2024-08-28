package com.vonage.kibana_crawler.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import com.vonage.kibana_crawler.pojo.kibana_request.FilterType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class FilterDeserializer extends JsonDeserializer<Filter> {

    @Override
    public Filter deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = CrawlerConstants.MAPPER.readTree(jsonParser);
        if(Objects.isNull(node)){
            return null;
        }
        for(FilterType filterType : FilterType.values()) {
            if(node.has(filterType.getJsonProperty())) {
                return CrawlerConstants.MAPPER.readValue(node.toString(), filterType.getDeserilizingClass());
            }
        }
        log.info("Node {} not found in permitted filters", node);
        return null;
    }
}
