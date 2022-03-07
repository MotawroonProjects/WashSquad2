package com.creative.share.apps.wash_squad.activities_fragments.activity_subscribtion;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivitySubscriptionDetialsBinding;
import com.creative.share.apps.wash_squad.databinding.ActivityWalletBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class SubscribtionActivity extends AppCompatActivity implements Listeners.BackListener  {
    private ActivitySubscriptionDetialsBinding binding;
    private String lang;

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

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);


    }


    @Override
    public void back() {
        finish();
    }


}
