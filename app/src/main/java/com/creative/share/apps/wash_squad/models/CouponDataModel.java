package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class CouponDataModel implements Serializable {

    private List<CouponModel>  data;
    public List<CouponModel> getData() {
        return data;
    }

    public static class CouponModel implements Serializable
    {
        private int id;
        private String en_title;
        private String ar_title;
        private String en_des;
        private String ar_des;
        private String coupon_serial;
        private int is_active;
        private String image;

        public int getId() {
            return id;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_des() {
            return en_des;
        }

        public String getAr_des() {
            return ar_des;
        }

        public String getCoupon_serial() {
            return coupon_serial;
        }

        public int getIs_active() {
            return is_active;
        }

        public String getImage() {
            return image;
        }
    }
}
