package com.thoughtworks.blockchain.statementloader.loader;

import org.springframework.core.io.InputStreamResource;

public interface LoadingService {
    InputStreamResource loadingData(Long startTimestamp, Long endTimestamp);
}
