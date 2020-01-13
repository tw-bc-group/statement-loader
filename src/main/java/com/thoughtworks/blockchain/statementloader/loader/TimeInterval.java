package com.thoughtworks.blockchain.statementloader.loader;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class TimeInterval {
    private Long startTime;
    private Long endTime;
}
