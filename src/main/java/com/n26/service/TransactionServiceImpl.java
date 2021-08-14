package com.n26.service;

import com.n26.exception.TimestampInFutureException;
import com.n26.exception.WrongTimestampException;
import com.n26.payload.Statistics;
import com.n26.payload.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final List<Transaction> transactions;
    private static final Object mutexLock = new Object();

    public TransactionServiceImpl() {
        transactions = new ArrayList<>();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        Date d = new Date();
        if (d.getTime() - transaction.getTimestamp().getTime() > PERIOD_IN_SECONDS)
            throw new WrongTimestampException();
        if (d.getTime() - transaction.getTimestamp().getTime() < 0)
            throw new TimestampInFutureException();

        synchronized (mutexLock) {
            transactions.add(transaction);
            removeOldTransactions();
        }
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        synchronized (mutexLock) {
            removeOldTransactions();
            transactions.stream()
                    .map(Transaction::getAmount)
                    .forEach(statistics::accept);
            return statistics;
        }
    }

    private void removeOldTransactions() {
        Date d = new Date();
        transactions.removeIf(transaction -> d.getTime() - transaction.getTimestamp().getTime() > PERIOD_IN_SECONDS);
    }

    @Override
    public void deleteTransactions() {
        synchronized (mutexLock) {
            transactions.clear();
        }
    }
}
