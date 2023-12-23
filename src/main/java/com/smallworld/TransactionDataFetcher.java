package com.smallworld;

import com.smallworld.data.Transaction;
import java.io.IOException;
import java.util.*;

public class TransactionDataFetcher {

    private List<Transaction> transactions;

    public TransactionDataFetcher(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public TransactionDataFetcher(String fileName) throws IOException {
        this.transactions = TransactionUtils.loadFromJson(fileName);
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        double sum = 0.0;
        for (Transaction transaction : transactions) {
            sum += transaction.getAmount();
        }
        return sum;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        double sum = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getSenderFullName().equals(senderFullName)) {
                sum += transaction.getAmount();
            }
        }
        return sum;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        double maxAmount = 0.0;
        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            if (amount > maxAmount) {
                maxAmount = amount;
            }
        }
        return maxAmount;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> uniqueClients = new HashSet<>();
        for (Transaction transaction : transactions) {
            uniqueClients.add(transaction.getSenderFullName());
            uniqueClients.add(transaction.getBeneficiaryFullName());
        }
        return uniqueClients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        for (Transaction transaction : transactions) {
            if ((transaction.getSenderFullName().equals(clientFullName) || transaction.getBeneficiaryFullName().equals(clientFullName))
                    && !transaction.isIssueSolved()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        Map<String, Transaction> indexedTransactions = new HashMap<>();
        for (Transaction transaction : transactions) {
            indexedTransactions.put(transaction.getBeneficiaryFullName(), transaction);
        }
        return indexedTransactions;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (!transaction.isIssueSolved() && transaction.getIssueId() != null) {
                unsolvedIssueIds.add(transaction.getIssueId());
            }
        }
        return unsolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.isIssueSolved() && transaction.getIssueMessage() != null) {
                solvedIssueMessages.add(transaction.getIssueMessage());
            }
        }
        return solvedIssueMessages;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        sortedTransactions.sort(Comparator.comparingDouble(Transaction::getAmount).reversed());
        return sortedTransactions.subList(0, Math.min(3, sortedTransactions.size()));
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> senderTotalAmounts = new HashMap<>();
        for (Transaction transaction : transactions) {
            String senderFullName = transaction.getSenderFullName();
            double amount = transaction.getAmount();
            senderTotalAmounts.merge(senderFullName, amount, Double::sum);
        }
        String topSender = null;
        double maxAmount = 0.0;
        for (Map.Entry<String, Double> entry : senderTotalAmounts.entrySet()) {
            if (entry.getValue() > maxAmount) {
                topSender = entry.getKey();
                maxAmount = entry.getValue();
            }
        }
        return Optional.ofNullable(topSender);
    }
}
