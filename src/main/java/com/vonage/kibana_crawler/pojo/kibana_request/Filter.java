package com.vonage.kibana_crawler.pojo.kibana_request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vonage.kibana_crawler.deserializers.FilterDeserializer;

/**
 * Just a marker interface for all different filter types, <br/>
 * 1.   'multi_match'<br/>
 * 2.   'match_phrase'<br/>
 * 3.   'range'<br/>
 */
@JsonDeserialize(using = FilterDeserializer.class)
public interface Filter {
}