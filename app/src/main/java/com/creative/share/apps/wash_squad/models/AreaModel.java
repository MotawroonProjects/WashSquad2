package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class AreaModel implements Serializable {
    private int id;
    private String title_ar;
    private String title_en;
    private String created_at;
    private String updated_at;

    public AreaModel(String title_ar, String title_en) {
        this.title_ar = title_ar;
        this.title_en = title_en;
    }

    public int getId() {
        return id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
