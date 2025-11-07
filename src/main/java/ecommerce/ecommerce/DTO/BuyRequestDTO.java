package ecommerce.ecommerce.DTO;

import ecommerce.ecommerce.entity.Products;
import ecommerce.ecommerce.entity.Users;

public class BuyRequestDTO {
    private int userId;
    private int productId;
    private int quantity;

    // getters and setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
