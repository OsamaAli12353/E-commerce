package ecommerce.ecommerce.repository;

import ecommerce.ecommerce.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products,Integer> {
}
