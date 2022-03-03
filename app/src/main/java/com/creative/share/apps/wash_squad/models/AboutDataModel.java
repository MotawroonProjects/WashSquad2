package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;

public class AboutDataModel implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable
    {
        private Condition conditions;

        public Condition getConditions() {
            return conditions;
        }
    }
    public class Condition implements Serializable
    {
        private String ar_content;
        private String en_content;

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }
    }
}
