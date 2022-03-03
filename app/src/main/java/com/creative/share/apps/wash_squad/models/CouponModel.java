package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class CouponModel implements Serializable {

    private int id;
    //1 or 0
    private String status;
    //1 or 0
    private String is_active;

    private double ratio;

    private String coupon_serial;

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getIs_active() {
        return is_active;
    }

    public double getRatio() {
        return ratio;
    }

    public String getCoupon_serial() {
        return coupon_serial;
    }
}
