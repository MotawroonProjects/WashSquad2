package com.creative.share.apps.wash_squad.activities_fragments.questions_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.adapters.QuestionsAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityQuestionsBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.QuestionDataModel;
import com.creative.share.apps.wash_squad.models.SettingModel;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityQuestionsBinding binding;
    private String lang;
    private QuestionsAdapter adapter;
    private List<QuestionDataModel.QuestionModel> questionModelList;
    private LinearLayoutManager manager;
    private SettingModel settingModel;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions);
        initView();

    }


    private void initView() {
        questionModelList = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        adapter = new QuestionsAdapter(questionModelList, this);
        binding.recView.setAdapter(adapter);

        getQuestion();
        binding.llQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.expandLayout.isExpanded()) {
                    binding.expandLayout.collapse(true);
                } else {
                    binding.expandLayout.expand(true);
                }
            }
        });
        binding.llWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingModel != null && settingModel.getWhatsapp() != null && !settingModel.getWhatsapp().equals("#")) {
                    open("https://api.whatsapp.com/send?phone=+966" + settingModel.getWhatsapp());
                } else {
                    Toast.makeText(QuestionsActivity.this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingModel.getWhatsapp()!= null) {
                    intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+966" + settingModel.getWhatsapp(), null));
                }
                if (intent != null) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(QuestionsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(QuestionsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(QuestionsActivity.this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();

                    // Common.CreateAlertDialog(QuestionsActivity.this, getResources().getString(R.string.phone_not_found));
                }

            }
        });
        binding.llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingModel.getWhatsapp()!=null) {
                    String number = "+966"+settingModel.getWhatsapp();  // The number on which you want to send SMS
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                }
                else {
                    Toast.makeText(QuestionsActivity.this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();

                    // Common.CreateAlertDialog(QuestionsActivity.this, getResources().getString(R.string.phone_not_found));
                }
            }
        });
        getSetting();
    }

    private void getQuestion() {

        Api.getService(Tags.base_url)
                .getQuestion()
                .enqueue(new Callback<QuestionDataModel>() {
                    @Override
                    public void onResponse(Call<QuestionDataModel> call, Response<QuestionDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            questionModelList.clear();
                            questionModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();

                            binding.tvNoDetails.setVisibility(View.GONE);

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                                binding.tvNoDetails.setVisibility(View.VISIBLE);
                            } else if (response.code() == 500) {
                                Toast.makeText(QuestionsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(QuestionsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(QuestionsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QuestionsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void getSetting() {

        Api.getService(Tags.base_url)
                .getStting()
                .enqueue(new Callback<SettingModel>() {
                    @Override
                    public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            settingModel = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<SettingModel> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
    }

    private void open(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void back() {
        finish();
    }

}
