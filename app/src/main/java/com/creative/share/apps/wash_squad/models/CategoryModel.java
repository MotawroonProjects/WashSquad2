package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private String id;
    private String title_ar;
    private String title_en;
    private String created_at;
    private String updated_at;
    private boolean isSelected = false;

    public String getId() {
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
