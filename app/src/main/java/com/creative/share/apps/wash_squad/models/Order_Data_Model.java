package com.creative.share.apps.wash_squad.models;

import java.io.Serializable;
import java.util.List;

public class Order_Data_Model implements Serializable {
    private int current_page;
    private List<OrderModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<OrderModel> getData() {
        return data;
    }

    public static class OrderModel implements Serializable {
        private int id;
        private int order_type;
        private int user_id;
        private String marketer_id;
        private String employee_id;
        private int service_id;
        private int size_id;
        private int type_id;
        private String package_id;
        private String address;
        private double latitude;
        private double longitude;
        private long order_date;
        private int order_time_id;
        private String addons;
        private String number_of_cars;
        private String payment_method;
        private int driver_id;
        private String feedback;
        private long start_time_work;
        private long end_time_work;
        private int status;
        private String distributor_employee_id;
        private int cancel_reason;
        private String opinion_des;
        private double rating;
        private double total_price;
        private double total_tax;
        private String coupon_serial;
        private String created_at;
        private String updated_at;
        private String driver_full_name;
        private String user_image;
        private String user_full_name;
        private String service_en_title;
        private String service_ar_title;
        private String service_image;
        private String carSize_en_title;
        private String carSize_ar_title;
        private String carSize_image;
        private String cancel_en_title;
        private String cancel_ar_title;
        private String carType_en_title;
        private String carType__ar_title;
        private String carType__image;
        private String work_time_choosen;
        private String work_time_en_title;
        private String work_time_ar_title;
        private String service_level2_en_title;
        private String service_level2_ar_title;
        private String brand_en_title;
        private String brand__ar_title;
        private String see_images;
        private List<Products> order_sub_services;

        public int getId() {
            return id;
        }

        public int getOrder_type() {
            return order_type;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getMarketer_id() {
            return marketer_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public int getService_id() {
            return service_id;
        }

        public int getSize_id() {
            return size_id;
        }

        public int getType_id() {
            return type_id;
        }

        public String getPackage_id() {
            return package_id;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public long getOrder_date() {
            return order_date;
        }

        public int getOrder_time_id() {
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

        public int getDriver_id() {
            return driver_id;
        }

        public String getFeedback() {
            return feedback;
        }

        public long getStart_time_work() {
            return start_time_work;
        }

        public long getEnd_time_work() {
            return end_time_work;
        }

        public int getStatus() {
            return status;
        }

        public String getDistributor_employee_id() {
            return distributor_employee_id;
        }

        public int getCancel_reason() {
            return cancel_reason;
        }

        public String getOpinion_des() {
            return opinion_des;
        }

        public double getRating() {
            return rating;
        }

        public double getTotal_price() {
            return total_price;
        }

        public double getTotal_tax() {
            return total_tax;
        }

        public String getCoupon_serial() {
            return coupon_serial;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getDriver_full_name() {
            return driver_full_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public String getUser_full_name() {
            return user_full_name;
        }

        public String getService_en_title() {
            return service_en_title;
        }

        public String getService_ar_title() {
            return service_ar_title;
        }

        public String getService_image() {
            return service_image;
        }

        public String getCarSize_en_title() {
            return carSize_en_title;
        }

        public String getCarSize_ar_title() {
            return carSize_ar_title;
        }

        public String getCarSize_image() {
            return carSize_image;
        }

        public String getCancel_en_title() {
            return cancel_en_title;
        }

        public String getCancel_ar_title() {
            return cancel_ar_title;
        }

        public String getCarType_en_title() {
            return carType_en_title;
        }

        public String getCarType__ar_title() {
            return carType__ar_title;
        }

        public String getCarType__image() {
            return carType__image;
        }

        public String getWork_time_choosen() {
            return work_time_choosen;
        }

        public String getWork_time_en_title() {
            return work_time_en_title;
        }

        public String getWork_time_ar_title() {
            return work_time_ar_title;
        }

        public String getService_level2_en_title() {
            return service_level2_en_title;
        }

        public String getService_level2_ar_title() {
            return service_level2_ar_title;
        }

        public String getBrand_en_title() {
            return brand_en_title;
        }

        public String getBrand__ar_title() {
            return brand__ar_title;
        }

        public String getSee_images() {
            return see_images;
        }

        public List<Products> getOrder_sub_services() {
            return order_sub_services;
        }

        private List<order_images> order_images;

        public List<OrderModel.order_images> getOrder_images() {
            return order_images;
        }

        public static class order_images implements Serializable {
            private int id;
            private int order_id;
            private String image;
            private String type;

            public int getId() {
                return id;
            }

            public int getOrder_id() {
                return order_id;
            }

            public String getImage() {
                return image;
            }

            public String getType() {
                return type;
            }
        }
    }

    public static class Products implements Serializable {
        private int id;
        private int order_id;
        private int sub_service_id;
        private double price;
        private String sub_service_en_title;
        private String sub_service_ar_title;
        private String sub_service_image;
        private int sub_service_price;
        private int sub_service_parent_id;
        private int sub_service_level;

        public int getId() {
            return id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public int getSub_service_id() {
            return sub_service_id;
        }

        public double getPrice() {
            return price;
        }

        public String getSub_service_en_title() {
            return sub_service_en_title;
        }

        public String getSub_service_ar_title() {
            return sub_service_ar_title;
        }

        public String getSub_service_image() {
            return sub_service_image;
        }

        public int getSub_service_price() {
            return sub_service_price;
        }

        public int getSub_service_parent_id() {
            return sub_service_parent_id;
        }

        public int getSub_service_level() {
            return sub_service_level;
        }
    }

}