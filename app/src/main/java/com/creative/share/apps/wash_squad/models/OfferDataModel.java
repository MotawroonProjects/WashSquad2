package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class OfferDataModel implements Serializable {

    private List<OfferModel> data;

    public List<OfferModel> getData() {
        return data;
    }

    public class OfferModel implements Serializable {
        private String ar_image;
        private String en_image;
        private int service_id;

        public String getAr_image() {
            return ar_image;
        }

        public void setAr_image(String ar_image) {
            this.ar_image = ar_image;
        }

        public String getEn_image() {
            return en_image;
        }

        public void setEn_image(String en_image) {
            this.en_image = en_image;
        }

        public int getService_id() {
            return service_id;
        }
    }
}
