package com.creative.share.apps.wash_squad.activities_fragments.activity_subscribtion;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.adapters.WashAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivitySubscriptionDetialsBinding;
import com.creative.share.apps.wash_squad.databinding.ActivityWalletBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.DayModel;
import com.creative.share.apps.wash_squad.models.SubscribtionDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribtionActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivitySubscriptionDetialsBinding binding;
    private String lang;
    private List<SubscribtionDataModel.WashSub> washSubList;
    private Preferences preferences;
    private UserModel userModel;
    private WashAdapter washAdapter;
    private int count, status;
    private ArrayList<DayModel> dayModelList;
    private ArrayList<String> dayModelList2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_detials);
        initView();

    }


    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        washSubList = new ArrayList<>();
       setDays();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        washAdapter = new WashAdapter(washSubList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(washAdapter);
        getSubscribtion();
        binding.btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0) {

                } else {
                    binding.flData.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.btnDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.flData.setVisibility(View.GONE);
            }
        });
    }

    private void setDays() {
        dayModelList = new ArrayList<>();
        dayModelList.add(new DayModel(getString(R.string.Saturday)));
        dayModelList.add(new DayModel(getString(R.string.sunday)));
        dayModelList.add(new DayModel(getString(R.string.monday)));
        dayModelList.add(new DayModel(getString(R.string.tuesday)));
        dayModelList.add(new DayModel(getString(R.string.wendesday)));
        dayModelList.add(new DayModel(getString(R.string.thursday)));
        dayModelList.add(new DayModel(getString(R.string.friday)));
        dayModelList2 = new ArrayList<>();
        dayModelList2.add("SATURDAY");
        dayModelList2.add("SUNDAY");
        dayModelList2.add("MONDAY");
        dayModelList2.add("TUESDAY");
        dayModelList2.add("WEDNESDAY");
        dayModelList2.add("THURSDAY");
        dayModelList2.add("FRIDAY");
    }


    @Override
    public void back() {
        finish();
    }

    private void getSubscribtion() {
        binding.progBar.setVisibility(View.VISIBLE);
        Log.e("data", "ddd");
        Api.getService(Tags.base_url)
                .getSubscribtion(userModel.getId() + "")
                .enqueue(new Callback<SubscribtionDataModel>() {
                    @Override
                    public void onResponse(Call<SubscribtionDataModel> call, Response<SubscribtionDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            updateui(response.body());

                        } else {
                            // binding.swipeRefresh.setRefreshing(false);
                            if (response.code() == 201) {
                                binding.cardView.setVisibility(View.GONE);
                                binding.llData.setVisibility(View.GONE);
                                binding.tvNoDetails.setVisibility(View.VISIBLE);
                                // binding.tvNoDetails.setVisibility(View.VISIBLE);
                            }
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                                //   binding.llNoCoupon.setVisibility(View.VISIBLE);
                            } else if (response.code() == 500) {
                                // Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscribtionDataModel> call, Throwable t) {
                        try {
                            // binding.swipeRefresh.setRefreshing(false);

                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    //    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateui(SubscribtionDataModel body) {
        washSubList.clear();
        washSubList.addAll(body.getWash_sub());
        binding.cardView.setVisibility(View.VISIBLE);
        binding.llData.setVisibility(View.VISIBLE);
        binding.tvNoDetails.setVisibility(View.GONE);
        count = 0;
        washAdapter.notifyDataSetChanged();
        for (int i = 0; i < washSubList.size(); i++) {
            if (washSubList.get(i).getStatus().equals("done")) {
                count += 1;
            } else {
                binding.setModel(washSubList.get(i));
                binding.setDay(dayModelList.get(dayModelList2.indexOf(washSubList.get(i).getDay().toUpperCase())).getDay_text());
                status = 1;
            }
        }
        if (status == 1) {
            binding.btnSubscribe.setText(getResources().getString(R.string.postpone_an_appointment));
        }
        binding.setCount((washSubList.size() - count) + "");
        binding.setPrice(body.getTotal_price());
    }

}
