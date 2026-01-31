package ecommerce.ecommerce.repository;

import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByTransactionDate(Date date);
    List<Transaction> findByUser(User user);

}
