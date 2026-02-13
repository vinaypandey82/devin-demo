package com.payment.account.dto;

public class AuditLogRequest {

    private String reqId;
    private Integer accId;
    private String errorDesc;

    public AuditLogRequest() {
    }

    public AuditLogRequest(String reqId, Integer accId, String errorDesc) {
        this.reqId = reqId;
        this.accId = accId;
        this.errorDesc = errorDesc;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Integer getAccId() {
        return accId;
    }

    public void setAccId(Integer accId) {
        this.accId = accId;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
