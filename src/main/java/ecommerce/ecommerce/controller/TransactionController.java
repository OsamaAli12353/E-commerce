package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAllTransactions();

        return transactions.stream()
                .map(tx -> new TransactionDTO(
                        tx.getTransactionId(),
                        tx.getTransactionDate(),
                        tx.getTransactionInfo(),
                        tx.getUser() != null ? tx.getUser().getName() : null,
                        tx.getUser() != null ? tx.getUser().getEmail() : null
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable int id) {
        Transaction tx = transactionService.findTransactionById(id);
        if (tx == null) {
            throw new RuntimeException("Transaction with ID " + id + " not found");
        }

        return new TransactionDTO(
                tx.getTransactionId(),
                tx.getTransactionDate(),
                tx.getTransactionInfo(),
                tx.getUser() != null ? tx.getUser().getName() : null,
                tx.getUser() != null ? tx.getUser().getEmail() : null
        );
    }

    @GetMapping("/by-date")
    public List<TransactionDTO> getTransactionsByDate(@RequestParam Date date) {
        List<Transaction> transactions = transactionService.findTransactionsByDate(date);
        return transactions.stream()
                .map(tx -> new TransactionDTO(
                        tx.getTransactionId(),
                        tx.getTransactionDate(),
                        tx.getTransactionInfo(),
                        tx.getUser() != null ? tx.getUser().getName() : null,
                        tx.getUser() != null ? tx.getUser().getEmail() : null
                ))
                .toList();
    }

    @PostMapping
    public String addTransaction(@RequestBody Transaction transaction) {
        transactionService.addOrUpdateTransaction(transaction);
        return "Transaction added successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable int id) {
        transactionService.deleteTransactionById(id);
        return "Transaction deleted successfully";
    }
}
