package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.TransactionDTO;
import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.security.CustomUserDetails;
import ecommerce.ecommerce.service.TransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing transaction-related operations in the e-commerce application.
 * Provides endpoints to view transaction history with role-based access control.
 * Transactions are returned as DTOs to avoid exposing sensitive entity data.
 */
@RestController
@RequestMapping("/api/transactions") // Base URL for all transaction endpoints
public class TransactionController {

    // Service layer handling business logic for transaction operations
    private final TransactionService transactionService;

    /**
     * Constructor injection of TransactionService dependency.
     *
     * @param transactionService Service for handling transaction operations
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves all transactions in the system.
     * ADMIN ONLY - Only administrators can view all transactions.
     * GET /api/transactions
     *
     * @return List of all transactions as DTOs (Data Transfer Objects)
     */
    @PreAuthorize("hasRole('ADMIN')") // Authorization check - restricts access to admins only
    @GetMapping
    public List<TransactionDTO> getAllTransactions() {

        // Fetch all transactions from the service layer
        // Convert each Transaction entity to TransactionDTO using stream mapping
        // This prevents exposing sensitive entity data and provides a clean API response
        return transactionService.findAllTransactions()
                .stream()
                .map(this::toDTO) // Transform each entity to DTO
                .toList(); // Collect results into an immutable list
    }

    /**
     * Retrieves transactions for the currently authenticated user.
     * CUSTOMER or ADMIN - Both roles can view their own transaction history.
     * GET /api/transactions/my
     *
     * @param userDetails Automatically injected authenticated user details from JWT token
     * @return List of user's transactions as DTOs
     */
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')") // Authorization check - authenticated users can view their own transactions
    @GetMapping("/my")
    public List<TransactionDTO> getMyTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Fetch transactions belonging to the authenticated user
        // The user entity is extracted from the JWT token via CustomUserDetails
        return transactionService
                .findTransactionsByUser(userDetails.getUser())
                .stream()
                .map(this::toDTO) // Convert entities to DTOs
                .toList(); // Return as immutable list
    }


    /**
     * Private mapper method to convert Transaction entity to TransactionDTO.
     * This prevents direct exposure of entity objects and provides control over
     * what data is returned in API responses.
     *
     * Includes null-safety checks for user information to prevent NullPointerException
     * in cases where a transaction might not have an associated user.
     *
     * @param tx The Transaction entity to convert
     * @return TransactionDTO containing transaction details and user info
     */
    private TransactionDTO toDTO(Transaction tx) {
        return new TransactionDTO(
                tx.getTransactionId(),
                tx.getTransactionDate(),
                tx.getTransactionInfo(),
                // Null-safe extraction of user name - returns null if user is not present
                tx.getUser() != null ? tx.getUser().getName() : null,
                // Null-safe extraction of user email - returns null if user is not present
                tx.getUser() != null ? tx.getUser().getEmail() : null
        );
    }
}