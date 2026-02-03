package ecommerce.ecommerce.DTO;

/**
 * Data Transfer Object for product information.
 * Used to transfer product data between layers without exposing the entity.
 */
public class ProductsDTO {
    // Unique identifier for the product
    private int productId;

    // Product name
    private String name;

    // Product price
    private float price;

    // Available quantity in stock
    private int quantity;

    // Default constructor
    public ProductsDTO() {
    }

    // Constructor without productId (for creating new products)
    public ProductsDTO(String name, float price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}