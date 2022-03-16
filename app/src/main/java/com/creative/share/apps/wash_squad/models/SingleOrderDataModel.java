package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class SingleOrderDataModel implements Serializable {

   private Order_Data_Model.OrderModel data;
   private String url;

    public Order_Data_Model.OrderModel getData() {
        return data;
    }

    public String getUrl() {
        return url;
    }
}
