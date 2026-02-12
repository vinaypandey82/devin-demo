#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "tx_definitions.h"

int parse_positional_string(const char* raw, PaymentRequest* req) {
    if (strlen(raw) < 300) return -1;
    char buffer[16];

    // Header: ReqID (0-10), Type (10-15)
    strncpy(req->req_id, raw, 10);
    req->req_id[10] = '\0';
    
    if (strncmp(raw + 10, "WIRE ", 5) == 0) req->channel = CH_ASYNC_WIRE;
    else req->channel = CH_SYNC_INTERNAL;

    // Body: Sender(50-60), Receiver(70-80), Amount(80-95)
    strncpy(buffer, raw + 50, 10); buffer[10] = '\0';
    req->src_acc = atoi(buffer);

    strncpy(buffer, raw + 70, 10); buffer[10] = '\0';
    req->dest_acc = atoi(buffer);

    strncpy(buffer, raw + 80, 15); buffer[15] = '\0';
    req->amount = atof(buffer);

    return 0;
}
