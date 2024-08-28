package com.vonage.kibana_crawler.pojo.kibana_request;

import com.vonage.kibana_crawler.pojo.kibana_request.filters.MatchPhrase;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MultiMatch;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.Range;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterType {

    MULTI_MATCH("multi_match", MultiMatch.class),
    MATCH_PHRASE("match_phrase", MatchPhrase.class),
    RANGE("range", Range.class),;

    private final String jsonProperty;

    private final Class<? extends Filter> deserilizingClass;

}
