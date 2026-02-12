#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "tx_definitions.h"

/* FORMAT SPECIFICATION:
   Header (50 chars): 0-10: Date, 10-15: ReqType, 15-50: Padding
   Body (250 chars):  0-10: Sender, 10-20: Date, 20-30: Receiver, 30-45: Amount
*/

int parse_positional_string(const char* raw, PaymentRequest* req) {
    if (strlen(raw) < 300) return -1;

    char buffer[50];

    // 1. Parse Header - Request Type (Offset 10, Length 5)
    memset(buffer, 0, 50);
    strncpy(buffer, raw + 10, 5);
    if (strstr(buffer, "WIRE")) req->channel = CH_ASYNC_WIRE;
    else req->channel = CH_SYNC_INTERNAL;

    // 2. Parse Body - Sender Account (Offset 50, Length 10)
    memset(buffer, 0, 50);
    strncpy(buffer, raw + 50, 10);
    req->src_acc = atoi(buffer);

    // 3. Parse Body - Receiver Account (Offset 70, Length 10)
    memset(buffer, 0, 50);
    strncpy(buffer, raw + 70, 10);
    req->dest_acc = atoi(buffer);

    // 4. Parse Body - Amount (Offset 80, Length 15)
    memset(buffer, 0, 50);
    strncpy(buffer, raw + 80, 15);
    req->amount = atof(buffer);

    // Copy Req ID from Header for tracking
    strncpy(req->req_id, raw, 10); 
    req->req_id[10] = '\0';

    return 0;
}
