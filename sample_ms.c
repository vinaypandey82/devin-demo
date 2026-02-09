#include <stdio.h>
#include <sqlca.h>

EXEC SQL BEGIN DECLARE SECTION;
    int   user_id = 505;
    int   product_id = 99;
    int   qty = 2;
    float item_price;
    float total_cost;
    float current_balance;
    int   order_id;
    char  status_msg[100];
EXEC SQL END DECLARE SECTION;

void handle_error(const char *stage) {
    printf("Failure at stage: %s\n", stage);
    EXEC SQL ROLLBACK WORK; // Full rollback of the transaction
    exit(1);
}

int main() {
    EXEC SQL CONNECT :user_id IDENTIFIED BY "secure_pass";

    /* --- STAGE 1: INVENTORY CHECK --- */
    EXEC SQL SELECT price INTO :item_price FROM products 
             WHERE prod_id = :product_id AND stock_level >= :qty 
             FOR UPDATE OF stock_level; // Row lock

    if (sqlca.sqlcode == 1403) { // Not found
        printf("Insufficient stock.\n");
        return 0;
    }

    /* --- STAGE 2: CREATE SAVEPOINT --- */
    // We want to be able to roll back just the order if balance fails
    EXEC SQL SAVEPOINT start_order;

    total_cost = item_price * qty;

    /* --- STAGE 3: RECORD ORDER --- */
    EXEC SQL INSERT INTO orders (order_id, cust_id, amount, status)
             VALUES (order_seq.NEXTVAL, :user_id, :total_cost, 'PENDING')
             RETURNING order_id INTO :order_id;

    if (sqlca.sqlcode != 0) handle_error("Order Insertion");

    /* --- STAGE 4: UPDATE CUSTOMER BALANCE --- */
    EXEC SQL SELECT balance INTO :current_balance FROM customers 
             WHERE cust_id = :user_id FOR UPDATE;

    if (current_balance < total_cost) {
        printf("Insufficient funds. Rolling back to Savepoint.\n");
        EXEC SQL ROLLBACK TO SAVEPOINT start_order;
        // Logic to record a 'Failed Payment' entry instead
        EXEC SQL INSERT INTO logs (msg) VALUES ('Failed payment for user');
        EXEC SQL COMMIT WORK; 
        return 0;
    }

    /* --- STAGE 5: NESTED LOGIC - DEDUCT STOCK --- */
    EXEC SQL UPDATE products SET stock_level = stock_level - :qty 
             WHERE prod_id = :product_id;

    EXEC SQL UPDATE customers SET balance = balance - :total_cost 
             WHERE cust_id = :user_id;

    /* --- STAGE 6: FINAL COMMIT --- */
    if (sqlca.sqlcode == 0) {
        EXEC SQL COMMIT WORK RELEASE;
        printf("Transaction Complete. Order ID: %d\n", order_id);
    } else {
        handle_error("Finalizing Transaction");
    }

    return 0;
}
