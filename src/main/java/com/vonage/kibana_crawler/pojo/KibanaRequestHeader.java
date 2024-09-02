package com.vonage.kibana_crawler.pojo;

import lombok.AllArgsConstructor;
import lombok.Setter;
import okhttp3.Headers;

import java.util.HashMap;
import java.util.Map;

@Setter
@AllArgsConstructor
public final class KibanaRequestHeader {

    private String cookie;

    private String osdVersion;

    private String osdXsrf;

    public Headers formHeaders(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("osd-version", osdVersion);
        headerMap.put("osd-xsrf", osdXsrf);
        headerMap.put("cookie", cookie);
        return Headers.of(headerMap);
    }
}
