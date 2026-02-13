package com.payment.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "acc_id")
    private Integer accId;

    @Column(name = "balance", nullable = false)
    private Double balance;

    public Account() {
    }

    public Account(Integer accId, Double balance) {
        this.accId = accId;
        this.balance = balance;
    }

    public Integer getAccId() {
        return accId;
    }

    public void setAccId(Integer accId) {
        this.accId = accId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
