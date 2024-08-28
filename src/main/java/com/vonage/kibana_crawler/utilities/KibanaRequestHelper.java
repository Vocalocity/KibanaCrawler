package com.vonage.kibana_crawler.utilities;

import com.vonage.kibana_crawler.pojo.kibana_request.Bool;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import com.vonage.kibana_crawler.pojo.kibana_request.KibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MatchPhrase;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MultiMatch;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.Range;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KibanaRequestHelper {

    public static String getQuery(KibanaRequest kibanaRequest) {
        return kibanaRequest
                .getParams()
                .getBody()
                .getQuery()
                .getBool()
                .getFilter().stream().filter(filter -> filter instanceof MultiMatch)
                .map(multiMatch -> "\"" + ((MultiMatch) multiMatch).getMultiMatch().getQuery() + "\"")
                .collect(Collectors.joining(" and ")) + " " +
                kibanaRequest
                .getParams()
                .getBody()
                .getQuery()
                .getBool()
                .getShould()
                .stream()
                .filter(should -> should instanceof MultiMatch)
                .map(should -> "\"" + ((MultiMatch) should).getMultiMatch().getQuery() + "\"")
                .collect(Collectors.joining(" or "));
    }

    public static Pair<String, String> getRange(KibanaRequest kibanaRequest, String rangeKey) {
        return kibanaRequest
                .getParams()
                .getBody()
                .getQuery()
                .getBool()
                .getFilter()
                .stream()
                .filter(filter -> filter instanceof Range)
                .filter(range -> ((Range) range).getRange().containsKey(rangeKey))
                .limit(1)
                .map(range -> ((Range) range).getRange().get(rangeKey))
                .map(rangeParams -> new MutablePair<>(rangeParams.getGte(), rangeParams.getLte()))
                .collect(Collectors.toList())
                .get(0);
    }

    public static Bool getBool(KibanaRequest kibanaRequest) {
        return kibanaRequest
                .getParams()
                .getBody()
                .getQuery()
                .getBool();
    }

    public static MultiMatch createMultiMatch(String searchParam) {
        return MultiMatch.builder()
                .multiMatch(MultiMatch.MultiMatchKeys
                        .builder()
                        .query(searchParam)
                        .type("phrase")
                        .lenient(true)
                        .build())
                .build();
    }

    public static Filter createRange(String type, String gte, String lte) {
        return Range.builder()
                .range(new HashMap<String, Range.RangeParams>(){
                    {
                        put(type, Range.RangeParams.builder()
                                .gte(gte)
                                .lte(lte)
                                .format("strict_date_optional_time")
                                .build());
                    }
                }).build();
    }

    @SafeVarargs
    public static MatchPhrase createMatchPhrase(Pair<String, Object>... pairs) {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(pairs).forEach(pair -> map.put(pair.getKey(), pair.getValue()));
        return MatchPhrase.builder()
                .matchPhrase(map)
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getFilter(KibanaRequest kibanaRequest, Class<? extends Filter> filterClazz){
        return (List<T>) getBool(kibanaRequest).getFilter()
                .stream().filter(filter -> filter.getClass().equals(filterClazz)).collect(Collectors.toList());
    }
}
