package com.creative.share.apps.wash_squad.activities_fragments.activity_choose_service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivityChooseServiceSentBinding;

public class ChooseServiceSentActivity extends AppCompatActivity {
    private ActivityChooseServiceSentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_choose_service_sent);
        initView();
    }

    private void initView() {

    }
}