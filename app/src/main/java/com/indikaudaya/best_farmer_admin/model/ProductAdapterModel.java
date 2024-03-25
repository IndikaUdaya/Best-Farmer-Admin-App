package com.indikaudaya.best_farmer_admin.model;

import com.indikaudaya.best_farmer_admin.dto.ProductImageDTO;

import java.util.List;

public class ProductAdapterModel {

    private Long pid;
    private String name, email, mobile;
    private PopularFood popularFood;
    private List<ProductImageDTO> productImages;
    boolean status;

    public ProductAdapterModel(String name, String email, String mobile, boolean status) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
    }

    public PopularFood getPopularFood() {
        return popularFood;
    }

    public void setPopularFood(PopularFood popularFood) {
        this.popularFood = popularFood;
    }

    public List<ProductImageDTO> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageDTO> productImages) {
        this.productImages = productImages;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BuyerAdapterModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                '}';
    }
}
