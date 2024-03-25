package com.indikaudaya.best_farmer_admin.model;

import java.io.Serializable;

public class ApiUserDetails implements Serializable {

    final private long id;
    final private String email;

    public ApiUserDetails(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
