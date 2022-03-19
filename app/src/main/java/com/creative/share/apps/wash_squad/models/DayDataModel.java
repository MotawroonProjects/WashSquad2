package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DayDataModel implements Serializable {
    private int id;
    private int service_id;
    private int place_id;
    private String created_at;
    private String updated_at;
    private ArrayList<String> days;

    public int getId() {
        return id;
    }

    public int getService_id() {
        return service_id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public ArrayList<String> getDays() {
        return days;
    }
}
