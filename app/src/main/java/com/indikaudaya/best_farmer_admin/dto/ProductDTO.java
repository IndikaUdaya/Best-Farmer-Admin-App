package com.indikaudaya.best_farmer_admin.dto;

import java.util.List;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int reviewCount;
    private double ratingScore;
    private int cartCount;
    private int watchlistCount;
    private double qty;
    private String unit;
    private String type;
    private boolean deliveryAvailable;
    private CategoryDTO category;
    private List<ProductImageDTO> productImages;
    private UserDTO seller;
    private boolean status;
    private long orderId;
    private double orderQty;
    private String orderDate;
    private boolean productDelivered;

    public ProductDTO(Long id) {
        this.id = id;
    }

    public ProductDTO(String name, String description, double price, double qty, String unit, String type, boolean deliveryAvailable, CategoryDTO category, List<ProductImageDTO> productImages, UserDTO seller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.unit = unit;
        this.type = type;
        this.deliveryAvailable = deliveryAvailable;
        this.category = category;
        this.productImages = productImages;
        this.seller = seller;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isProductDelivered() {
        return productDelivered;
    }

    public void setProductDelivered(boolean productDelivered) {
        this.productDelivered = productDelivered;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getWatchlistCount() {
        return watchlistCount;
    }

    public void setWatchlistCount(int watchlistCount) {
        this.watchlistCount = watchlistCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(double ratingScore) {
        this.ratingScore = ratingScore;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDeliveryAvailable() {
        return deliveryAvailable;
    }

    public void setDeliveryAvailable(boolean deliveryAvailable) {
        this.deliveryAvailable = deliveryAvailable;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<ProductImageDTO> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageDTO> productImages) {
        this.productImages = productImages;
    }

    public UserDTO getSeller() {
        return seller;
    }

    public void setSeller(UserDTO seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", reviewCount=" + reviewCount +
                ", ratingScore=" + ratingScore +
                ", cartCount=" + cartCount +
                ", qty=" + qty +
                ", unit='" + unit + '\'' +
                ", type='" + type + '\'' +
                ", deliveryAvailable=" + deliveryAvailable +
                ", category=" + category +
                ", productImages=" + productImages +
                ", seller=" + seller +
                '}';
    }
}
