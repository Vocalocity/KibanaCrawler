package com.vonage.kibana_crawler.pojo;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;

@Setter
@AllArgsConstructor
public final class KibanaRequestHeader {

    private String cookie;

    private String osdVersion;

    private String osdXsrf;

    public HttpHeaders formHeaders(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("osd-version", Collections.singletonList(osdVersion));
        map.put("osd-xsrf", Collections.singletonList(osdXsrf));
        map.put("cookie", Arrays.asList(cookie));
        return new HttpHeaders(map);
    }
}
