package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class DayModel implements Serializable {
    private String day_text;
    private boolean isSelected = false;

    public DayModel(String day_text) {
        this.day_text = day_text;
    }

    public String getDay_text() {
        return day_text;
    }

    public void setDay_text(String day_text) {
        this.day_text = day_text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
