package com.n26.service;

import com.n26.payload.Statistics;
import com.n26.payload.Transaction;

public interface TransactionService {
    long PERIOD_IN_SECONDS = 60000;

    void addTransaction(Transaction transaction);

    Statistics getStatistics();

    void deleteTransactions();

}
