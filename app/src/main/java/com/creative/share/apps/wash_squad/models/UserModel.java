package com.creative.share.apps.wash_squad.models;


import java.io.Serializable;

public class UserModel implements Serializable {

    private int id;
    private String phone_code;
    private String phone;
    private String full_name;
    private String logo;
    private int user_type;
    private String password_token;
    private double wallet;

    public String getPassword_token() {
        return password_token;
    }

    public int getId() {
        return id;
    }


    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getLogo() {
        return logo;
    }

    public int getUser_type() {
        return user_type;
    }

    public double getWallet() {
        return wallet;
    }
}
