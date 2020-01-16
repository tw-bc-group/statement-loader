package com.thoughtworks.blockchain.statementloader.batch.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component("RestReader")
public class RestItemReader implements ItemReader<JsonObject> {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextJasonObjectIndex;
    private List<JsonObject> jsonObjects;

    @Autowired
    public RestItemReader(@Value("${loader.bridge.url}") String apiUrl) {
        this.apiUrl = apiUrl;
        this.restTemplate = new RestTemplate();
        nextJasonObjectIndex = 0;
    }

    @Override
    public JsonObject read() {
        log.info("Reading the information of the next");
        if (dataIsNotInitialized()) {
            jsonObjects = fetchFromApi();
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

    private List<JsonObject> fetchFromApi() {
        log.debug("fetching data from an external API by using the url: {}", apiUrl);

        ResponseEntity<Object[]> response = restTemplate.getForEntity(
                apiUrl,
                Object[].class
        );
        final Gson gson = new Gson();
        final JsonObject[] result = gson.fromJson(gson.toJson(response.getBody()), JsonObject[].class);

        log.info("found: {}", result.length);
        return Arrays.asList(result);
    }
}
