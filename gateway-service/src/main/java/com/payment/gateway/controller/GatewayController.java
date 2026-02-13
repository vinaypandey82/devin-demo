package com.payment.gateway.controller;

import com.payment.gateway.client.PaymentProcessingClient;
import com.payment.gateway.dto.PaymentRequest;
import com.payment.gateway.dto.PaymentResponse;
import com.payment.gateway.formatter.FixedWidthFormatter;
import com.payment.gateway.parser.FixedWidthParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private final FixedWidthParser parser;
    private final FixedWidthFormatter formatter;
    private final PaymentProcessingClient paymentProcessingClient;

    public GatewayController(FixedWidthParser parser,
                             FixedWidthFormatter formatter,
                             PaymentProcessingClient paymentProcessingClient) {
        this.parser = parser;
        this.formatter = formatter;
        this.paymentProcessingClient = paymentProcessingClient;
    }

    @PostMapping(value = "/fixed-width",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> handleFixedWidth(@RequestBody String rawInput) {
        log.info("Received fixed-width request ({} chars)", rawInput.length());

        PaymentRequest req = parser.parse(rawInput);
        if (req == null) {
            return ResponseEntity.ok(formatter.formatError("ERR_LEN"));
        }

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("reqId", req.getReqId());
        paymentRequest.put("channel", req.getChannel().name());
        paymentRequest.put("srcAcc", req.getSrcAcc());
        paymentRequest.put("destAcc", req.getDestAcc());
        paymentRequest.put("amount", req.getAmount());

        PaymentResponse response = paymentProcessingClient.processPayment(paymentRequest);
        return ResponseEntity.ok(formatter.format(response));
    }

    @PostMapping(value = "/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentResponse> handleJson(@RequestBody PaymentRequest request) {
        log.info("Received JSON request for ID: {}", request.getReqId());

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("reqId", request.getReqId());
        paymentRequest.put("channel", request.getChannel().name());
        paymentRequest.put("srcAcc", request.getSrcAcc());
        paymentRequest.put("destAcc", request.getDestAcc());
        paymentRequest.put("amount", request.getAmount());

        PaymentResponse response = paymentProcessingClient.processPayment(paymentRequest);
        return ResponseEntity.ok(response);
    }
}
