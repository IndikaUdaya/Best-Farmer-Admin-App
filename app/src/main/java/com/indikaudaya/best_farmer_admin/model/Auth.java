package com.indikaudaya.best_farmer_admin.model;

import java.io.Serializable;

public class Auth implements Serializable {
    private String email;
    private String mobile;
    private String password;
    private boolean status;
    private String  userType;

    public Auth(String email, String mobile, String password, boolean status,String userType ) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.userType = userType;
        this.status = status;
    }

}
