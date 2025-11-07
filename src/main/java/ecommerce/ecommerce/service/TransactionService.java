package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Transaction;
import java.util.Date;
import java.util.List;

public interface TransactionService {
    Transaction findTransactionById(int id);
    List<Transaction> findAllTransactions();
    void addOrUpdateTransaction(Transaction transaction);
    void deleteTransactionById(int id);
    List<Transaction> findTransactionsByDate(Date date);
}
