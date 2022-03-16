package com.creative.share.apps.wash_squad.activities_fragments.activity_choose_service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_sent_details.ServiceSentDetailsActivity;
import com.creative.share.apps.wash_squad.adapters.MainServiceAdapter;
import com.creative.share.apps.wash_squad.adapters.SendServiceAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityChooseServiceSentBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.SelectedLocation;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.TimeDataModel;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseServiceSentActivity extends AppCompatActivity {
    private ActivityChooseServiceSentBinding binding;
    private List<ServiceDataModel.ServiceModel> serviceModelList;
    private SendServiceAdapter adapter;
    private LinearLayoutManager manager;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_service_sent);
        initView();
    }

    private void initView() {
        serviceModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(this, 2);
        binding.recView.setLayoutManager(manager);
        adapter = new SendServiceAdapter(serviceModelList, this);
        binding.recView.setAdapter(adapter);

        getServices();
        binding.llBack.setOnClickListener(view -> finish());
    }

    private void getServices() {

        Api.getService(Tags.base_url)
                .getAllServices()
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            serviceModelList.clear();
                            serviceModelList.add(response.body().getData().get(0));
                            serviceModelList.add(response.body().getData().get(1));
                            serviceModelList.add(response.body().getData().get(3));
                            adapter.notifyDataSetChanged();
                            binding.llNoServices.setVisibility(View.GONE);

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                                binding.llNoServices.setVisibility(View.VISIBLE);
                            } else if (response.code() == 500) {
                                Toast.makeText(ChooseServiceSentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ChooseServiceSentActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceDataModel> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ChooseServiceSentActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChooseServiceSentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    public void setItemData(ServiceDataModel.ServiceModel serviceModel1) {
        Intent intent = new Intent(this, ServiceSentDetailsActivity.class);
        intent.putExtra("data", serviceModel1);

        startActivityForResult(intent, 4);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            Intent intent = getIntent();
            if (intent != null) {

                setResult(RESULT_OK, intent);

            }
            finish();
        }
    }

}