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

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    // Public
    @GetMapping("/all")
    public List<Products> getAllProducts() {
        return productService.findAllProducts();
    }

    // Public
    @GetMapping("/{id}")
    public Products getProductById(@PathVariable int id) {
        return productService.findProductById(id);
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addProduct(@RequestBody Products product) {
        productService.addOrUpdateProduct(product);
        return "Product added successfully";
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable int id,
                                @RequestBody Products updatedProduct) {

        Products product = productService.findProductById(id);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());

        productService.addOrUpdateProduct(product);
        return "Product updated successfully";
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return "Product deleted successfully";
    }

    // CUSTOMER or ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/buy")
    public String buyProduct(@RequestBody BuyRequestDTO request,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser(); // من JWT
        Products product = productService.findProductById(request.getProductId());

        return productService.buyProducts(
                user, product, request.getQuantity());
    }
}
