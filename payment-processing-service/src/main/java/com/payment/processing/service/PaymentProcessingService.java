package com.payment.processing.service;

import com.payment.processing.client.AccountServiceClient;
import com.payment.processing.dto.BalanceMoveResponse;
import com.payment.processing.dto.PaymentRequest;
import com.payment.processing.dto.PaymentResponse;
import com.payment.processing.model.ChannelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessingService {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessingService.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    private final AccountServiceClient accountServiceClient;

    public PaymentProcessingService(AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;
    }

    public PaymentResponse processPayment(PaymentRequest req) {
        int retryCount = 0;

        while (retryCount < MAX_RETRIES) {
            BalanceMoveResponse moveResponse = accountServiceClient.performBalanceMove(
                    req.getSrcAcc(), req.getDestAcc(), req.getAmount()
            );

            int statusCode = moveResponse.getStatusCode();

            if (statusCode == 0) {
                log.info("ID: {} | Status: SUCCESS", req.getReqId());
                return new PaymentResponse(0, req.getReqId(), req.getSrcAcc(), "SUCCESS");
            } else if (statusCode == -1 && retryCount < MAX_RETRIES - 1) {
                retryCount++;
                log.info("ID: {} | Locked. Retrying {}/{}...", req.getReqId(), retryCount, MAX_RETRIES);
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                log.info("ID: {} | Status: FAILED | Code: {}", req.getReqId(), statusCode);

                if (req.getChannel() == ChannelType.CH_ASYNC_WIRE) {
                    String errMsg = String.format("TX Failed with Code %d after %d retries", statusCode, retryCount);
                    accountServiceClient.logAuditError(req.getReqId(), req.getSrcAcc(), errMsg);
                }

                return new PaymentResponse(statusCode, req.getReqId(), req.getSrcAcc(),
                        "FAILED: " + moveResponse.getMessage());
            }
        }

        log.info("ID: {} | Status: FAILED | Max retries exceeded", req.getReqId());

        if (req.getChannel() == ChannelType.CH_ASYNC_WIRE) {
            String errMsg = String.format("TX Failed with Code -1 after %d retries", MAX_RETRIES);
            accountServiceClient.logAuditError(req.getReqId(), req.getSrcAcc(), errMsg);
        }

        return new PaymentResponse(-1, req.getReqId(), req.getSrcAcc(), "FAILED: Max retries exceeded");
    }
}
