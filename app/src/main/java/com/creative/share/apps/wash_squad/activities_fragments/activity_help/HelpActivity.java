package com.creative.share.apps.wash_squad.activities_fragments.activity_help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_contact_us.ContactUsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_terms_conditions.TermsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.questions_activity.QuestionsActivity;
import com.creative.share.apps.wash_squad.databinding.ActivityHelpBinding;
import com.creative.share.apps.wash_squad.databinding.DialogLanguageBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class HelpActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityHelpBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.btnLang.setOnClickListener(view -> CreateLangDialog());
        binding.btnQuestions.setOnClickListener(view -> {
            Intent intent = new Intent(this, QuestionsActivity.class);
            startActivity(intent);
        });

        binding.btnTerms.setOnClickListener(view -> {
            Intent intent = new Intent(this, TermsActivity.class);
            startActivity(intent);
        });

        binding.btnContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        });


    }

    private void CreateLangDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = getIntent();
            if (intent!=null)
            {
                intent.putExtra("lang","ar");
                setResult(RESULT_OK,intent);
            }
            finish();

        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = getIntent();
            if (intent!=null)
            {
                intent.putExtra("lang","en");
                setResult(RESULT_OK,intent);
            }
            finish();


        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void back() {
        finish();
    }

}
