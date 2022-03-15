package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SubscribtionDataModel implements Serializable {

    private String id;
    private String order_type;
    private String user_id;
    private String marketer_id;
    private String employee_id;
    private String service_id;
    private String status;
    private String sub_service_id;
    private String size_id;
    private String brand_id;
    private String type_id;
    private String package_id;
    private String address;
    private String latitude;
    private String longitude;
    private String order_date;
    private String order_time_id;
    private String addons;
    private String number_of_cars;
    private String payment_method;
    private String driver_id;
    private String feedback;
    private String start_time_work;
    private String end_time_work;
    private String distributor_employee_id;
    private String cancel_reason;
    private String opinion_des;
    private String rating;
    private String total_price;
    private String coupon_serial;
    private String place_check;
    private String step;
    private String order_time;
    private String neighborhood;
    private String satisfied_status;
    private String created_at;
    private String updated_at;
    private String commission_value;
    private ArrayList<WashSub> wash_sub;

    public String getId() {
        return id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getMarketer_id() {
        return marketer_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public String getService_id() {
        return service_id;
    }

    public String getStatus() {
        return status;
    }

    public String getSub_service_id() {
        return sub_service_id;
    }

    public String getSize_id() {
        return size_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getType_id() {
        return type_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_time_id() {
        return order_time_id;
    }

    public String getAddons() {
        return addons;
    }

    public String getNumber_of_cars() {
        return number_of_cars;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getStart_time_work() {
        return start_time_work;
    }

    public String getEnd_time_work() {
        return end_time_work;
    }

    public String getDistributor_employee_id() {
        return distributor_employee_id;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public String getOpinion_des() {
        return opinion_des;
    }

    public String getRating() {
        return rating;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getCoupon_serial() {
        return coupon_serial;
    }

    public String getPlace_check() {
        return place_check;
    }

    public String getStep() {
        return step;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getSatisfied_status() {
        return satisfied_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCommission_value() {
        return commission_value;
    }

    public ArrayList<WashSub> getWash_sub() {
        return wash_sub;
    }

    public class WashSub implements Serializable {
        private String id;
        private String number_of_wash;
        private String order_id;
        private String wash_date;
        private String will_wash_date;
        private String status;
        private int time_dealy;
        private String created_at;
        private String updated_at;
        private String day;

        public String getId() {
            return id;
        }

        public String getNumber_of_wash() {
            return number_of_wash;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getWash_date() {
            return wash_date;
        }

        public String getWill_wash_date() {
            return will_wash_date;
        }

        public String getStatus() {
            return status;
        }

        public int getTime_dealy() {
            return time_dealy;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getDay() {
            return day;
        }

        public void setTime_dealy(int time_dealy) {
            this.time_dealy = time_dealy;
        }
    }
}
