package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class CarSizeDataModel implements Serializable {

    private List<CarSizeModel> data;

    public List<CarSizeModel> getData() {
        return data;
    }

    public class CarSizeModel implements Serializable
    {
        private int id;
        private String image;
        private String price;
        private String ar_title;
        private String en_title;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getPrice() {
            return price;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }
    }
}
