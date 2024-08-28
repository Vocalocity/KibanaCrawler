package com.vonage.kibana_crawler.builder;

import com.vonage.kibana_crawler.pojo.AppCustomizedKibanaRequest;
import com.vonage.kibana_crawler.utilities.KibanaRequestHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class AppCustomizedKibanaRequestBuilder {

    private final AppCustomizedKibanaRequest kibanaRequest;


    public AppCustomizedKibanaRequestBuilder() {
        this.kibanaRequest = new AppCustomizedKibanaRequest();
        this.kibanaRequest.setFilters(new ArrayList<>());
        this.kibanaRequest.setMustNot(new ArrayList<>());
        this.kibanaRequest.setShould(new ArrayList<>());
    }

    public AppCustomizedKibanaRequestBuilder addMultiMatch(String query){
        this.kibanaRequest.getFilters().add(KibanaRequestHelper.createMultiMatch(query));
        return this;
    }

    public AppCustomizedKibanaRequestBuilder addShould(String query){
        this.kibanaRequest.getShould().add(KibanaRequestHelper.createMultiMatch(query));
        return this;
    }

    public AppCustomizedKibanaRequestBuilder addShould(String... queries){
        for(String query : queries){
            this.kibanaRequest.getShould().add(KibanaRequestHelper.createMultiMatch(query));
        }
        return this;
    }

    public AppCustomizedKibanaRequestBuilder addMultiMatch(String... queries){
        for(String query : queries){
            this.kibanaRequest.getFilters().add(KibanaRequestHelper.createMultiMatch(query));
        }
        return this;
    }

    public AppCustomizedKibanaRequestBuilder addMatchPhrase(Pair<String, Object> matchPhrase){
        this.kibanaRequest.getFilters().add(KibanaRequestHelper.createMatchPhrase(matchPhrase));
        return this;
    }

    public AppCustomizedKibanaRequestBuilder addMustNot(Pair<String, Object> mustNot){
        this.kibanaRequest.getMustNot().add(KibanaRequestHelper.createMatchPhrase(mustNot));
        return this;
    }

    public AppCustomizedKibanaRequestBuilder addRange(String type, String gte, String lte){
        this.kibanaRequest.getFilters().add(KibanaRequestHelper.createRange(type, gte, lte));
        return this;
    }

    public AppCustomizedKibanaRequestBuilder index(String index){
        this.kibanaRequest.setIndex(index);
        return this;
    }

    public AppCustomizedKibanaRequestBuilder size(Integer size){
        this.kibanaRequest.setSize(size);
        return this;
    }

    public AppCustomizedKibanaRequest build() {
        return kibanaRequest;
    }
}
