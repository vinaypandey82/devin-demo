package com.payment.processing.dto;

public class PaymentResponse {

    private int statusCode;
    private String reqId;
    private Integer srcAcc;
    private String message;

    public PaymentResponse() {
    }

    public PaymentResponse(int statusCode, String reqId, Integer srcAcc, String message) {
        this.statusCode = statusCode;
        this.reqId = reqId;
        this.srcAcc = srcAcc;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Integer getSrcAcc() {
        return srcAcc;
    }

    public void setSrcAcc(Integer srcAcc) {
        this.srcAcc = srcAcc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
