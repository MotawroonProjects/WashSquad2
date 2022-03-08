package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class TimeDataModel implements Serializable {

    private List<TimeModel> data;

    public List<TimeModel> getData() {
        return data;
    }

    public static class TimeModel implements Serializable
    {
        private int id;
        private String time_text;
        private String type;
        private String status_en;
        private boolean isSelected = false;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getId() {
            return id;
        }

        public String getTime_text() {
            return time_text;
        }

        public String getType() {
            return type;
        }

        public String getStatus_en() {
            return status_en;
        }
    }
}
