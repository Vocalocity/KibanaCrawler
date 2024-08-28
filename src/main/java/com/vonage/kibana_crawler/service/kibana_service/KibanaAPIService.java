package com.vonage.kibana_crawler.service.kibana_service;

import com.vonage.kibana_crawler.aspects.annotation.ExecutionTime;
import com.vonage.kibana_crawler.configs.CrawlerEnvironment;
import com.vonage.kibana_crawler.mappers.AppCustomizedKibanaRequestToKibanaRequestMapper;
import com.vonage.kibana_crawler.pojo.kibana_response.NestedHits;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.utilities.KibanaRequestHelper;
import com.vonage.kibana_crawler.pojo.AppCustomizedKibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_request.KibanaRequest;
import com.vonage.kibana_crawler.pojo.kibana_response.KibanaResponse;
import com.vonage.kibana_crawler.utilities.constants.Symbols;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class KibanaAPIService implements IKibanaAPIService {

    private final RestTemplate restTemplate;

    private final AppCustomizedKibanaRequestToKibanaRequestMapper kibanaRequestMapper;

    private final CrawlerEnvironment environment;

    @Setter
    @Getter
    private HttpHeaders headers;

    @SneakyThrows
    @ExecutionTime
    private KibanaResponse sendRequest(KibanaRequest request) {
        log.info("Searching for '{}' Duration {}...", KibanaRequestHelper.getQuery(request), KibanaRequestHelper.getRange(request, "timestamp"));
        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    CrawlerConstants.KIBANA_HOST + Symbols.FORWARD_SLASH.getSymbol() + CrawlerConstants.OPEN_SEARCH,
                    HttpMethod.POST,
                    new HttpEntity<>(CrawlerConstants.MAPPER.writeValueAsString(request), headers),
                    String.class
            );
            if(response.getStatusCode().is2xxSuccessful()) {
                return CrawlerConstants.MAPPER.readValue(response.getBody(), KibanaResponse.class);
            }
            else log.error("{} Response {}", CrawlerConstants.SOMETHING_WENT_WRONG, response.getBody());
        }catch (Exception e){
            log.error("{} ERROR {}", CrawlerConstants.UNABLE_TO_GET_RESPONSE, e.getMessage());
        }
        return null;
    }

    @Override
    public KibanaResponse sendRequest(AppCustomizedKibanaRequest request) {
        KibanaRequest customizedRequest = kibanaRequestMapper.toKibanaRequest(request);
        KibanaResponse response = null;
        Integer retries = environment.getKibanaApiClientRetry();
        while(Objects.isNull(response) && retries > 0){
            response = sendRequest(customizedRequest);
            retries--;
        }
        return response;
    }

    /**
     * @param customizedKibanaRequest throw {@link IllegalArgumentException} if this is null.
     */
    @Override
    @ExecutionTime
    public void sendRequest(AppCustomizedKibanaRequest customizedKibanaRequest, BlockingQueue<KibanaResponse> container) {
        if(Objects.isNull(customizedKibanaRequest)) {
            throw new IllegalArgumentException("Cannot hit servers with empty request.");
        }
        if(Objects.isNull(container)) {
            throw new IllegalArgumentException("Cannot store response in queue.");
        }
        int totalRecords = -1, recordsFound = 0;
        List<Object> sort = null;
        try{
            do{
                customizedKibanaRequest.setSearchAfter(sort);
                KibanaResponse kibanaResponse = sendRequest(customizedKibanaRequest);
                log.info("Adding response to queue. Status {}", container.offer(Objects.isNull(kibanaResponse) ? KibanaResponse.invalidResponse() : kibanaResponse)
                        ? "Successful" : "Failed");
                List<NestedHits> hits = kibanaResponse.getRawResponse().getHits().getHits();
                if(hits.isEmpty()){
                    log.info(CrawlerConstants.NO_HITS);
                    break;
                }
                if(totalRecords == -1) {
                    totalRecords = kibanaResponse.getRawResponse().getHits().getTotal();
                }
                recordsFound += hits.size();
                NestedHits firstHit = hits.get(0);
                NestedHits lastHit = hits.get(hits.size()-1);
                sort = lastHit.getSort();
                log.info(CrawlerConstants.DURATION_OF_LOGS + ": {}", Stream.of(firstHit.getSource().getTimestamp(), lastHit.getSource().getTimestamp()).sorted().collect(Collectors.joining(" - ")));
                log.info(CrawlerConstants.LOG_IDS + "{}", firstHit.getId() + " - " + lastHit.getId());
                log.info(CrawlerConstants.LOG_SIZE + ": {}/{}", recordsFound, totalRecords);
            } while (recordsFound < totalRecords);
        } catch (Exception e){
            log.error(CrawlerConstants.UNABLE_TO_COMPLETE_REQUEST + " Error {}", e.getMessage());
        } finally {
            container.offer(KibanaResponse.invalidResponse());
        }
    }
}
