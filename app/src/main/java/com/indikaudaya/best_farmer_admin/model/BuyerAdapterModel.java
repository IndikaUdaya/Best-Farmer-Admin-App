package com.indikaudaya.best_farmer_admin.model;

public class BuyerAdapterModel {

   private String name,email,mobile;
   boolean status;

    public BuyerAdapterModel(String name, String email, String mobile, boolean status) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
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
