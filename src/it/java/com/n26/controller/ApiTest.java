package com.n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.payload.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateTransaction_should_return_400_status() throws Exception {
        String url = "/transactions";
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100.50))
                .build();
        String content = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(TransactionController.class))
                .andExpect(handler().method(TransactionController.class.getMethod("createTransaction", Transaction.class)))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEmpty();
    }

    @Test
    public void testCreateTransaction_should_return_204_status_when_timestamp_old() throws Exception {
        String url = "/transactions";
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100.50))
                .timestamp(Date.from(Instant.now().minus(2, ChronoUnit.MINUTES)))
                .build();
        String content = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is(204))
                .andExpect(handler().handlerType(TransactionController.class))
                .andExpect(handler().method(TransactionController.class.getMethod("createTransaction", Transaction.class)))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEmpty();
    }

    @Test
    public void testCreateTransaction_should_return_422_status_when_timestamp_wrong_format() throws Exception {
        String url = "/transactions";
        String content = "{\"timestamp\":\"4/23/2018 11:32 PM\",\"amount\":\"262.01\"}";

        MvcResult mvcResult = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is(422))
                .andExpect(handler().handlerType(TransactionController.class))
                .andExpect(handler().method(TransactionController.class.getMethod("createTransaction", Transaction.class)))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEmpty();
    }

    @Test
    public void testCreateTransaction_should_return_422_status_when_timestamp_in_future() throws Exception {
        String url = "/transactions";
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100.50))
                .timestamp(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .build();
        String content = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is(422))
                .andExpect(handler().handlerType(TransactionController.class))
                .andExpect(handler().method(TransactionController.class.getMethod("createTransaction", Transaction.class)))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEmpty();
    }

    @Test
    public void testCreateTransaction_should_return_201() throws Exception {
        String url = "/transactions";
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100.50))
                .timestamp(Date.from(Instant.now()))
                .build();
        String content = objectMapper.writeValueAsString(transaction);

        MvcResult mvcResult = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().is(201))
                .andExpect(handler().handlerType(TransactionController.class))
                .andExpect(handler().method(TransactionController.class.getMethod("createTransaction", Transaction.class)))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetTransactions() throws Exception {
        String url = "/statistics";
        MvcResult mvcResult = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(handler().handlerType(StatisticsController.class))
                .andExpect(handler().method(StatisticsController.class.getMethod("getStatistics")))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEqualTo("{\"sum\":\"0.00\",\"avg\":\"0.00\",\"max\":\"0.00\",\"min\":\"0.00\",\"count\":0}");
    }

    @Test
    public void testDeleteTransactions() throws Exception {
        String url = "/transactions";
        MvcResult mvcResult = this.mockMvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(204))
                .andExpect(handler().handlerType(TransactionController.class))
                .andExpect(handler().method(TransactionController.class.getMethod("deleteTransaction")))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertThat(result).isEmpty();
    }


    @Test
    public void name() {

    }
}