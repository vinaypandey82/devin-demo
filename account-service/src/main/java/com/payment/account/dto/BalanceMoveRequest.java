package com.payment.account.dto;

public class BalanceMoveRequest {

    private Integer sourceAccountId;
    private Integer destinationAccountId;
    private Double amount;

    public BalanceMoveRequest() {
    }

    public BalanceMoveRequest(Integer sourceAccountId, Integer destinationAccountId, Double amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Integer sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Integer getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Integer destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
