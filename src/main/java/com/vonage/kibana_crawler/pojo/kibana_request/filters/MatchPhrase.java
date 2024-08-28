package com.vonage.kibana_crawler.pojo.kibana_request.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vonage.kibana_crawler.deserializers.MatchPhraseDeserializer;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = MatchPhraseDeserializer.class)
@Builder
public class MatchPhrase implements Filter {

    @JsonProperty("match_phrase")
    Map<String, Object> matchPhrase;
}
