package com.creative.share.apps.wash_squad.activities_fragments.activity_product_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivityProductDetailsBinding;

public class ProductDetailsActivity extends AppCompatActivity {
    private ActivityProductDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_product_details);
        initView();
    }

    private void initView() {
    }
}