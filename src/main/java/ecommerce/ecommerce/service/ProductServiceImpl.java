package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Products;
import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private TransactionService transactionService;
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductServiceImpl(ProductsRepository productsRepository,TransactionService transactionService) {
        this.productsRepository = productsRepository;
        this.transactionService=transactionService;
    }

    @Override
    public Products findProductById(int id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override

    public List<Products> findAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    @Transactional
    public void addOrUpdateProduct(Products product) {
        productsRepository.save(product);
        System.out.println("Product saved successfully");
    }

    @Override
    @Transactional
    public void deleteProductById(int id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        productsRepository.delete(product);
        System.out.println("Product deleted successfully");
    }

    @Override
    @Transactional
    public String buyProducts(Users user, Products product, int quantity) {
        Products currentProduct = productsRepository.findById(product.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + product.getProductId()));

        int newQuantity = currentProduct.getQuantity() - quantity;

        if (newQuantity < 0)
            return "Not available";

        currentProduct.setQuantity(newQuantity);
        productsRepository.save(currentProduct);

        Transaction addTransaction = new Transaction();
        addTransaction.setTransactionDate(new Date());
        addTransaction.setTransactionInfo(currentProduct.getName() + " Quantity: " + quantity);
        addTransaction.setUser(user);

        transactionService.addOrUpdateTransaction(addTransaction);

        return "Success";
    }

}
