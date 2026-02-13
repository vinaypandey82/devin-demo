package com.payment.gateway.parser;

import com.payment.gateway.dto.ChannelType;
import com.payment.gateway.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class FixedWidthParser {

    private static final int REQUIRED_LENGTH = 300;

    public PaymentRequest parse(String raw) {
        if (raw == null || raw.length() < REQUIRED_LENGTH) {
            return null;
        }

        PaymentRequest req = new PaymentRequest();

        req.setReqId(raw.substring(0, 10).trim());

        String channelStr = raw.substring(10, 15);
        if ("WIRE ".equals(channelStr)) {
            req.setChannel(ChannelType.CH_ASYNC_WIRE);
        } else {
            req.setChannel(ChannelType.CH_SYNC_INTERNAL);
        }

        String srcAccStr = raw.substring(50, 60).trim();
        req.setSrcAcc(Integer.parseInt(srcAccStr));

        String destAccStr = raw.substring(70, 80).trim();
        req.setDestAcc(Integer.parseInt(destAccStr));

        String amountStr = raw.substring(80, 95).trim();
        req.setAmount(Double.parseDouble(amountStr));

        return req;
    }
}
