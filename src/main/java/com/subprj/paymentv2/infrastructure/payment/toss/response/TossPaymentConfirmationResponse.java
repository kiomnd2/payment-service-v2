package com.subprj.paymentv2.infrastructure.payment.toss.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TossPaymentConfirmationResponse {
    private String version;
    private String paymentKey;
    private String type;
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private String method;
    private long totalAmount;
    private long balanceAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private boolean useEscrow;
    private String lastTransactionKey;
    private long suppliedAmount;
    private double vat;
    private boolean cultureExpense;
    private double taxFreeAmount;
    private int taxExemptionAmount;
    private List<Cancel> cancels;
    private boolean isPartialCancelable;
    private Card card;
    private VirtualAccount virtualAccount;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private Transfer transfer;
    private Receipt receipt;
    private Checkout checkout;
    private EasyPay easyPay;
    private String country;
    private Failure failure;
    private CashReceipt cashReceipt;

    // Getters and Setters

    @Getter
    @Setter
    @ToString
    public static class Cancel {
        private long cancelAmount;
        private String cancelReason;
        private long taxFreeAmount;
        private int taxExemptionAmount;
        private long refundableAmount;
        private long easyPayDiscountAmount;
        private String canceledAt;
        private String transactionKey;
        private String receiptKey;
        private String cancelStatus;
        private String cancelRequestId;

        // Getters and Setters
    }

    @Getter
    @Setter
    @ToString
    public static class Card {
        private long amount;
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private int installmentPlanMonths;
        private String approveNo;
        private boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private boolean isInterestFree;
        private String interestPayer;

        // Getters and Setters
    }

    @Getter
    @Setter
    @ToString
    public static class VirtualAccount {
        private String accountType;
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
        private String refundStatus;
        private boolean expired;
        private String settlementStatus;
        private RefundReceiveAccount refundReceiveAccount;
        private String secret;

        // Getters and Setters
    }

    @Getter
    @Setter
    @ToString
    public static class MobilePhone {
        private String customerMobilePhone;
        private String settlementStatus;
        private String receiptUrl;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class GiftCertificate {
        private String approveNo;
        private String settlementStatus;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class Transfer {
        private String bankCode;
        private String settlementStatus;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class Receipt {
        private String url;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class Checkout {
        private String url;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class EasyPay {
        private String provider;
        private long amount;
        private long discountAmount;

        // Getters and Setters
    }

    @Getter
    @Setter
    @ToString
    public static class RefundReceiveAccount {
        private String bankCode;
        private String accountNumber;
        private String holderName;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class Failure {
        private String code;
        private String message;

        // Getters and Setters
    }


    @Getter
    @Setter
    @ToString
    public static class CashReceipt {
        private String type;
        private String receiptKey;
        private String issueNumber;
        private String receiptUrl;
        private long amount;
        private int taxFreeAmount;
        private String issueStatus;
        private Failure failure;
        private String customerIdentityNumber;
        private String requestedAt;

        // Getters and Setters
    }
}

