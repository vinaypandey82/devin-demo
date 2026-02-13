package com.payment.account.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String requestId,
        int sourceAccount,
        int destAccount,
        BigDecimal amount
) {
}
