package com.vonage.kibana_crawler.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.pojo.kibana_request.FilterType;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.Range;

import java.io.IOException;
import java.util.Map;

public class RangeDeserializer extends JsonDeserializer<Range> {

    @Override
    public Range deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = CrawlerConstants.MAPPER.readTree(parser);
        Range range = new Range();
        range.setRange(
                CrawlerConstants.MAPPER.readValue(node.get(FilterType.RANGE.getJsonProperty()).toString(),
                        new TypeReference<Map<String, Range.RangeParams>>() {}
        ));
        return range;
    }
}
