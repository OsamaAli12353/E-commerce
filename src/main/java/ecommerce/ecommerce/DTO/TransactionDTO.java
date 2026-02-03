package ecommerce.ecommerce.DTO;

import java.util.Date;

/**
 * Data Transfer Object for transaction information.
 * Contains transaction details along with associated user information.
 */
public class TransactionDTO {
    // Unique identifier for the transaction
    private int transactionId;

    // Date and time when the transaction occurred
    private Date transactionDate;

    // Details about the transaction (e.g., products purchased, quantities)
    private String transactionInfo;

    // Name of the user who made the transaction
    private String userName;

    // Email of the user who made the transaction
    private String userEmail;

    // Constructor
    public TransactionDTO(int transactionId, Date transactionDate, String transactionInfo, String userName, String userEmail) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionInfo = transactionInfo;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    // Getters and Setters

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}