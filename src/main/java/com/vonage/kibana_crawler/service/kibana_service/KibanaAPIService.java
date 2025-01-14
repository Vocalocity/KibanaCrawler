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
import okhttp3.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class KibanaAPIService implements IKibanaAPIService {

    private final OkHttpClient okHttpClient;

    private final AppCustomizedKibanaRequestToKibanaRequestMapper kibanaRequestMapper;

    private final CrawlerEnvironment environment;

    private final static int TIMEOUT = 150000; // timeout is 2.5 minutes.

    @Setter
    @Getter
    private Headers headers;

    /**
     * Sends request to Kibana servers and gets response. If the response is successful, map the response to {@link KibanaResponse}
     * and returns the same. If the response status is in between 400 and 499 returns {@link KibanaResponse#unauthorizedResponse()}.
     * If the response status is greater than 499 then returns {@link KibanaResponse#invalidResponse()}
     * @param request simple kibana request
     * @return {@link KibanaResponse}, {@link KibanaResponse#unauthorizedResponse()}, {@link KibanaResponse#invalidResponse()}
     */
    @SneakyThrows
    @ExecutionTime
    public KibanaResponse sendRequestHelper(KibanaRequest request) {
        if(headers == null) {
            log.error("Unable to move call Kibana as headers are null.");
            return KibanaResponse.unauthorizedResponse();
        }
        log.info("Searching for '{}' Duration {}...", KibanaRequestHelper.getQuery(request), KibanaRequestHelper.getRange(request, "timestamp"));
        try(Response response = getFutureResponse(request)){
            return getResponseByStatus(response);
        }
        catch (TimeoutException e) {
            log.error("Connection timed out.");
        }
        catch (Exception e){
            log.error("Exception occurred while sending request.");
        }
        return KibanaResponse.invalidResponse();
    }

    private KibanaResponse getResponseByStatus(Response response) throws IOException {
        if(response.isSuccessful()) {
            return CrawlerConstants.MAPPER.readValue(response.body().string(), KibanaResponse.class);
        }
        else if(response.code() >= 400 && response.code() < 500) {
            return KibanaResponse.unauthorizedResponse();
        }
        else{
            return KibanaResponse.invalidResponse();
        }
    }

    private Response getFutureResponse(KibanaRequest kibanaRequest) throws ExecutionException, InterruptedException, TimeoutException {
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), kibanaRequest.toString());
        Request request = new Request.Builder()
                .url(CrawlerConstants.KIBANA_HOST + Symbols.FORWARD_SLASH.getSymbol() + CrawlerConstants.OPEN_SEARCH)
                .post(requestBody)
                .headers(headers)
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try{
                return okHttpClient.newCall(request).execute();
            } catch (Exception e){
                log.error("Error in getFutureResponse.", e);
            }
            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_2)
                    .code(500)
                    .body(ResponseBody.create((MediaType.get("application/json; charset=utf-8")), "Error while fetching response."))
                    .build();

        }).get(TIMEOUT, TimeUnit.MINUTES);
    }

    /**
     * Sends request to helper method. Retries if the response is invalid.
     * @return {@link KibanaResponse}
     */
    @Override
    public KibanaResponse sendRequest(KibanaRequest request) {
        KibanaResponse response = KibanaResponse.invalidResponse();
        Integer retries = environment.getKibanaApiClientRetry();
        while(KibanaResponse.invalidResponse().equals(response) && retries > 0){
            response = sendRequestHelper(request);
            retries--;
        }
        return response;
    }

    /**
     * @param customizedKibanaRequest throw {@link IllegalArgumentException} if this is null.
     */
    @Override
    @ExecutionTime
    public void sendRequest(AppCustomizedKibanaRequest customizedKibanaRequest, Queue<KibanaResponse> container) {
        KibanaResponse defaultResponse = KibanaResponse.invalidResponse();
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
                KibanaResponse kibanaResponse = sendRequest(this.kibanaRequestMapper.toKibanaRequest(customizedKibanaRequest));
                if(KibanaResponse.unauthorizedResponse().equals(kibanaResponse)){
                    defaultResponse = kibanaResponse;
                    return;
                }
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
            container.offer(defaultResponse);
        }
    }
}
