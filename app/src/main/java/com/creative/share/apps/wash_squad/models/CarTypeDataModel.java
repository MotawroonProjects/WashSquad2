package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class CarTypeDataModel implements Serializable {

    private List<CarTypeModel> data;

    public List<CarTypeModel> getData() {
        return data;
    }

    public static class CarTypeModel implements Serializable
    {
        private int id;
        private String ar_title;
        private String en_title;
        private String level;
        private List<CarBrandModel> level2;

        public CarTypeModel() {
        }

        public CarTypeModel(String ar_title, String en_title) {
            this.ar_title = ar_title;
            this.en_title = en_title;
        }

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getLevel() {
            return level;
        }

        public List<CarBrandModel> getLevel2() {
            return level2;
        }
    }

    public static class CarBrandModel implements Serializable
    {

        private int id;
        private String ar_title;
        private String en_title;
        private String parent_id;
        private String size;
        private String size_en_title;
        private String size_ar_title;
        private double size_price;
        private String size_image;

        public CarBrandModel() {
        }

        public CarBrandModel(String ar_title, String en_title) {
            this.ar_title = ar_title;
            this.en_title = en_title;
        }

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getParent_id() {
            return parent_id;
        }

        public String getSize() {
            return size;
        }

        public String getSize_en_title() {
            return size_en_title;
        }

        public String getSize_ar_title() {
            return size_ar_title;
        }

        public double getSize_price() {
            return size_price;
        }

        public String getSize_image() {
            return size_image;
        }
    }

}
