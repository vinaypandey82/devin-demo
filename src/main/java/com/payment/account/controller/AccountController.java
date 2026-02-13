package com.payment.account.controller;

import com.payment.account.dto.TransferRequest;
import com.payment.account.exception.AccountLockedException;
import com.payment.account.exception.InsufficientFundsException;
import com.payment.account.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final int RESPONSE_LENGTH = 300;

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/transfer", produces = MediaType.TEXT_PLAIN_VALUE)
    public String transfer(@RequestBody TransferRequest request) {
        String status;
        try {
            accountService.performBalanceMove(
                    request.sourceAccount(),
                    request.destAccount(),
                    request.amount()
            );
            status = "SUCCESS";
        } catch (AccountLockedException e) {
            status = "LOCKED";
        } catch (InsufficientFundsException e) {
            status = "FAILURE";
        } catch (Exception e) {
            status = "FAILURE";
        }

        String base = String.format("%-10s%-10s%010d", status, request.requestId(), request.sourceAccount());
        return base + " ".repeat(Math.max(0, RESPONSE_LENGTH - base.length()));
    }
}
