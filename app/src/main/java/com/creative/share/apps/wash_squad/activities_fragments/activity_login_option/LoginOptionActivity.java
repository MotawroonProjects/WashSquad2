package com.creative.share.apps.wash_squad.activities_fragments.activity_login_option;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.wash_squad.activity_quick_order.QuickOrderActivity;
import com.creative.share.apps.wash_squad.databinding.ActivityLoginOptionBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.AboutDataModel;

import java.util.Locale;

import io.paperdb.Paper;

public class LoginOptionActivity extends AppCompatActivity {
    private ActivityLoginOptionBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login_option);

        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.btnLogin.setOnClickListener(view -> {
            Intent intent=new Intent(LoginOptionActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnQuickOrder.setOnClickListener(view -> {
            Intent intent=new Intent(LoginOptionActivity.this, SignInActivity.class);
            intent.putExtra("data","quick");
            startActivity(intent);
            finish();
        });
    }
}