package com.indikaudaya.best_farmer_admin.model;

import com.indikaudaya.best_farmer_admin.dto.ProductImageDTO;
import com.indikaudaya.best_farmer_admin.dto.UserDTO;

import java.io.Serializable;
import java.util.List;

public class PopularFood implements Serializable {

    private long productId;
    private String title;
    private String description;
    private List<ProductImageDTO> productImageList;
    private double price;
    private int reviewCount;
    private double ratingScore;

    private UserDTO seller;

    private int cartCount;
    private int watchlistCount;

    private boolean deliveryOption;
    private String type;
    private String unit;
    private String categoryNameForUpdate;

    private double qty;

    private boolean deliveryProduct;


    public PopularFood(long productId, String title, String description, String imageId, double price, int reviewCount, double ratingScore, int cartCount, UserDTO seller, double pqty) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.reviewCount = reviewCount;
        this.ratingScore = ratingScore;
        this.cartCount = cartCount;
        this.seller = seller;
        this.qty = pqty;
    }

    public PopularFood(long productId, String title, String description, double price, int reviewCount, double ratingScore, int cartCount, List<ProductImageDTO> productImageList, UserDTO seller, double pqty) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.reviewCount = reviewCount;
        this.ratingScore = ratingScore;
        this.cartCount = cartCount;
        this.productImageList = productImageList;
        this.seller = seller;
        this.qty = pqty;
    }

    public boolean isDeliveryProduct() {
        return deliveryProduct;
    }

    public void setDeliveryProduct(boolean deliveryProduct) {
        this.deliveryProduct = deliveryProduct;
    }

    public String getCategoryNameForUpdate() {
        return categoryNameForUpdate;
    }

    public void setCategoryNameForUpdate(String categoryNameForUpdate) {
        this.categoryNameForUpdate = categoryNameForUpdate;
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

    public boolean isDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(boolean deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ProductImageDTO> getProductImageList() {
        return productImageList;
    }

    public void setProductImageList(List<ProductImageDTO> productImageList) {
        this.productImageList = productImageList;
    }

    public int getWatchlistCount() {
        return watchlistCount;
    }

    public void setWatchlistCount(int watchlistCount) {
        this.watchlistCount = watchlistCount;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public UserDTO getSeller() {
        return seller;
    }

    public void setSeller(UserDTO seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "PopularFood{" +
                "productId=" + productId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", productImageList=" + productImageList +
                ", price=" + price +
                ", reviewCount=" + reviewCount +
                ", ratingScore=" + ratingScore +
                ", seller=" + seller +
                ", cartCount=" + cartCount +
                ", watchlistCount=" + watchlistCount +
                ", deliveryOption=" + deliveryOption +
                ", type='" + type + '\'' +
                ", unit='" + unit + '\'' +
                ", categoryNameForUpdate='" + categoryNameForUpdate + '\'' +
                ", qty=" + qty +
                '}';
    }
}
