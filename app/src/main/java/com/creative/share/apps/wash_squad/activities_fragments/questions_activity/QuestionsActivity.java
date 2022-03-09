package com.creative.share.apps.wash_squad.activities_fragments.questions_activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.adapters.QuestionsAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityQuestionsBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.QuestionDataModel;
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

public class QuestionsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityQuestionsBinding binding;
    private String lang;
    private QuestionsAdapter adapter;
    private List<QuestionDataModel.QuestionModel> questionModelList;
    private LinearLayoutManager manager;

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


    @Override
    public void back() {
        finish();
    }

}
