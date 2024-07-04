package com.subprj.paymentv2.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public class PSPConfirmationException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryableError;
    private Throwable cause;

    @Builder
    public PSPConfirmationException(String errorCode, String errorMessage, Boolean isSuccess,
                                    Boolean isFailure, Boolean isUnknown, Boolean isRetryableError, Throwable cause) {

        long count = Stream.of(isSuccess, isFailure, isUnknown).filter(v -> v != null && v).count();
        if (count != 1) {
            throw new IllegalArgumentException(String.format("(%s) 는 올바르지 않는 결제 상태입니다.", this.getClass().getName()));
        }
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryableError = isRetryableError;
        this.cause = cause;
    }
}
