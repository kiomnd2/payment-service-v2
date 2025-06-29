package com.subprj.paymentv2.stream.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PartitionKeyUtil {
    private final int partitionKeyCounty = 6;

    public int createPartitionKey(int number) {
        return Math.abs(number) % partitionKeyCounty;
    }

}
