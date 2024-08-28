package com.vonage.kibana_crawler.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.pojo.kibana_request.FilterType;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MultiMatch;

import java.io.IOException;

public class MultiMatchDeserializer extends JsonDeserializer<MultiMatch> {

    @Override
    public MultiMatch deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = CrawlerConstants.MAPPER.readTree(parser);
        MultiMatch multiMatch = new MultiMatch();
        multiMatch.setMultiMatch(CrawlerConstants.MAPPER.readValue(
                node.get(FilterType.MULTI_MATCH.getJsonProperty()).toString(),
                MultiMatch.MultiMatchKeys.class
        ));
        return multiMatch;
    }
}
