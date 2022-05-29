package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class ProductDataModel implements Serializable {
    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }
}
