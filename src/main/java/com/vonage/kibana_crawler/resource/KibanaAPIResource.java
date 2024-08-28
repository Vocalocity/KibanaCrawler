package com.vonage.kibana_crawler.resource;

import com.vonage.kibana_crawler.pojo.KibanaRequestHeader;
import com.vonage.kibana_crawler.service.kibana_service.KibanaAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class KibanaAPIResource {

    private final KibanaAPIService kibanaAPIService;

    @PostMapping("/headers")
    public ResponseEntity<String> setHeaders(@RequestBody KibanaRequestHeader requestHeader) {
        try{
            kibanaAPIService.setHeaders(requestHeader.formHeaders());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unable to set headers.");
        }
        return ResponseEntity.ok().body("Headers set.");
    }
}
