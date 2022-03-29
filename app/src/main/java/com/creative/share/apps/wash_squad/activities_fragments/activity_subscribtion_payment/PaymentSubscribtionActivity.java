package com.creative.share.apps.wash_squad.activities_fragments.activity_subscribtion_payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaymentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaypalwebviewActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_terms_conditions.TermsActivity;
import com.creative.share.apps.wash_squad.adapters.AdditionalAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityPaymentBinding;
import com.creative.share.apps.wash_squad.databinding.ActivityPaymentSubscribtionBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.CouponModel;
import com.creative.share.apps.wash_squad.models.ItemSubscribeToUpload;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.SettingModel;
import com.creative.share.apps.wash_squad.models.SingleOrderDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.singleton.SingleTon;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSubscribtionActivity extends AppCompatActivity {
    private ActivityPaymentSubscribtionBinding binding;
    private String lang;
    private ItemSubscribeToUpload itemToUpload;
    private SingleTon singleTon;
    private LinearLayoutManager manager;
    private AdditionalAdapter adapter;
    private double total = 0.0;
    private double coupon_value = 0;
    private CouponModel couponModel = null;
    private Preferences preferences;
    private UserModel userModel;
    private SettingModel settingModel;
    private double tax;
    private Order_Data_Model.OrderModel orderModel;
    private double wallet;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_subscribtion);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            itemToUpload = (ItemSubscribeToUpload) intent.getSerializableExtra("item");
            if (intent.getSerializableExtra("order") != null) {
                orderModel = (Order_Data_Model.OrderModel) intent.getSerializableExtra("order");

            }
        }
    }


    private void initView() {

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        singleTon = SingleTon.newInstance();
        binding.setItemModel(itemToUpload);

        binding.tvPoliciesAndTerms.setPaintFlags(binding.tvPoliciesAndTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM", Locale.ENGLISH);
        // String m_date = dateFormat.format(new Date(itemToUpload.getOrder_date()*1000));
        String m_date = itemToUpload.getOrder_date();
        binding.tvDate.setText(String.format("%s %s %s", m_date, "  ", itemToUpload.getday()));
        binding.tvPoliciesAndTerms.setOnClickListener(view -> {
            Intent intent = new Intent(this, TermsActivity.class);
            startActivity(intent);
        });

//        if (itemToUpload.getSub_services().size()>0)
//        {tvDate
//            manager = new LinearLayoutManager(this);
//            adapter = new AdditionalAdapter(itemToUpload.getSub_services(),this);
//            binding.recView.setLayoutManager(manager);
//            binding.recView.setAdapter(adapter);
//
//
//            binding.tvNoAdditionalServices.setVisibility(View.GONE);
//        }else
//            {
//                binding.tvNoAdditionalServices.setVisibility(View.VISIBLE);
//
//            }
        binding.tvEdit.setOnClickListener(view -> {
            finish();
        });
        binding.rbNo.setOnClickListener(view1 -> {
            itemToUpload.setPayment_method(2);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.cache);
            binding.flCash.setVisibility(View.GONE);
        });
        binding.rbYes.setOnClickListener(view12 -> {
            itemToUpload.setPayment_method(2);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.cache);
            binding.flCash.setVisibility(View.GONE);
        });
        binding.rb1.setOnClickListener(view ->
        {
            itemToUpload.setPayment_method(1);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.apple_pay);


        });
        binding.rb2.setOnClickListener(view ->
        {
            binding.flCash.setVisibility(View.VISIBLE);


        });
        binding.rb3.setOnClickListener(view -> {
            itemToUpload.setPayment_method(3);
            binding.tvPayment.setText(R.string.pos);
        });
        binding.rb4.setOnClickListener(view -> {

            if(orderModel!=null){
                if(itemToUpload.getTotal_price()-orderModel.getTotal_price()<=wallet){
                    itemToUpload.setPayment_method(4);
                    binding.setItemModel(itemToUpload);
                    binding.tvPayment.setText(R.string.my_wallet_balance);
                }
                else{
                    binding.flMyWallet.setVisibility(View.VISIBLE);

                }

            }
            else {
                    if(itemToUpload.getTotal_price()<=wallet){
                    itemToUpload.setPayment_method(4);
                    binding.setItemModel(itemToUpload);
                    binding.tvPayment.setText(R.string.my_wallet_balance);
                }
                else{
                    binding.flMyWallet.setVisibility(View.VISIBLE);

                }
            }

        });
        binding.tvDone.setOnClickListener(view -> binding.flMyWallet.setVisibility(View.GONE));


        binding.btnSend.setOnClickListener(view -> {
            if (itemToUpload.isDataValidStep2(this)) {
                itemToUpload.setUser_phone(userModel.getPhone_code() + itemToUpload.getUser_phone());
                if (couponModel == null || couponModel.getId() == orderModel.getCoupon().getId()) {
                    itemToUpload.setCoupon_serial(null);
                } else {
                    itemToUpload.setCoupon_serial(couponModel.getCoupon_serial());
                }
                if (orderModel == null) {
                    uploadOrder(itemToUpload);
                } else {
                    itemToUpload.setOrder_id(orderModel.getId() + "");
                    updateOrder(itemToUpload);
                }
            }
        });


        binding.btnOther.setOnClickListener(view -> {
            // if (itemToUpload.isDataValidStep2(this)) {

            if (couponModel == null) {
                itemToUpload.setCoupon_serial(null);
            } else {
                itemToUpload.setCoupon_serial(couponModel.getCoupon_serial());
            }


            //  singleTon.addItem(itemToUpload);
            Intent intent = getIntent();
            if (intent != null) {
                setResult(RESULT_OK, intent);
            }
            finish();
            // }
        });


        binding.btnDiscount.setOnClickListener(view -> {
            String coupon = binding.edtCoupon.getText().toString().trim();

            if (!TextUtils.isEmpty(coupon)) {
                binding.edtCoupon.setError(null);
                Common.CloseKeyBoard(this, binding.edtCoupon);
                getCouponValue(coupon);

            } else {
                binding.edtCoupon.setError(getString(R.string.field_req));
            }
        });

        updateTotalPrice(coupon_value);

        getSetting();
        getProfile();
        if (orderModel != null) {
            updateData();
        }
    }

    private void updateData() {
        if (orderModel.getPayment_method().equals("1")) {
            binding.rb1.setChecked(true);
            itemToUpload.setPayment_method(1);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.apple_pay);
            binding.setItemModel(itemToUpload);
            //binding.rb1
        } else if (orderModel.getPayment_method().equals("2")) {
            itemToUpload.setPayment_method(2);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.cache);
            binding.setItemModel(itemToUpload);
            binding.rb2.setChecked(true);

        } else if (orderModel.getPayment_method().equals("3")) {
            itemToUpload.setPayment_method(3);
            binding.rb3.setChecked(true);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.pos);
            binding.setItemModel(itemToUpload);

        } else if (orderModel.getPayment_method().equals("4")) {
            itemToUpload.setPayment_method(4);
            binding.rb4.setChecked(true);
            binding.setItemModel(itemToUpload);
            binding.tvPayment.setText(R.string.my_wallet);
            binding.setItemModel(itemToUpload);
        }
        if (orderModel.getCoupon_serial() != null) {
            getCouponValue(orderModel.getCoupon_serial());
        }
    }


    private void updateTotalPrice(double coupon_value) {
        double coupon = 0;

        /*if (itemToUpload.getSub_services().size()>0)
        {
            total +=itemToUpload.getService_price();
            total += getAdditionalServicePrice(itemToUpload.getSub_services());

            total = total -((total*coupon_value)/100);
        }else
            {
                total +=(itemToUpload.getService_price())-((itemToUpload.getService_price()*coupon_value)/100);

            }*/
        //  tax = (itemToUpload.getTotal_price() * settingModel.getTax_per()) / 100;
        coupon = (((itemToUpload.getTotal_price()) * coupon_value) / 100);
        binding.setCoupon(coupon);
        total = itemToUpload.getTotal_price() - coupon;
        itemToUpload.setTotal_price(total);
        binding.setItemModel(itemToUpload);
    }

    private void getCouponValue(String coupon) {


        binding.progBar.setVisibility(View.VISIBLE);
        binding.iconChecked.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getCoupon(userModel.getId(), coupon)
                .enqueue(new Callback<CouponModel>() {
                    @Override
                    public void onResponse(Call<CouponModel> call, Response<CouponModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {

                            couponModel = response.body();
                            updateTotalPrice(couponModel.getRatio());
                            binding.tvCoupon.setText(String.format(Locale.ENGLISH, "%s %s", String.valueOf(couponModel.getRatio() / 100.0), getString(R.string.discount)));
                            binding.iconChecked.setVisibility(View.VISIBLE);
                            Common.CreateDialogAlert(PaymentSubscribtionActivity.this, getString(R.string.cong) + " " + (couponModel.getRatio() / 100) + " " + getString(R.string.disc));

                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 422) {
                                Common.CreateDialogAlert(PaymentSubscribtionActivity.this, getString(R.string.inv_coupon));
                            } else if (response.code() == 500) {
                                Toast.makeText(PaymentSubscribtionActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {

                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PaymentSubscribtionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private double getAdditionalServicePrice(List<ItemToUpload.SubServiceModel> sub_services) {
        double total = 0.0;
        for (ItemToUpload.SubServiceModel subServiceModel : sub_services) {
            total += subServiceModel.getPrice();
        }
        return total;

    }

    private void updateOrder(ItemSubscribeToUpload itemToUpload) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Log.e("dllddl", itemToUpload.getday());
            Api.getService(Tags.base_url)
                    .updateOrderSubscribe(itemToUpload)
                    .enqueue(new Callback<SingleOrderDataModel>() {
                        @Override
                        public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.suc), Toast.LENGTH_LONG).show();
                                if (itemToUpload.getPayment_method() == 1 && orderModel.getTotal_price() < itemToUpload.getTotal_price()) {
                                    Intent intent = new Intent(PaymentSubscribtionActivity.this, PaypalwebviewActivity.class);
                                    intent.putExtra("url", response.body().getUrl());


                                    startActivityForResult(intent, 100);
                                } else {
                                    Intent intent = getIntent();
                                    if (intent != null) {
                                        setResult(RESULT_OK, intent);
                                    }
                                    finish();
                                }
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 402) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, R.string.num_exceed, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleOrderDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(PaymentSubscribtionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PaymentSubscribtionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                                Log.e("ec", e.getMessage() + "_");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("edddc", e.getMessage() + "_");

            dialog.dismiss();

        }

    }


    private void uploadOrder(ItemSubscribeToUpload itemToUpload) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {


            Api.getService(Tags.base_url)
                    .addOrderSubscribe(itemToUpload)
                    .enqueue(new Callback<SingleOrderDataModel>() {
                        @Override
                        public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.suc), Toast.LENGTH_LONG).show();

                                if (itemToUpload.getPayment_method() == 1) {
                                    Intent intent = new Intent(PaymentSubscribtionActivity.this, PaypalwebviewActivity.class);
                                    intent.putExtra("url", response.body().getUrl());


                                    startActivityForResult(intent, 100);
                                } else {
                                    Intent intent = getIntent();
                                    if (intent != null) {
                                        setResult(RESULT_OK, intent);
                                    }
                                    finish();
                                }
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 402) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, R.string.num_exceed, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(PaymentSubscribtionActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(PaymentSubscribtionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleOrderDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(PaymentSubscribtionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PaymentSubscribtionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                                Log.e("ec", e.getMessage() + "_");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("edddc", e.getMessage() + "_");

            dialog.dismiss();

        }

    }

    private void getSetting() {

        Api.getService(Tags.base_url)
                .getStting()
                .enqueue(new Callback<SettingModel>() {
                    @Override
                    public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            settingModel = response.body();
                            tax = (itemToUpload.getTotal_price() * settingModel.getTax_per()) / 100;
                            binding.setTax(tax);
                            itemToUpload.setTotal_price(itemToUpload.getTotal_price() + tax);
                            itemToUpload.setTotal_tax(tax);
                            binding.setItemModel(itemToUpload);
                        }
                    }

                    @Override
                    public void onFailure(Call<SettingModel> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
    }
    private void getProfile() {
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getProfile(userModel.getId() + "")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                            if (response.isSuccessful() && response.body() != null) {
                                binding.progBar.setVisibility(View.GONE);
                               wallet=response.body().getWallet();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());

                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Intent intent = getIntent();
            if (intent != null) {
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }
}
