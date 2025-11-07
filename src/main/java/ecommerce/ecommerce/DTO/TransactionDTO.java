package ecommerce.ecommerce.DTO;

import java.util.Date;

public class TransactionDTO {
    private int transactionId;
    private Date transactionDate;
    private String transactionInfo;
    private String userName;
    private String userEmail;

    public TransactionDTO(int transactionId, Date transactionDate, String transactionInfo, String userName, String userEmail) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionInfo = transactionInfo;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    // getters & setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getTransactionInfo() { return transactionInfo; }
    public void setTransactionInfo(String transactionInfo) { this.transactionInfo = transactionInfo; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
