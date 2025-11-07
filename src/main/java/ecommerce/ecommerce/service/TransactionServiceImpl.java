package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction findTransactionById(int id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public void addOrUpdateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }

    @Override
    public void deleteTransactionById(int id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
        transactionRepository.delete(transaction);
        System.out.println("Transaction deleted successfully");
    }

    @Override
    public List<Transaction> findTransactionsByDate(Date date) {
        return transactionRepository.findByTransactionDate(date);
    }
}
