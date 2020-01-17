package com.thoughtworks.blockchain.statementloader.batch.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RestItemReader implements ItemReader<JsonObject> {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextJasonObjectIndex;
    private List<JsonObject> jsonObjects;

    private Long startTimeInterval;
    private Long endTimeInterval;

    public RestItemReader(String apiUrl) {
        this.apiUrl = apiUrl;
        this.restTemplate = new RestTemplate();
        this.nextJasonObjectIndex = 0;
        this.startTimeInterval = 0L;
        this.endTimeInterval = Long.MAX_VALUE;
    }

    public void setTimeInterval(Long startTimestamp, Long endTimestamp) {
        this.startTimeInterval = startTimestamp;
        this.endTimeInterval = endTimestamp;
    }

    @Override
    public JsonObject read() {
        log.info("Reading the information of the next");
        if (dataIsNotInitialized()) {
            jsonObjects = fetchFromApi(startTimeInterval, endTimeInterval);
        }

        JsonObject nextJson = null;
        if (nextJasonObjectIndex < jsonObjects.size()) {
            nextJson = jsonObjects.get(nextJasonObjectIndex);
            nextJasonObjectIndex++;
        }
        log.info("got json: {}", nextJson);
        return nextJson;
    }

    private boolean dataIsNotInitialized() {
        return this.jsonObjects == null;
    }

    private List<JsonObject> fetchFromApi(Long startTimestamp, Long endTimestamp) {
        log.debug("fetching data from an external API by using the url: {}", apiUrl);

        ResponseEntity<Object[]> response = restTemplate.getForEntity(
                apiUrl,
                Object[].class
        );
        final Gson gson = new Gson();
        final JsonObject[] result = gson.fromJson(gson.toJson(response.getBody()), JsonObject[].class);
        log.info("found: {}", result.length);

        final List<JsonObject> filteredResult = new ArrayList<>();
        for (JsonObject json : result) {
            if (isInTimeInterval(parseBlockTime(json), startTimestamp, endTimestamp)) {
                filteredResult.add(json);
            }
        }
        log.info("after filter: {}", filteredResult.size());
        return filteredResult;
    }

    private boolean isInTimeInterval(BigInteger blockTimestamp, Long startTimeInterval, Long endTimeInterval) {
        return blockTimestamp.compareTo(BigInteger.valueOf(startTimeInterval)) >= 0 &&
                blockTimestamp.compareTo(BigInteger.valueOf(endTimeInterval)) < 0;
    }

    private BigInteger parseBlockTime(JsonObject json) {
        BigInteger r = BigInteger.ZERO;
        try {
            r = json
                    .get("txs")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("blockTime")
                    .getAsBigInteger();
        } catch (NullPointerException ignored) {

        }
        return r;
    }
}
