package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service implementation for transaction operations.
 * Handles CRUD operations and queries for purchase transactions.
 * All methods are transactional to ensure data consistency.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    // Constructor
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param id The transaction ID
     * @return The transaction entity
     * @throws RuntimeException if transaction not found
     */
    @Override
    public Transaction findTransactionById(int id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
    }

    /**
     * Retrieves all transactions from the database.
     *
     * @return List of all transactions
     */
    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Adds a new transaction or updates an existing one.
     *
     * @param transaction The transaction to save or update
     */
    @Override
    public void addOrUpdateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The transaction ID to delete
     * @throws RuntimeException if transaction not found
     */
    @Override
    public void deleteTransactionById(int id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
        transactionRepository.delete(transaction);
        System.out.println("Transaction deleted successfully");
    }

    /**
     * Finds all transactions on a specific date.
     *
     * @param date The transaction date
     * @return List of transactions on that date
     */
    @Override
    public List<Transaction> findTransactionsByDate(Date date) {
        return transactionRepository.findByTransactionDate(date);
    }

    /**
     * Finds all transactions for a specific user.
     *
     * @param user The user
     * @return List of user's transactions
     */
    public List<Transaction> findTransactionsByUser(User user) {
        return transactionRepository.findByUser(user);
    }
}