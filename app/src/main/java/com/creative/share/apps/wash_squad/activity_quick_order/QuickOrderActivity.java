package com.creative.share.apps.wash_squad.activity_quick_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivityQuickOrderBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class QuickOrderActivity extends AppCompatActivity {
    private ActivityQuickOrderBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_quick_order);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
    }
}