package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class ServiceDataModel implements Serializable {

    private List<ServiceModel> data;

    public List<ServiceModel> getData() {
        return data;
    }

    public class ServiceModel implements Serializable
    {
        private int id;
        private String ar_title;
        private String en_title;
        private String ar_des;
        private String en_des;
        private String ar_note;
        private String en_note;
        private String image;
        public String style_class;
        private String parent_id;
        private String level;
        private String price;
        public String count;
        private String is_gift;
        private String created_at;
        private String updated_at;
        private List<Level2> level2;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_des() {
            return ar_des;
        }

        public String getEn_des() {
            return en_des;
        }

        public String getImage() {
            return image;
        }

        public String getPrice() {
            return price;
        }

        public String getAr_note() {
            return ar_note;
        }

        public String getEn_note() {
            return en_note;
        }

        public String getStyle_class() {
            return style_class;
        }

        public String getParent_id() {
            return parent_id;
        }

        public String getLevel() {
            return level;
        }

        public String getCount() {
            return count;
        }

        public String getIs_gift() {
            return is_gift;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public List<Level2> getLevel2() {
            return level2;
        }
    }
    public class Level2 implements Serializable
    {
        private int id;
        private String ar_title;
        private String en_title;
        private String ar_des;
        private String en_des;
        private String image;
        private String price;
        private String parent_id;
        private List<Level3> level3;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_des() {
            return ar_des;
        }

        public String getEn_des() {
            return en_des;
        }

        public String getImage() {
            return image;
        }

        public String getPrice() {
            return price;
        }

        public String getParent_id() {
            return parent_id;
        }

        public List<Level3> getLevel3() {
            return level3;
        }
    }
    public class Level3 implements Serializable
    {
        private int id;
        private String ar_title;
        private String en_title;
        private String ar_des;
        private String en_des;
        private String image;
        private double price;
        private String parent_id;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_des() {
            return ar_des;
        }

        public String getEn_des() {
            return en_des;
        }

        public String getImage() {
            return image;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getParent_id() {
            return parent_id;
        }
    }
}
