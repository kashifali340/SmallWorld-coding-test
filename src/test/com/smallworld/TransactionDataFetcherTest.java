package com.smallworld;

import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TransactionDataFetcherTest {

    private TransactionDataFetcher transactionDataFetcher;

    @BeforeEach
    void setUp() {
        try {
            transactionDataFetcher = new TransactionDataFetcher(TransactionUtils.loadFromJson("transactions.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTotalTransactionAmount() {
        double totalAmount = transactionDataFetcher.getTotalTransactionAmount();
        assertEquals(4371.37, totalAmount, 1);
    }

    @Test
    void getTotalTransactionAmountSentBy() {
        double totalAmountSentByTom = transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        assertEquals(828.2599999999999, totalAmountSentByTom, 1);
    }

    @Test
    void getMaxTransactionAmount() {
        double maxAmount = transactionDataFetcher.getMaxTransactionAmount();
        assertEquals(985.0, maxAmount, 0.001);
    }

    @Test
    void countUniqueClients() {
        long uniqueClients = transactionDataFetcher.countUniqueClients();
        assertEquals(14, uniqueClients);
    }

    @Test
    void hasOpenComplianceIssues() {
        assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Tom Shelby"));
        assertFalse(transactionDataFetcher.hasOpenComplianceIssues("Alfie Solomons"));
    }

    @Test
    void getTransactionsByBeneficiaryName() {
        Map<String, Transaction> transactionsByBeneficiary = transactionDataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(10, transactionsByBeneficiary.size());
        assertTrue(transactionsByBeneficiary.containsKey("Alfie Solomons"));
    }

    @Test
    void getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = transactionDataFetcher.getUnsolvedIssueIds();
        assertEquals(4, unsolvedIssueIds.size());
        assertTrue(unsolvedIssueIds.contains(3));
    }

    @Test
    void getAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = transactionDataFetcher.getAllSolvedIssueMessages();
        assertEquals(4, solvedIssueMessages.size());
    }

    @Test
    void getTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = transactionDataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(985.0, top3Transactions.get(0).getAmount(), 0.001);
    }

    @Test
    void getTopSender() {
        Optional<String> topSender = transactionDataFetcher.getTopSender();
        assertTrue(topSender.isPresent());
        assertEquals("Grace Burgess", topSender.get());
    }
}
