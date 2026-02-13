package com.payment.gateway.client;

import com.payment.gateway.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class PaymentProcessingClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessingClient.class);

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public PaymentProcessingClient(RestTemplate restTemplate,
                                   @Value("${payment-processing-service.url}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public PaymentResponse processPayment(Map<String, Object> request) {
        String url = paymentServiceUrl + "/api/payments/process";
        log.info("Calling Payment Processing Service: POST {}", url);
        return restTemplate.postForObject(url, request, PaymentResponse.class);
    }
}
