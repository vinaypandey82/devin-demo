package com.payment.processing.client;

import com.payment.processing.dto.AuditLogRequest;
import com.payment.processing.dto.BalanceMoveRequest;
import com.payment.processing.dto.BalanceMoveResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceClient.class);

    private final RestTemplate restTemplate;
    private final String accountServiceUrl;

    public AccountServiceClient(RestTemplate restTemplate,
                                @Value("${account-service.url}") String accountServiceUrl) {
        this.restTemplate = restTemplate;
        this.accountServiceUrl = accountServiceUrl;
    }

    public BalanceMoveResponse performBalanceMove(Integer sourceId, Integer destinationId, Double amount) {
        BalanceMoveRequest request = new BalanceMoveRequest(sourceId, destinationId, amount);
        String url = accountServiceUrl + "/api/accounts/balance-move";
        log.info("Calling Account Service: POST {}", url);
        return restTemplate.postForObject(url, request, BalanceMoveResponse.class);
    }

    public void logAuditError(String reqId, Integer accId, String errorDesc) {
        AuditLogRequest request = new AuditLogRequest(reqId, accId, errorDesc);
        String url = accountServiceUrl + "/api/accounts/audit-log";
        log.info("Logging audit error to Account Service: POST {}", url);
        try {
            restTemplate.postForObject(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to log audit error for request {}: {}", reqId, e.getMessage());
        }
    }
}
