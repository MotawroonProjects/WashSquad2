package com.creative.share.apps.wash_squad.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.wash_squad.BR;
import com.creative.share.apps.wash_squad.R;

import java.io.Serializable;
import java.util.List;

public class SendServiceModel extends BaseObservable implements Serializable {
    private int user_id;
    private int service_id;
    private int sub_serv_id;
    private int carSize_id;
    private int carType_id;
    private int brand_id;
    private String en_brand_name;
    private String ar_brand_name;
    private String ar_car_type;
    private String en_car_type;
    private String sender_name;
    private String sender_phone;
    private String receiver_name;
    private String receiver_phone;
    private String order_date;
    private int order_time_id;
    private int payment_method;

    private double service_price;
    private double total_price;
    private int number_of_cars;

    private String coupon_serial;
    private String ar_service_type;
    private String en_service_type;
    private ServiceDataModel.Level2 level2;
    private List<ItemToUpload.SubServiceModel> sub_services;

    public ObservableField<String> user_name_error = new ObservableField<>();
    public ObservableField<String> user_phone_error = new ObservableField<>();
    public ObservableField<String> receiver_name_error = new ObservableField<>();
    public ObservableField<String> receiver_phone_error = new ObservableField<>();
    public ObservableField<String> date_error = new ObservableField<>();
    public ObservableField<String> time_error = new ObservableField<>();

    public boolean isDataValidStep1(Context context) {
        if (service_id != 0 &&
                sub_serv_id != 0 &&
                carSize_id != 0 &&
                carType_id != 0 &&
                brand_id != 0 &&
                !sender_name.isEmpty() &&
                !sender_phone.isEmpty() &&
                !receiver_name.isEmpty() &&
                !receiver_phone.isEmpty() &&
                order_time_id != 0 &&
                !order_date.isEmpty()


        ) {
            user_name_error.set(null);
            user_phone_error.set(null);
            receiver_name_error.set(null);
            receiver_phone_error.set(null);
            date_error.set(null);
            time_error.set(null);

            return true;
        } else {
            if (carSize_id == 0) {
                Toast.makeText(context, R.string.ch_car_size, Toast.LENGTH_SHORT).show();
            }

            if (carType_id == 0) {
                Toast.makeText(context, R.string.ch_car_type, Toast.LENGTH_SHORT).show();
            }

            if (brand_id == 0) {
                Toast.makeText(context, R.string.ch_brand, Toast.LENGTH_SHORT).show();
            }
            if (order_time_id == 0) {
                time_error.set(context.getString(R.string.field_req));
            } else {
                time_error.set(null);

            }
            if (sender_name.isEmpty()) {
                user_name_error.set(context.getString(R.string.field_req));
            } else {
                user_name_error.set(null);
            }
            if (sender_phone.isEmpty()) {
                user_phone_error.set(context.getString(R.string.field_req));
            } else {
                user_phone_error.set(null);
            }
            if (receiver_name.isEmpty()) {
                receiver_name_error.set(context.getString(R.string.field_req));
            } else {
                receiver_name_error.set(null);
            }
            if (receiver_phone.isEmpty()) {
                receiver_phone_error.set(context.getString(R.string.field_req));
            } else {
                receiver_phone_error.set(null);
            }

            if (order_date.isEmpty()) {
                date_error.set(context.getString(R.string.field_req));
            } else {
                date_error.set(null);

            }

            return false;
        }
    }
    public boolean isDataValidStep2(Context context) {
        if (payment_method != 0) {

            return true;
        } else {
            Toast.makeText(context, R.string.ch_payment, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public SendServiceModel() {
        this.service_id = 0;
        notifyPropertyChanged(BR.service_id);
        this.sub_serv_id = 0;
        notifyPropertyChanged(BR.sub_serv_id);

        this.carType_id = 0;
        notifyPropertyChanged(BR.carType_id);

        this.carSize_id = 0;
        notifyPropertyChanged(BR.carSize_id);


        this.ar_car_type = "";
        notifyPropertyChanged(BR.ar_car_type);

        this.en_car_type = "";
        notifyPropertyChanged(BR.en_car_type);
        this.sender_name = "";
        notifyPropertyChanged(BR.user_name);
        this.sender_phone = "";
        notifyPropertyChanged(BR.user_phone);
        this.receiver_name = "";
        notifyPropertyChanged(BR.receiver_name);
        this.receiver_phone = "";
        notifyPropertyChanged(BR.receiver_phone);
        this.order_date = "";
        notifyPropertyChanged(BR.order_date);

        this.ar_service_type = "";
        notifyPropertyChanged(BR.ar_service_type);

        this.en_service_type = "";
        notifyPropertyChanged(BR.en_service_type);

        this.brand_id = 0;
        notifyPropertyChanged(BR.brand_id);

        this.ar_brand_name = "";
        notifyPropertyChanged(BR.ar_brand_name);
        this.en_brand_name = "";
        notifyPropertyChanged(BR.en_brand_name);

    }

    @Bindable
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
        notifyPropertyChanged(BR.user_id);
    }

    @Bindable
    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
        notifyPropertyChanged(BR.service_id);
    }

    @Bindable
    public int getSub_serv_id() {
        return sub_serv_id;
    }

    public void setSub_serv_id(int sub_serv_id) {
        this.sub_serv_id = sub_serv_id;
        notifyPropertyChanged(BR.sub_serv_id);
    }

    @Bindable
    public int getCarSize_id() {
        return carSize_id;
    }

    public void setCarSize_id(int carSize_id) {
        this.carSize_id = carSize_id;
        notifyPropertyChanged(BR.carSize_id);
    }

    @Bindable
    public int getCarType_id() {
        return carType_id;
    }

    public void setCarType_id(int carType_id) {
        this.carType_id = carType_id;
        notifyPropertyChanged(BR.carType_id);
    }

    @Bindable
    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
        notifyPropertyChanged(BR.brand_id);
    }

    @Bindable
    public String getEn_brand_name() {
        return en_brand_name;
    }

    public void setEn_brand_name(String en_brand_name) {
        this.en_brand_name = en_brand_name;
        notifyPropertyChanged(BR.en_brand_name);
    }

    @Bindable
    public String getAr_brand_name() {
        return ar_brand_name;
    }

    public void setAr_brand_name(String ar_brand_name) {
        this.ar_brand_name = ar_brand_name;
        notifyPropertyChanged(BR.ar_brand_name);
    }

    @Bindable
    public String getAr_car_type() {
        return ar_car_type;
    }

    public void setAr_car_type(String ar_car_type) {
        this.ar_car_type = ar_car_type;
        notifyPropertyChanged(BR.ar_car_type);
    }

    @Bindable
    public String getEn_car_type() {
        return en_car_type;
    }

    public void setEn_car_type(String en_car_type) {
        this.en_car_type = en_car_type;
        notifyPropertyChanged(BR.en_car_type);
    }

    @Bindable
    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
        notifyPropertyChanged(BR.user_name);
    }

    @Bindable
    public String getSender_phone() {
        return sender_phone;
    }

    public void setSender_phone(String sender_phone) {
        this.sender_phone = sender_phone;
        notifyPropertyChanged(BR.user_phone);
    }

    @Bindable
    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
        notifyPropertyChanged(BR.receiver_name);
    }


    @Bindable
    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
        notifyPropertyChanged(BR.receiver_phone);
    }

    @Bindable
    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
        notifyPropertyChanged(BR.order_date);
    }

    @Bindable
    public double getService_price() {
        return service_price;
    }

    public void setService_price(double service_price) {
        this.service_price = service_price;
        notifyPropertyChanged(BR.service_price);
    }

    @Bindable
    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
        notifyPropertyChanged(BR.total_price);
    }

    public int getAmount() {
        return number_of_cars;
    }

    public void setAmount(int number_of_cars) {
        this.number_of_cars = number_of_cars;
    }

    @Bindable
    public String getCoupon_serial() {
        return coupon_serial;
    }

    public void setCoupon_serial(String coupon_serial) {
        this.coupon_serial = coupon_serial;
        notifyPropertyChanged(BR.coupon_serial);
    }

    @Bindable
    public String getAr_service_type() {
        return ar_service_type;
    }

    public void setAr_service_type(String ar_service_type) {
        this.ar_service_type = ar_service_type;
        notifyPropertyChanged(BR.ar_service_type);
    }

    @Bindable
    public String getEn_service_type() {
        return en_service_type;
    }

    public void setEn_service_type(String en_service_type) {
        this.en_service_type = en_service_type;
        notifyPropertyChanged(BR.en_service_type);
    }

    public ServiceDataModel.Level2 getLevel2() {
        return level2;
    }

    public void setLevel2(ServiceDataModel.Level2 level2) {
        this.level2 = level2;
    }

    public List<ItemToUpload.SubServiceModel> getSub_services() {
        return sub_services;
    }

    public void setSub_services(List<ItemToUpload.SubServiceModel> sub_services) {
        this.sub_services = sub_services;
    }
    @Bindable
    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
        notifyPropertyChanged(BR.payment_method);

    }
}
