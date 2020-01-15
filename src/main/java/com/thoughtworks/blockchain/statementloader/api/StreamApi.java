package com.thoughtworks.blockchain.statementloader.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/stream")
public class StreamApi {

    @GetMapping
    public ResponseEntity<StreamingResponseBody> handle(HttpServletRequest request) {
        System.out.println("asyncSupported: " + request.isAsyncSupported());
        System.out.println(Thread.currentThread().getName());
        StreamingResponseBody responseBody = outputStream -> {
            for (int i = 0; i < 1000; i++) {
                outputStream.write((i + " - ").getBytes());
                outputStream.flush();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
