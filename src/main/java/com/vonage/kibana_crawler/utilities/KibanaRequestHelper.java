package com.vonage.kibana_crawler.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vonage.kibana_crawler.pojo.kibana_request.Bool;
import com.vonage.kibana_crawler.pojo.kibana_request.Filter;
import com.vonage.kibana_crawler.pojo.kibana_request.IKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.impl.KibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MatchPhrase;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.MultiMatch;
import com.vonage.kibana_crawler.pojo.kibana_request.filters.Range;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class KibanaRequestHelper {

    public static String getQuery(IKibanaRequest kibanaRequest) {
        KibanaRequest convertedRequest;
        try {
            convertedRequest = CrawlerConstants.MAPPER.readValue(kibanaRequest.getJSONRequest(), KibanaRequest.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse to 'Kibana Request'");
            throw new RuntimeException(e);
        }
        String query = "";
        try{
            query = convertedRequest
                    .getParams()
                    .getBody()
                    .getQuery()
                    .getBool()
                    .getFilter().stream().filter(filter -> filter instanceof MultiMatch)
                    .map(multiMatch -> "\"" + ((MultiMatch) multiMatch).getMultiMatch().getQuery() + "\"")
                    .collect(Collectors.joining(" and ")) + " " +
                    convertedRequest
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
        catch (Exception e) {
            log.error("Error getting query.");
        }
        return query;
    }

    /**
     * This is an expensive method as it first gets JSON request and then converts it to {@link KibanaRequest}. So refrain from using this continuously in same method as it make it slower.<br></br>
     *
     * <strong>Not recommended.</strong><br></br>
     *<pre>
     *     public void foo(){
     *         KibanaRequestHelper.getRange.setShould(should)
     *         KibanaRequestHelper.getRange.setFilter(filterA)
     *         KibanaRequestHelper.getRange.setFilter(filterB)
     *     }
     *</pre>
     *
     * <strong>Recommended.</strong> <br></br>
     * <pre>
     *    public void foo(){
     *         Bool bool = KibanaRequestHelper.getRange(req);
     *         bool.setShould(should)
     *         bool.setFilter(filterA)
     *         bool.setFilter(filterB)
     *      }
     * </pre>
     */
    public static Pair<String, String> getRange(IKibanaRequest kibanaRequest, String rangeKey) {
        Pair<String, String> rangePair = new MutablePair<>("", "");
        try{
            rangePair = CrawlerConstants.MAPPER.readValue(kibanaRequest.getJSONRequest(), KibanaRequest.class)
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
        } catch (Exception e){
            log.error("Error getting range.");
        }
        return rangePair;
    }

    /**
     * This is an expensive method as it first gets JSON request and then converts it to {@link KibanaRequest}. So refrain from using this continuously in same method as it make it slower.<br></br>
     *
     * <strong>Not recommended.</strong><br></br>
     *<pre>
     *     public void foo(){
     *         KibanaRequestHelper.getBool(req).setShould(should)
     *         KibanaRequestHelper.getBool(req).setFilter(filterA)
     *         KibanaRequestHelper.getBool(req).setFilter(filterB)
     *     }
     *</pre>
     *
     * <strong>Recommended.</strong> <br></br>
     * <pre>
     *    public void foo(){
     *         Bool bool = KibanaRequestHelper.getBool(req);
     *         bool.setShould(should)
     *         bool.setFilter(filterA)
     *         bool.setFilter(filterB)
     *      }
     * </pre>
     */
    public static Bool getBool(IKibanaRequest kibanaRequest) {
        try {
            return CrawlerConstants.MAPPER.readValue(kibanaRequest.getJSONRequest(), KibanaRequest.class)
                    .getParams()
                    .getBody()
                    .getQuery()
                    .getBool();
        } catch (JsonProcessingException e) {
           log.error("Unable to get boolean");
           throw new RuntimeException(e);
        }
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
    public static <T> List<T> getFilter(IKibanaRequest kibanaRequest, Class<? extends Filter> filterClazz){
        return (List<T>) getBool(kibanaRequest).getFilter()
                .stream().filter(filter -> filter.getClass().equals(filterClazz)).collect(Collectors.toList());
    }
}
