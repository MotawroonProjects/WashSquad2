package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class AreaModel implements Serializable {
    private int id;
    private String ar_name;
    private String en_name;
    private String created_at;
    private String updated_at;

    public AreaModel(String title_ar, String en_name) {
        this.ar_name = title_ar;
        this.en_name = en_name;
    }

    public int getId() {
        return id;
    }

    public String getAr_name() {
        return ar_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
