package com.payment.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "async_error_logs")
public class AsyncErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "req_id", length = 10)
    private String reqId;

    @Column(name = "acc_id")
    private Integer accId;

    @Column(name = "error_desc", length = 100)
    private String errorDesc;

    @Column(name = "log_date")
    private LocalDateTime logDate;

    public AsyncErrorLog() {
    }

    public AsyncErrorLog(String reqId, Integer accId, String errorDesc, LocalDateTime logDate) {
        this.reqId = reqId;
        this.accId = accId;
        this.errorDesc = errorDesc;
        this.logDate = logDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDateTime logDate) {
        this.logDate = logDate;
    }
}
