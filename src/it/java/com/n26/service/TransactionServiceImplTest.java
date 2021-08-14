package com.n26.service;

import com.n26.payload.Statistics;
import com.n26.payload.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TransactionServiceImplTest {
    @Autowired
    TransactionService transactionService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testStatistics() {
        List<Transaction> list = List.of(
                new Transaction(BigDecimal.valueOf(127.96), Date.from(Instant.now())),
                new Transaction(BigDecimal.valueOf(262.01), Date.from(Instant.now())),
                new Transaction(BigDecimal.valueOf(456.01), Date.from(Instant.now())),
                new Transaction(BigDecimal.valueOf(401.6), Date.from(Instant.now())),
                new Transaction(BigDecimal.valueOf(427.39), Date.from(Instant.now()))
        );

        list.forEach(transaction -> transactionService.addTransaction(transaction));
        Statistics statistics = transactionService.getStatistics();

        assertThat(statistics.getAvg()).isEqualTo(BigDecimal.valueOf(334.99));
        assertThat(statistics.getMax()).isEqualTo(BigDecimal.valueOf(456.01));
        assertThat(statistics.getMin()).isEqualTo(BigDecimal.valueOf(127.96));
        assertThat(statistics.getSum()).isEqualTo(BigDecimal.valueOf(1674.97));
        assertThat(statistics.getCount()).isEqualTo(5);

        transactionService.deleteTransactions();
        statistics = transactionService.getStatistics();
        assertThat(statistics.getAvg()).isEqualTo(BigDecimal.ZERO);
        assertThat(statistics.getMax()).isEqualTo(BigDecimal.ZERO);
        assertThat(statistics.getMin()).isEqualTo(BigDecimal.ZERO);
        assertThat(statistics.getSum()).isEqualTo(BigDecimal.ZERO);
        assertThat(statistics.getCount()).isEqualTo(0L);
    }
}