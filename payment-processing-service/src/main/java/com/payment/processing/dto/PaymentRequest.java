package com.payment.processing.dto;

import com.payment.processing.model.ChannelType;

public class PaymentRequest {

    private String reqId;
    private ChannelType channel;
    private Integer srcAcc;
    private Integer destAcc;
    private Double amount;

    public PaymentRequest() {
    }

    public PaymentRequest(String reqId, ChannelType channel, Integer srcAcc, Integer destAcc, Double amount) {
        this.reqId = reqId;
        this.channel = channel;
        this.srcAcc = srcAcc;
        this.destAcc = destAcc;
        this.amount = amount;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public ChannelType getChannel() {
        return channel;
    }

    public void setChannel(ChannelType channel) {
        this.channel = channel;
    }

    public Integer getSrcAcc() {
        return srcAcc;
    }

    public void setSrcAcc(Integer srcAcc) {
        this.srcAcc = srcAcc;
    }

    public Integer getDestAcc() {
        return destAcc;
    }

    public void setDestAcc(Integer destAcc) {
        this.destAcc = destAcc;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
