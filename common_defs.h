#ifndef COMMON_DEFS_H
#define COMMON_DEFS_H

typedef struct {
    int account_id;
    double amount;
    char currency[4];
    char branch_code[10];
} TransactionRequest;

/* Shared SQL Communication Area */
#include <sqlca.h>

#endif
