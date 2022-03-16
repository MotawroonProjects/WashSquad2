package com.creative.share.apps.wash_squad.services;


import com.creative.share.apps.wash_squad.models.AboutDataModel;
import com.creative.share.apps.wash_squad.models.CarSizeDataModel;
import com.creative.share.apps.wash_squad.models.CarTypeDataModel;
import com.creative.share.apps.wash_squad.models.CouponDataModel;
import com.creative.share.apps.wash_squad.models.CouponModel;
import com.creative.share.apps.wash_squad.models.ItemSubscribeToUpload;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.OfferDataModel;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.PlaceGeocodeData;
import com.creative.share.apps.wash_squad.models.PlaceMapDetailsData;
import com.creative.share.apps.wash_squad.models.QuestionDataModel;
import com.creative.share.apps.wash_squad.models.SendServiceModel;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.SettingModel;
import com.creative.share.apps.wash_squad.models.SingleOrderDataModel;
import com.creative.share.apps.wash_squad.models.SubscribtionDataModel;
import com.creative.share.apps.wash_squad.models.TimeDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {


    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @FormUrlEncoded
    @POST("api/client/register")
    Call<UserModel> signUp(@Field("full_name") String full_name,
                           @Field("phone_code") String phone_code,
                           @Field("phone") String phone,
                           @Field("password") String password,
                           @Field("software_type") int software_type
    );

    @FormUrlEncoded
    @POST("api/client/password/forget")
    Call<UserModel> forget(@Field("phone") String phone,
                           @Field("phone_code") String phone_code
    );

    @FormUrlEncoded
    @POST("api/client/login")
    Call<UserModel> login(@Field("phone") String phone,
                          @Field("phone_code") String phone_code,
                          @Field("password") String password);

    @FormUrlEncoded
    @POST("api/client/password/reset")
    Call<UserModel> changpass(
            @Field("user_id") int user_id,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("api/order/rate")
    Call<Order_Data_Model.OrderModel> rateorder(@Field("order_id") String order_id,
                                                @Field("opinion_des") String opinion_des,
                                                @Field("rating") float rating);

    @FormUrlEncoded
    @POST("api/single-order")
    Call<Order_Data_Model.OrderModel> getOrdersById(
            @Field("order_id") String order_id

    );

    @FormUrlEncoded
    @POST("api/client/cofirm-code")
    Call<ResponseBody> confirmCode(@Field("user_id") int user_id,
                                   @Field("code") String code
    );

    @FormUrlEncoded
    @POST("api/client/code/send")
    Call<ResponseBody> resendCode(@Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("api/client/passwordCode/confirm")
    Call<ResponseBody> confirmCodepass(@Field("user_id") int user_id,
                                       @Field("code") String code
    );


    @GET("api/services")
    Call<ServiceDataModel> getAllServices();

    @GET("api/carSizes")
    Call<CarSizeDataModel> getCarSizes();

    @GET("api/carTypes")
    Call<CarTypeDataModel> getCarTypes();

    @GET("api/questions")
    Call<QuestionDataModel> getQuestion();

    @GET("api/about-us")
    Call<AboutDataModel> aboutApp();


    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("subject") String subject,
                                   @Field("message") String message
    );

    @FormUrlEncoded
    @POST("api/date/get/times")
    Call<TimeDataModel> getTime(@Field("date") String date);

    @FormUrlEncoded
    @POST("api/profile/edit")
    Call<UserModel> edit_profile(@Field("user_id") String user_id,
                                 @Field("full_name") String full_name
    );

    @FormUrlEncoded
    @POST("api/client/password/change")
    Call<UserModel> edit_pass(@Field("user_id") String user_id,
                              @Field("new_pass") String new_pass
    );

    @Multipart
    @POST("api/profile/edit")
    Call<UserModel> editUserImage(@Part("user_id") RequestBody user_id,
                                  @Part("full_name") RequestBody full_name,
                                  @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("api/orders")
    Call<Order_Data_Model> MyOrder(@Field("user_id") int user_id,
                                   @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/user-current-orders")
    Call<Order_Data_Model> currentOrder(@Field("user_id") int user_id,
                                        @Field("page") int page
    );

    @FormUrlEncoded
    @POST("api/user-previous-orders")
    Call<Order_Data_Model> previousOrder(@Field("user_id") int user_id,
                                         @Field("page") int page
    );


    @POST("api/order/add")
    Call<SingleOrderDataModel> addOrder(@Body ItemToUpload itemToUpload);

    @POST("api/order/add")
    Call<SingleOrderDataModel> addOrderSubscribe(@Body ItemSubscribeToUpload itemToUpload);

    @GET("api/offers")
    Call<OfferDataModel> getOffers();

    @GET("api/all-coupons")
    Call<CouponDataModel> getCoupons();


    @FormUrlEncoded
    @POST("api/coupon/check")
    Call<CouponModel> getCoupon(@Field("user_id") int user_id,
                                @Field("coupon_serial") String coupon_serial
    );

    @FormUrlEncoded
    @POST("api/getPrice")
    Call<Double> getPrice(@Field("service_id") int service_id,
                          @Field("size_id") int size_id
    );

    @FormUrlEncoded
    @POST("api/phone-tokens")
    Call<ResponseBody> updatePhoneToken(
            @Field("phone_token") String phone_token,
            @Field("user_id") int user_id,
            @Field("software_type") String software_type

    );

    @FormUrlEncoded
    @POST("api/phone/token/delete")
    Call<ResponseBody> deltePhoneToken(
            @Field("phone_token") String phone_token,
            @Field("user_id") int user_id

    );

    @FormUrlEncoded
    @POST("api/client/logout")
    Call<ResponseBody> logout(
            @Field("user_id") int user_id
    );

    @GET("api/order/getSubscription")
    Call<SubscribtionDataModel> getSubscribtion(@Query("user_id") String user_id);

    @GET("api/setting")
    Call<SettingModel> getStting();

    @POST("api/order/send-gift")
    Call<SingleOrderDataModel> sendGift(@Body SendServiceModel sendServiceModel);

    @FormUrlEncoded
    @POST("api/order/update-subscription")
    Call<ResponseBody> postponeAppointment(@Field("subscription_id") String subscription_id,
                                           @Field("status") String status
    );
}