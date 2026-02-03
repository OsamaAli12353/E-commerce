package ecommerce.ecommerce.service;

import ecommerce.ecommerce.entity.Products;
import ecommerce.ecommerce.entity.Transaction;
import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service implementation for product operations.
 * Handles CRUD operations and purchase transactions for products.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private TransactionService transactionService;
    private final ProductsRepository productsRepository;

    // Constructor with dependency injection
    @Autowired
    public ProductServiceImpl(ProductsRepository productsRepository, TransactionService transactionService) {
        this.productsRepository = productsRepository;
        this.transactionService = transactionService;
    }

    /**
     * Finds a product by its ID.
     *
     * @param id The product ID
     * @return The product entity
     * @throws RuntimeException if product not found
     */
    @Override
    public Products findProductById(int id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all products
     */
    @Override
    public List<Products> findAllProducts() {
        return productsRepository.findAll();
    }

    /**
     * Adds a new product or updates an existing one.
     * Wrapped in a transaction to ensure data consistency.
     *
     * @param product The product to save or update
     */
    @Override
    @Transactional
    public void addOrUpdateProduct(Products product) {
        productsRepository.save(product);
        System.out.println("Product saved successfully");
    }

    /**
     * Deletes a product by its ID.
     * Wrapped in a transaction to ensure data consistency.
     *
     * @param id The product ID to delete
     * @throws RuntimeException if product not found
     */
    @Override
    @Transactional
    public void deleteProductById(int id) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        productsRepository.delete(product);
        System.out.println("Product deleted successfully");
    }

    /**
     * Processes a product purchase.
     * Updates product inventory and creates a transaction record.
     * Wrapped in a transaction to ensure atomicity.
     *
     * @param user The user making the purchase
     * @param product The product being purchased
     * @param quantity The quantity to purchase
     * @return "Success" if purchase completed, "Not available" if insufficient stock
     */
    @Override
    @Transactional
    public String buyProducts(User user, Products product, int quantity) {
        // Retrieve current product from database
        Products currentProduct = productsRepository.findById(product.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + product.getProductId()));

        // Calculate new quantity after purchase
        int newQuantity = currentProduct.getQuantity() - quantity;

        // Check if sufficient stock is available
        if (newQuantity < 0)
            return "Not available";

        // Update product quantity
        currentProduct.setQuantity(newQuantity);
        productsRepository.save(currentProduct);

        // Create transaction record
        Transaction addTransaction = new Transaction();
        addTransaction.setTransactionDate(new Date());
        addTransaction.setTransactionInfo(currentProduct.getName() + " Quantity: " + quantity);
        addTransaction.setUser(user);

        // Save transaction
        transactionService.addOrUpdateTransaction(addTransaction);

        return "Success";
    }
}