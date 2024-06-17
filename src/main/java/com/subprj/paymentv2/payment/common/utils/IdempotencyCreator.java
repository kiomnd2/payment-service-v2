package com.subprj.paymentv2.payment.common.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IdempotencyCreator {

    public String create(Object data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }
}
