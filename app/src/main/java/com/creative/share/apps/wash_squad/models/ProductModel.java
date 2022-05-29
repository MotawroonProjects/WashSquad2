package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String id;
    private String image;
    private String title_ar;
    private String title_en;
    private String category_id;
    private String price;
    private String is_low_price;
    private String low_price_value;
    private String desc_ar;
    private String desc_en;
    private String linkk;
    private String created_at;
    private String updated_at;
    private CategoryModel category;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getPrice() {
        return price;
    }

    public String getIs_low_price() {
        return is_low_price;
    }

    public String getLow_price_value() {
        return low_price_value;
    }

    public String getDesc_ar() {
        return desc_ar;
    }

    public String getDesc_en() {
        return desc_en;
    }

    public String getLinkk() {
        return linkk;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public CategoryModel getCategory() {
        return category;
    }
}
