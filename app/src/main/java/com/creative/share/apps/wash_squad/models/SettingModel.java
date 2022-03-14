package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class SettingModel implements Serializable {
    private String twitter;
    private String instagram;
    private String whatsapp;
    private String snapchat;
    private About about;
    private Conditions conditions;
    private String delay_order_sub_limit;
    private double tax_per;

    public String getTwitter() {
        return twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public About getAbout() {
        return about;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public String getDelay_order_sub_limit() {
        return delay_order_sub_limit;
    }

    public double getTax_per() {
        return tax_per;
    }

    public class About implements Serializable{
        private int id;
        private String link;
        private String en_title;
        private String ar_title;
        private String ar_content;
        private String en_content;
        private String image;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getLink() {
            return link;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }

        public String getImage() {
            return image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

    public class Conditions implements Serializable{
        private int id;
        private String link;
        private String en_title;
        private String ar_title;
        private String ar_content;
        private String en_content;
        private String image;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getLink() {
            return link;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }

        public String getImage() {
            return image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
