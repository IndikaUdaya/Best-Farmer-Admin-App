package com.indikaudaya.best_farmer_admin.dto;

import java.io.Serializable;

public class ProductImageDTO implements Serializable {
    private String path;

    public ProductImageDTO() {
    }

    public ProductImageDTO(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
