package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class QuestionDataModel implements Serializable {

    private List<QuestionModel> data;

    public List<QuestionModel> getData() {
        return data;
    }

    public class QuestionModel implements Serializable
    {
        private int id;
        private String en_title;
        private String ar_title;
        private String ar_content;
        private String en_content;

        public int getId() {
            return id;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }
    }
}
