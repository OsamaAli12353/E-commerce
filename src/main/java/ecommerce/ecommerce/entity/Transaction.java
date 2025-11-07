package ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;

    @Column(name = "transaction_info")
    private String transactionInfo;

    public Transaction() {}

    public Transaction(Users user, Date transactionDate, String transactionInfo) {
        this.user = user;
        this.transactionDate = transactionDate;
        this.transactionInfo = transactionInfo;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getTransactionInfo() { return transactionInfo; }
    public void setTransactionInfo(String transactionInfo) { this.transactionInfo = transactionInfo; }
}
