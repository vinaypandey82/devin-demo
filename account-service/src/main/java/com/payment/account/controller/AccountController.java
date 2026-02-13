package com.payment.account.controller;

import com.payment.account.dto.AuditLogRequest;
import com.payment.account.dto.BalanceMoveRequest;
import com.payment.account.dto.BalanceMoveResponse;
import com.payment.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/balance-move")
    public ResponseEntity<BalanceMoveResponse> balanceMove(@RequestBody BalanceMoveRequest request) {
        BalanceMoveResponse response = accountService.performBalanceMove(
                request.getSourceAccountId(),
                request.getDestinationAccountId(),
                request.getAmount()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/audit-log")
    public ResponseEntity<Void> logAudit(@RequestBody AuditLogRequest request) {
        accountService.logAsyncError(request.getReqId(), request.getAccId(), request.getErrorDesc());
        return ResponseEntity.ok().build();
    }
}
