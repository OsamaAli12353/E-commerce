package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.DTO.BuyRequestDTO;
import ecommerce.ecommerce.entity.Products;
import ecommerce.ecommerce.entity.Users;
import ecommerce.ecommerce.service.ProductService;
import ecommerce.ecommerce.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductService productService;
    private final UserService userService;

    public ProductsController(ProductService productService,UserService userService) {
        this.productService = productService;
        this. userService=userService;
    }

    @GetMapping
    public List<Products> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public Products getProductById(@PathVariable int id) {
        return productService.findProductById(id);
    }

    @PostMapping
    public String addProduct(@RequestBody Products product) {
        productService.addOrUpdateProduct(product);
        return "Product added successfully";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable int id, @RequestBody Products updatedProduct) {
        Products existingProduct = productService.findProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        productService.addOrUpdateProduct(existingProduct);
        return "Product updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProductById(id);
        return "Product deleted successfully";
    }
    @PostMapping("/buy")
    public String buyProduct(@RequestBody BuyRequestDTO request) {
        Users user = userService.findUserById(request.getUserId());
        Products product = productService.findProductById(request.getProductId());

        if (user == null || product == null) {
            return "User or Product not found";
        }

        return productService.buyProducts(user, product, request.getQuantity());
    }

}
