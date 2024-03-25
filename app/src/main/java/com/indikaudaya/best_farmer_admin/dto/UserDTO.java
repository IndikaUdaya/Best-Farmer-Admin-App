package com.indikaudaya.best_farmer_admin.dto;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String shopAddress;
    private String mobile;
    private String password;

    private List<SellerReviewDTO> buyerReview;

    private boolean status;


    private List<ProductDTO> products;

    private String userType;


    public UserDTO() {
    }

    public List<SellerReviewDTO> getBuyerReview() {
        return buyerReview;
    }

    public void setBuyerReview(List<SellerReviewDTO> buyerReview) {
        this.buyerReview = buyerReview;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public UserDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
