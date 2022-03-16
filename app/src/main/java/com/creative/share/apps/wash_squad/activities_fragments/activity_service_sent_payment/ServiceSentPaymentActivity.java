package com.creative.share.apps.wash_squad.activities_fragments.activity_service_sent_payment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaymentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaypalwebviewActivity;
import com.creative.share.apps.wash_squad.adapters.AdditionalAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityServiceSentPaymentBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.CouponModel;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.SendServiceModel;
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

public class ServiceSentPaymentActivity extends AppCompatActivity {
    private ActivityServiceSentPaymentBinding binding;
    private String lang;
    private SendServiceModel sendServiceModel;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_sent_payment);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            sendServiceModel = (SendServiceModel) intent.getSerializableExtra("item");
        }
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        singleTon = SingleTon.newInstance();
        binding.setSendServiceModel(sendServiceModel);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM", Locale.ENGLISH);
        // String m_date = dateFormat.format(new Date(itemToUpload.getOrder_date()*1000));
        String m_date = sendServiceModel.getOrder_date();


        binding.tvEdit.setOnClickListener(view -> {
            finish();
        });

//        binding.btnSend.setOnClickListener(view -> {
//            if (itemToUpload.isDataValidStep2(this))
//            {
//                if (couponModel==null)
//                {
//                    itemToUpload.setCoupon_serial(null);
//                }else
//                {
//                    itemToUpload.setCoupon_serial(couponModel.getCoupon_serial());
//                }
//                uploadOrder(itemToUpload);
//            }
//        });

/*
        binding.btnOther.setOnClickListener(view -> {
            if (itemToUpload.isDataValidStep2(this))
            {

                if (couponModel==null)
                {
                    itemToUpload.setCoupon_serial(null);
                }else
                {
                    itemToUpload.setCoupon_serial(couponModel.getCoupon_serial());
                }


                singleTon.addItem(itemToUpload);
                Intent intent = getIntent();
                if (intent!=null)
                {
                    setResult(RESULT_OK,intent);
                }
                finish();
            }
        });
*/

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
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendServiceModel.setPayment_method(2);
                uploadOrder(sendServiceModel);
            }
        });

        updateTotalPrice(coupon_value);
        getSetting();
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
        coupon = (((sendServiceModel.getTotal_price()) * coupon_value) / 100);
        binding.setCoupon(coupon);
        total = sendServiceModel.getTotal_price() - coupon;
        sendServiceModel.setTotal_price(total);
        binding.tvCoupon.setText(String.format(Locale.ENGLISH, "%s %s", String.valueOf(coupon), getString(R.string.sar)));
        binding.setSendServiceModel(sendServiceModel);
    }


    private double getAdditionalServicePrice(List<ItemToUpload.SubServiceModel> sub_services) {
        double total = 0.0;
        for (ItemToUpload.SubServiceModel subServiceModel : sub_services) {
            total += subServiceModel.getPrice();
        }
        return total;

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
                            binding.iconChecked.setVisibility(View.VISIBLE);
                            Common.CreateDialogAlert(ServiceSentPaymentActivity.this, getString(R.string.cong) + " " + (couponModel.getRatio() / 100) + " " + getString(R.string.disc));

                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 422) {
                                Common.CreateDialogAlert(ServiceSentPaymentActivity.this, getString(R.string.inv_coupon));
                            } else if (response.code() == 500) {
                                Toast.makeText(ServiceSentPaymentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceSentPaymentActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(ServiceSentPaymentActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceSentPaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void uploadOrder(SendServiceModel sendServiceModel) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {


            Api.getService(Tags.base_url)
                    .sendGift(sendServiceModel)
                    .enqueue(new Callback<SingleOrderDataModel>() {
                        @Override
                        public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(ServiceSentPaymentActivity.this, getString(R.string.suc), Toast.LENGTH_LONG).show();
                                if (sendServiceModel.getPayment_method() == 2) {
                                    Intent intent = new Intent(ServiceSentPaymentActivity.this, PaypalwebviewActivity.class);
                                    intent.putExtra("url", response.body().getUrl());


                                    startActivityForResult(intent,100);
                                } else {
                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(RESULT_OK, intent);
                                }
                                finish();}
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(ServiceSentPaymentActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 402) {
                                    Toast.makeText(ServiceSentPaymentActivity.this, R.string.num_exceed, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(ServiceSentPaymentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ServiceSentPaymentActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(ServiceSentPaymentActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ServiceSentPaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            tax = (sendServiceModel.getTotal_price() * settingModel.getTax_per()) / 100;
                            binding.setTax(tax);
                            sendServiceModel.setTotal_price(sendServiceModel.getTotal_price() + tax);
                            binding.setSendServiceModel(sendServiceModel);
                        }
                    }

                    @Override
                    public void onFailure(Call<SettingModel> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Intent intent = getIntent();
            if (intent != null) {
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }
}