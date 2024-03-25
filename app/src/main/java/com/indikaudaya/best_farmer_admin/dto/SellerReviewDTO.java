package com.indikaudaya.best_farmer_admin.dto;

public class SellerReviewDTO {

    private Long id;

    private String comment;
    private float rating;
    private UserDTO buyer;
    private UserDTO seller;
    private String reviewDate;
    private ProductDTO product;

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public UserDTO getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDTO buyer) {
        this.buyer = buyer;
    }

    public UserDTO getSeller() {
        return seller;
    }

    public void setSeller(UserDTO seller) {
        this.seller = seller;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "SellerReviewDTO{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", reviewDate='" + reviewDate + '\'' +
                '}';
    }
}
