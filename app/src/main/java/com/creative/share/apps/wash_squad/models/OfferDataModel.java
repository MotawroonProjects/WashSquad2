package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class OfferDataModel implements Serializable {

    private List<OfferModel> data;

    public List<OfferModel> getData() {
        return data;
    }

    public class OfferModel implements Serializable {
        private String image;
        private int service_id;

        public String getImage() {
            return image;
        }

        public int getService_id() {
            return service_id;
        }
    }
}
