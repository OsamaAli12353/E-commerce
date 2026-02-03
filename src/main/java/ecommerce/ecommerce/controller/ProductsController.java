package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.BuyRequestDTO;
import ecommerce.ecommerce.entity.Products;
import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.security.CustomUserDetails;
import ecommerce.ecommerce.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing product-related operations in the e-commerce application.
 * Handles CRUD operations for products and purchase functionality.
 * Access control is enforced based on user roles (ADMIN, CUSTOMER).
 */
@RestController
@RequestMapping("/api/products") // Base URL for all product endpoints
public class ProductsController {

    // Service layer handling business logic for product operations
    private final ProductService productService;

    /**
     * Constructor injection of ProductService dependency.
     *
     * @param productService Service for handling product operations
     */
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products in the system.
     * PUBLIC ENDPOINT - No authentication required.
     * GET /api/products/all
     *
     * @return List of all available products
     */
    @GetMapping("/all")
    public List<Products> getAllProducts() {
        return productService.findAllProducts();
    }

    /**
     * Retrieves a specific product by its ID.
     * PUBLIC ENDPOINT - No authentication required.
     * GET /api/products/{id}
     *
     * @param id The unique identifier of the product
     * @return The product with the specified ID
     */
    @GetMapping("/{id}")
    public Products getProductById(@PathVariable int id) {
        return productService.findProductById(id);
    }

    /**
     * Adds a new product to the inventory.
     * ADMIN ONLY - Requires ADMIN role to access.
     * POST /api/products/add
     *
     * @param product The product details to be added
     * @return Success message
     */
    @PreAuthorize("hasRole('ADMIN')") // Authorization check - only admins can add products
    @PostMapping("/add")
    public String addProduct(@RequestBody Products product) {
        productService.addOrUpdateProduct(product);
        return "Product added successfully";
    }

    /**
     * Updates an existing product's details.
     * ADMIN ONLY - Requires ADMIN role to access.
     * PUT /api/products/update/{id}
     *
     * @param id The ID of the product to update
     * @param updatedProduct The new product details
     * @return Success message
     */
    @PreAuthorize("hasRole('ADMIN')") // Authorization check - only admins can update products
    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable int id,
                                @RequestBody Products updatedProduct) {

        // Retrieve the existing product from the database
        Products product = productService.findProductById(id);

        // Update product fields with new values
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());

        // Persist the updated product to the database
        productService.addOrUpdateProduct(product);
        return "Product updated successfully";
    }

    /**
     * Deletes a product from the inventory.
     * ADMIN ONLY - Requires ADMIN role to access.
     * DELETE /api/products/delete/{id}
     *
     * @param id The ID of the product to delete
     * @return Success message
     */
    @PreAuthorize("hasRole('ADMIN')") // Authorization check - only admins can delete products
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return "Product deleted successfully";
    }

    /**
     * Handles product purchase requests from authenticated users.
     * CUSTOMER or ADMIN - Both roles can purchase products.
     * POST /api/products/buy
     *
     * @param request DTO containing product ID and quantity to purchase
     * @param userDetails Automatically injected authenticated user details from JWT token
     * @return Result message of the purchase operation
     */
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')") // Authorization check - authenticated users can buy
    @PostMapping("/buy")
    public String buyProduct(@RequestBody BuyRequestDTO request,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Extract the User entity from the authenticated user details (obtained from JWT)
        User user = userDetails.getUser();

        // Retrieve the product being purchased
        Products product = productService.findProductById(request.getProductId());

        // Process the purchase transaction (inventory check, payment, order creation, etc.)
        return productService.buyProducts(
                user, product, request.getQuantity());
    }
}