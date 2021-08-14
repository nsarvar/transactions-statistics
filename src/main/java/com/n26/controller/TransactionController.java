package com.n26.controller;

import com.n26.exception.TimestampInFutureException;
import com.n26.exception.WrongTimestampException;
import com.n26.payload.Transaction;
import com.n26.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

import static com.n26.service.TransactionService.PERIOD_IN_SECONDS;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createTransaction(@RequestBody @Valid Transaction transaction) {
        transactionService.addTransaction(transaction);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTransaction() {
        transactionService.deleteTransactions();
    }
}
