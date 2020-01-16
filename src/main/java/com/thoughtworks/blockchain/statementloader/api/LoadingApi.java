package com.thoughtworks.blockchain.statementloader.api;

import com.thoughtworks.blockchain.statementloader.loader.LoadingServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@RestController
@RequestMapping("/batch-data")
public class LoadingApi {

    private final LoadingServiceFactory loadingServiceFactory;

    @Autowired
    public LoadingApi(LoadingServiceFactory loadingServiceFactory) {
        this.loadingServiceFactory = loadingServiceFactory;
    }

    @GetMapping("/bridge")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StreamingResponseBody> loadBridgeData() {
        final InputStreamResource resource = loadingServiceFactory.getByBeanName("Rest").loadingData(0L, 0L);
        return new ResponseEntity<>(out -> IOUtils.copy(resource.getInputStream(), out), HttpStatus.OK);
    }

    @GetMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StreamingResponseBody> loadPaymentData(@RequestParam("startTime") Long startTimestamp,
                                                                 @RequestParam("endTime") Long endTimestamp) {
        final InputStreamResource resource = loadingServiceFactory.getByBeanName("Mysql").loadingData(startTimestamp, endTimestamp);
        return new ResponseEntity<>(out -> IOUtils.copy(resource.getInputStream(), out), HttpStatus.OK);
    }
}
