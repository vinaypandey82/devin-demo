package com.payment.gateway.formatter;

import com.payment.gateway.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class FixedWidthFormatter {

    private static final int RESPONSE_LENGTH = 300;

    public String format(PaymentResponse response) {
        String status = response.getStatusCode() == 0 ? "SUCCESS" : "FAILURE";
        String reqId = response.getReqId() != null ? response.getReqId() : "";
        int srcAcc = response.getSrcAcc() != null ? response.getSrcAcc() : 0;

        String formatted = String.format("%-10s%-10s%010d", status, reqId, srcAcc);

        StringBuilder sb = new StringBuilder(formatted);
        while (sb.length() < RESPONSE_LENGTH) {
            sb.append(' ');
        }
        return sb.substring(0, RESPONSE_LENGTH);
    }

    public String formatError(String errorCode) {
        String formatted = String.format("%-10s", errorCode);
        StringBuilder sb = new StringBuilder(formatted);
        while (sb.length() < RESPONSE_LENGTH) {
            sb.append(' ');
        }
        return sb.substring(0, RESPONSE_LENGTH);
    }
}
