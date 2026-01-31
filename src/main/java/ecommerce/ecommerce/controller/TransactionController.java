package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.security.CustomUserDetails;
import ecommerce.ecommerce.service.TransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<TransactionDTO> getAllTransactions() {

        return transactionService.findAllTransactions()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // CUSTOMER or ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/my")
    public List<TransactionDTO> getMyTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return transactionService
                .findTransactionsByUser(userDetails.getUser())
                .stream()
                .map(this::toDTO)
                .toList();
    }


    // Mapper (private – clean)
    private TransactionDTO toDTO(Transaction tx) {
        return new TransactionDTO(
                tx.getTransactionId(),
                tx.getTransactionDate(),
                tx.getTransactionInfo(),
                tx.getUser() != null ? tx.getUser().getName() : null,
                tx.getUser() != null ? tx.getUser().getEmail() : null
        );
    }
}
