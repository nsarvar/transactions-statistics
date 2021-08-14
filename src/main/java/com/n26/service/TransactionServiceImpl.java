package com.n26.service;

import com.n26.exception.TimestampInFutureException;
import com.n26.exception.WrongTimestampException;
import com.n26.payload.Statistics;
import com.n26.payload.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final Map<Long, Statistics> statsMap;
    private static final Object mutexLock = new Object();

    public TransactionServiceImpl() {
        statsMap = new HashMap<>();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        long currentMs = Instant.now().toEpochMilli();
        long transactionMs = transaction.getTimestamp().getTime();
        long diff = currentMs - transactionMs;

        if (diff > PERIOD_IN_SECONDS)
            throw new WrongTimestampException();
        if (diff < 0)
            throw new TimestampInFutureException();

        synchronized (mutexLock) {
            // O(1) -> because number of elements are fixed, 1 minute has 60000 ms, so Map can have max 60000 elements
            removeOldTransactions();
            // O(1) - put into map is O(1)
            putIntoStatsMap(transaction);
        }
    }

    private void removeOldTransactions() {
        long currentMs = Instant.now().toEpochMilli();
        statsMap.entrySet()
                .removeIf(entry -> currentMs - entry.getKey() > PERIOD_IN_SECONDS);
    }

    private void putIntoStatsMap(Transaction transaction) {
        long mSeconds = transaction.getTimestamp().getTime();
        Statistics stats = statsMap.get(mSeconds);
        if (stats == null) {
            stats = new Statistics();
        }
        stats.accept(transaction.getAmount());
        statsMap.put(mSeconds, stats);
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        synchronized (mutexLock) {
            // O(1)
            removeOldTransactions();
            // O(1) -> because map can have max 60000 elements so it is constant
            statsMap.forEach((key, value) -> statistics.combine(value));
        }
        return statistics;
    }

    @Override
    public void deleteTransactions() {
        synchronized (mutexLock) {
            // O(1) - number of elements is constant - max=60000
            statsMap.clear();
        }
    }
}
