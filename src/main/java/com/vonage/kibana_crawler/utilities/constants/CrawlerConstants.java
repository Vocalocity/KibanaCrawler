package com.vonage.kibana_crawler.utilities.constants;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.format.DateTimeFormatter;

public final class CrawlerConstants {

    private CrawlerConstants(){};

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .findAndRegisterModules();

    // API Calls Constants
    public static final String KIBANA_HOST = "https://virginia-cl.prod.logs.vonage.com";

    public static final String OPEN_SEARCH = "_dashboards/internal/search/opensearch-with-long-numerals";

    // Errors
    public static final String UNABLE_TO_GET_RESPONSE = "Unable to get response from server.";

    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";

    public static final String NO_HITS = "No hits found.";

    // Logs constants
    public static final String DURATION_OF_LOGS = "Duration of logs";

    public static final String LOG_IDS = "LOG IDs";

    public static final String LOG_SIZE = "LOG size";

    public static final String UNABLE_TO_COMPLETE_REQUEST = "Unable to complete request.";

    public static final String DEFAULT_KIBANA_REQUEST_PATH = "/DEFAULT_KIBANA_PAYLOAD.json";
}
