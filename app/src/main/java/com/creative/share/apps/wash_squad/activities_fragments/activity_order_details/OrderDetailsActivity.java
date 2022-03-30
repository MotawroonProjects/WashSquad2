package com.creative.share.apps.wash_squad.activities_fragments.activity_order_details;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_choose_service.ChooseServiceSentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.fragments.Fragment_Product_Details;
import com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.fragments.Fragment_Order_Products;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaypalwebviewActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_print.PrintActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_sent_payment.ServiceSentPaymentActivity;
import com.creative.share.apps.wash_squad.adapters.ViewPagerAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityOrderDetailsBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.NotStateModel;
import com.creative.share.apps.wash_squad.models.OfferDataModel;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;
    private Order_Data_Model.OrderModel orderModel;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> title;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private List<ServiceDataModel.ServiceModel> serviceModelList;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        getDataFromIntent();
        initView();
        getOrder();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (Order_Data_Model.OrderModel) intent.getSerializableExtra("data");
        }
    }

    private void initView() {
        serviceModelList = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        Log.e("d;dddlld", lang);
        binding.setModel(orderModel);
        fragmentList = new ArrayList<>();
        title = new ArrayList<>();
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(2);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            EventBus.getDefault().register(this);
        }
        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(orderModel);
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });
        binding.llprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, PrintActivity.class);
                intent.putExtra("url", Tags.base_url+"api/order/print/"+orderModel.getId());
                startActivity(intent);
            }
        });
        getServices();

    }

    private void getOrder() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getOrdersById(orderModel.getId() + "")
                    .enqueue(new Callback<Order_Data_Model.OrderModel>() {
                        @Override
                        public void onResponse(Call<Order_Data_Model.OrderModel> call, Response<Order_Data_Model.OrderModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                orderModel = response.body();
                                updateUi(orderModel);

                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(OrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.e("errorsssss", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Data_Model.OrderModel> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(OrderDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void updateUi(Order_Data_Model.OrderModel orderModel) {
        fragmentList.clear();
        fragmentList.add(Fragment_Product_Details.newInstance(orderModel));
        fragmentList.add(Fragment_Order_Products.newInstance(orderModel));
        title.add(getString(R.string.info));
        title.add(getString(R.string.addition));
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
        adapter.addFragment(fragmentList);
        adapter.addTitles(title);
        binding.pager.setAdapter(adapter);

        for (int i = 0; i < binding.tab.getChildCount(); i++) {
            View view = ((ViewGroup) binding.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(10, 0, 10, 0);
        }
        Log.e("ldlfllf", orderModel.getStatus() + "");
        if (orderModel.getStatus() == 0) {
            binding.img1.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img2.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            binding.img3.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            binding.img4.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            binding.btnCancel.setVisibility(View.VISIBLE);


        } else if (orderModel.getStatus() == 1) {
            binding.img1.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img2.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img3.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            binding.img4.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            binding.btnCancel.setVisibility(View.GONE);
        } else if (orderModel.getStatus() == 2) {
            binding.img1.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img2.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img3.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img4.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            binding.btnCancel.setVisibility(View.GONE);

        } else {
            binding.img1.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img2.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img3.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.img4.setBackground(getResources().getDrawable(R.drawable.circle_primary));
            binding.btnCancel.setVisibility(View.GONE);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenNotificationChange(final NotStateModel notStateModel) {
        getOrder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void show(Order_Data_Model.OrderModel orderModel) {
        if (orderModel.getService_id() == 77) {
            Intent intent = new Intent(this, SubscriptionServiceActivity.class);
            intent.putExtra("order", orderModel);

            intent.putExtra("data", serviceModelList.get(2));

            startActivityForResult(intent, 1000);
        } else {
            int pos = -1;
            for (int i = 0; i < serviceModelList.size(); i++) {
                if (serviceModelList.get(i).getId() == orderModel.getService_id()) {
                    pos = i;
                }
            }
            if (pos != -1) {
                Intent intent = new Intent(this, ServiceDetailsActivity.class);
                intent.putExtra("data", serviceModelList.get(pos));
                intent.putExtra("order", orderModel);
                startActivityForResult(intent, 1000);
            }
        }
    }

    private void getServices() {

        Api.getService(Tags.base_url)
                .getAllServices()
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            serviceModelList.clear();
                            serviceModelList.addAll(response.body().getData());


                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                // Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceDataModel> call, Throwable t) {
                        try {
//                            binding.swipeRefresh.setRefreshing(false);
//                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //  Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            Intent intent = getIntent();
            if (intent != null) {

                setResult(RESULT_OK, intent);

            }
            finish();

        }
    }

    private void cancelOrder() {
        final ProgressDialog dialog = Common.createProgressDialog(OrderDetailsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .Cancel_order(orderModel.getId(), 15)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(OrderDetailsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(RESULT_OK, intent);
                                }
                                finish();

                            } else {
                                Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(OrderDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("lll", e.getMessage().toString());
        }
    }

}
