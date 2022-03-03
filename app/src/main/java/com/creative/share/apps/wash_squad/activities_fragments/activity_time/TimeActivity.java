package com.creative.share.apps.wash_squad.activities_fragments.activity_time;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.adapters.TimeAmAdapter;
import com.creative.share.apps.wash_squad.adapters.TimePmAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityTimeBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.TimeDataModel;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityTimeBinding binding;
    private String lang;
    private RecyclerView.LayoutManager manager1,manager2;
    private List<TimeDataModel.TimeModel> timeModelList;
    private List<TimeDataModel.TimeModel> timeAmModelList;
    private List<TimeDataModel.TimeModel> timePmModelList;
    private TimeAmAdapter amAdapter;
    private TimePmAdapter pmAdapter;
    private String selected_date;
    private TimeDataModel.TimeModel timeModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_time);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        selected_date = dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("date"))
        {
            selected_date = intent.getStringExtra("date");

            Log.e("date",selected_date);
        }
    }


    private void initView() {

        timeModelList = new ArrayList<>();
        timeAmModelList = new ArrayList<>();
        timePmModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        manager1 = new GridLayoutManager(this,4);
        manager2 = new GridLayoutManager(this,4);
        binding.recViewAm.setHasFixedSize(true);
        binding.recViewPm.setHasFixedSize(true);

        binding.recViewAm.setLayoutManager(manager1);
        binding.recViewPm.setLayoutManager(manager2);
        amAdapter = new TimeAmAdapter(timeAmModelList,this);
        binding.recViewAm.setAdapter(amAdapter);

        pmAdapter = new TimePmAdapter(timePmModelList,this);
        binding.recViewPm.setAdapter(pmAdapter);

        binding.btnDon.setOnClickListener(view -> {
            Intent intent = getIntent();
            if (intent!=null)
            {
                intent.putExtra("data",timeModel);
                setResult(RESULT_OK,intent);
            }
            finish();
        });
        getTime();


    }

    private void getTime() {
        Api.getService(Tags.base_url)
                .getTime(selected_date)
                .enqueue(new Callback<TimeDataModel>() {
                    @Override
                    public void onResponse(Call<TimeDataModel> call, Response<TimeDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            timeModelList.clear();
                            timeModelList.addAll(response.body().getData());
                            updateUI(response.body().getData());
                            binding.llNoItems.setVisibility(View.GONE);

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                                binding.llNoItems.setVisibility(View.VISIBLE);
                            } else if (response.code() == 500) {
                                Toast.makeText(TimeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(TimeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TimeDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TimeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TimeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateUI(List<TimeDataModel.TimeModel> data) {

        for (TimeDataModel.TimeModel timeModel :data)
        {
            if (timeModel.getType().equals("1"))
            {
                timeAmModelList.add(timeModel);
            }else if (timeModel.getType().equals("2"))
            {
                timePmModelList.add(timeModel);
            }
        }

        amAdapter.notifyDataSetChanged();
        pmAdapter.notifyDataSetChanged();



    }



    public void setItemAm(TimeDataModel.TimeModel timeModel) {
        if (pmAdapter!=null)
        {
            pmAdapter.clearSelection();
            binding.btnDon.setVisibility(View.VISIBLE);
            this.timeModel = timeModel;
        }




    }

    public void setItemPm(TimeDataModel.TimeModel timeModel) {

        if (amAdapter!=null)
        {
            binding.btnDon.setVisibility(View.VISIBLE);
            amAdapter.clearSelection();
            this.timeModel=timeModel;
        }

    }

    @Override
    public void back() {
        finish();
    }

}
