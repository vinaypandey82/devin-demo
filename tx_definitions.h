#ifndef TX_DEFS_H
#define TX_DEFS_H

#include <sqlca.h>

typedef enum { CH_SYNC_INTERNAL, CH_ASYNC_WIRE } ChannelType;

typedef struct {
    char req_id[11];
    ChannelType channel;
    int src_acc;
    int dest_acc;
    double amount;
    char raw_header[51];
} PaymentRequest;

/* Prototypes for cross-module integration */
int parse_positional_string(const char* raw, PaymentRequest* req);
void process_payment(PaymentRequest* req);
void log_exception_to_db(PaymentRequest *req, const char *msg);
int perform_balance_move(int src, int dest, double amt);

#endif
