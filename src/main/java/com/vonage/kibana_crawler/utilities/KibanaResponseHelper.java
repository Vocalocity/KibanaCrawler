package com.vonage.kibana_crawler.utilities;

import com.vonage.kibana_crawler.pojo.kibana_response.KibanaResponse;
import com.vonage.kibana_crawler.pojo.kibana_response.NestedHits;
import com.vonage.kibana_crawler.pojo.kibana_response.Source;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class KibanaResponseHelper {

    private KibanaResponseHelper() {}

    public static List<String> getMessage(KibanaResponse response){
        return Objects.requireNonNull(response)
                .getRawResponse()
                .getHits()
                .getHits()
                .stream()
                .map(hit -> hit.getSource().getMessage())
                .collect(Collectors.toList());
    }

    public static List<Source> getSource(KibanaResponse response){
       return Objects.requireNonNull(response)
                .getRawResponse()
                .getHits()
                .getHits()
                .stream()
                .map(NestedHits::getSource)
                .collect(Collectors.toList());
    }
}
