package com.creative.share.apps.wash_squad.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
    private String longitude;
    private String latitude;
    private int place_id;

    private String address;
    private String time;
    private String time_type;
    private int order_time_id;
    private int payment_method;

    private double service_price;
    private double total_price;
    private int number_of_cars;

    private String coupon_serial;
    private String ar_service_type;
    private String en_service_type;
    private double total_tax;
    private String vehicleChar;
    private String vehicleNumber;
    private String car_blade_number;
    private ServiceDataModel.Level2 level2;
    private List<ItemToUpload.SubServiceModel> sub_services;
    private String phone_code;
    public ObservableField<String> user_name_error = new ObservableField<>();
    public ObservableField<String> user_phone_error = new ObservableField<>();
    public ObservableField<String> receiver_name_error = new ObservableField<>();
    public ObservableField<String> receiver_phone_error = new ObservableField<>();
    public ObservableField<String> address_error = new ObservableField<>();
    public ObservableField<String> date_error = new ObservableField<>();
    public ObservableField<String> time_error = new ObservableField<>();
    public ObservableField<String> car_blade_number_error = new ObservableField<>();
    public ObservableField<String> car_plate_char_error = new ObservableField<>();
    public boolean isDataValidStep1(Context context) {
        Log.e("dddkkdk",service_id+" "+carSize_id+" "+carType_id+" "+brand_id+" "+address+" "+sender_name+" "+sender_phone+" "+receiver_name+" "+receiver_phone+" "+time+" "+order_time_id+" "+order_date+" "+car_blade_number+" "+vehicleChar+" "+place_id);
        if (service_id != 0 &&
                //   sub_serv_id != 0 &&
                carSize_id != 0 &&
                carType_id != 0 &&
                brand_id != 0 &&
                !TextUtils.isEmpty(address) &&
                !sender_name.isEmpty() &&
                !sender_phone.isEmpty() &&
                !receiver_name.isEmpty() &&
                !receiver_phone.isEmpty() &&
                !time.isEmpty() &&
                order_time_id != 0 &&
                !order_date.isEmpty() &&
                place_id != 0
                &&!vehicleChar.isEmpty() &&
                !vehicleNumber.isEmpty()


        ) {
            address_error.set(null);
            user_name_error.set(null);
            user_phone_error.set(null);
            receiver_name_error.set(null);
            receiver_phone_error.set(null);
            date_error.set(null);
            time_error.set(null);
            car_blade_number_error.set(null);
            car_plate_char_error.set(null);
            return true;
        } else {
            if (carSize_id == 0) {
                Toast.makeText(context, R.string.ch_car_size, Toast.LENGTH_SHORT).show();
            }

            if (carType_id == 0) {
                Toast.makeText(context, R.string.ch_car_type, Toast.LENGTH_SHORT).show();
            }
            if (vehicleNumber.isEmpty()) {
                car_blade_number_error.set(context.getString(R.string.field_req));
            } else {
                car_blade_number_error.set(null);
            }
            if (vehicleChar.isEmpty()) {
                car_plate_char_error.set(context.getString(R.string.field_req));
            } else {
                car_plate_char_error.set(null);
            }
            if (brand_id == 0) {
                Toast.makeText(context, R.string.ch_brand, Toast.LENGTH_SHORT).show();
            }
            if (place_id == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_area), Toast.LENGTH_LONG).show();
            }
            if (order_time_id == 0) {
                time_error.set(context.getString(R.string.field_req));
            } else {
                time_error.set(null);

            }
            if (TextUtils.isEmpty(address)) {
                address_error.set(context.getString(R.string.field_req));
            } else {
                address_error.set(null);

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
            if (time.isEmpty()) {
                time_error.set(context.getString(R.string.field_req));
            } else {
                time_error.set(null);
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

        this.latitude = "";
        notifyPropertyChanged(BR.latitude);

        this.longitude = "";
        notifyPropertyChanged(BR.longitude);
        this.address = "";
        notifyPropertyChanged(BR.address);

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
        this.time = "";
        notifyPropertyChanged(BR.time);

        this.order_time_id = 0;
        notifyPropertyChanged(BR.order_time_id);
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
        this.place_id = 0;
        notifyPropertyChanged(BR.place_id);
        this.vehicleNumber = "";
        notifyPropertyChanged(BR.vehicleNumber);
        this.vehicleChar = "";
        notifyPropertyChanged(BR.vehicleChar);
    }

    @Bindable
    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
        notifyPropertyChanged(BR.place_id);
    }

    public String getTime_type() {
        return time_type;
    }

    public void setTime_type(String time_type) {
        this.time_type = time_type;
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        time_error.set(null);
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public int getOrder_time_id() {
        return order_time_id;
    }

    public void setOrder_time_id(int order_time_id) {
        this.order_time_id = order_time_id;
        notifyPropertyChanged(BR.order_time_id);
    }

    public double getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(double total_tax) {
        this.total_tax = total_tax;
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
        date_error.set(null);
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

    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.latitude);

    }

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.longitude);

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        address_error.set(null);

        notifyPropertyChanged(BR.order_date);

    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }
    @Bindable
    public String getCar_blade_number() {
        return car_blade_number;
    }

    public void setCar_blade_number(String car_blade_number) {
        this.car_blade_number = car_blade_number;
        notifyPropertyChanged(BR.car_blade_number);
    }



    @Bindable
    public String getVehicleChar() {
        return vehicleChar;
    }

    public void setVehicleChar(String vehicleChar) {
        this.vehicleChar = vehicleChar;
        notifyPropertyChanged(BR.vehicleChar);
    }

    @Bindable
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
        notifyPropertyChanged(BR.vehicleNumber);
    }

}
