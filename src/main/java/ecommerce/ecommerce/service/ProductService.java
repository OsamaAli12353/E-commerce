package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Products;
import ecommerce.ecommerce.entity.Users;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

public interface ProductService {
    Products findProductById(int id);
    List<Products> findAllProducts();
    void addOrUpdateProduct(Products product);
    void deleteProductById(int id);
    public String buyProducts(Users user, Products product, int quantity);
}
