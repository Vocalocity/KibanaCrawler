package com.vonage.kibana_crawler.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.pojo.kibana_request.FilterType;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MatchPhrase;

import java.io.IOException;
import java.util.Map;

public class MatchPhraseDeserializer extends JsonDeserializer<MatchPhrase> {

    @Override
    public MatchPhrase deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = CrawlerConstants.MAPPER.readTree(parser);
        MatchPhrase matchPhrase = new MatchPhrase();
        matchPhrase.setMatchPhrase(CrawlerConstants.MAPPER.readValue(
                node.get(FilterType.MATCH_PHRASE.getJsonProperty()).toString(),
                new TypeReference<Map<String, Object>>() {}));
        return matchPhrase;
    }
}