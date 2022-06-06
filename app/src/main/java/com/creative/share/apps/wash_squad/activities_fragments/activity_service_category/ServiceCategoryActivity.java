package com.creative.share.apps.wash_squad.activities_fragments.activity_service_category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_sent_details.ServiceSentDetailsActivity;
import com.creative.share.apps.wash_squad.adapters.CategoryServiceAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityServiceCategoryBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;

import java.util.Locale;

import io.paperdb.Paper;

public class ServiceCategoryActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityServiceCategoryBinding binding;
    private String lang;
    private ServiceDataModel.ServiceModel serviceModel;
    private CategoryServiceAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private int type;
    private Order_Data_Model.OrderModel orderModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_category);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            serviceModel = (ServiceDataModel.ServiceModel) intent.getSerializableExtra("data");
            if (intent.getStringExtra("type") != null) {
                type = 1;
            }
            if (intent.getSerializableExtra("order") != null) {
                orderModel = (Order_Data_Model.OrderModel) intent.getSerializableExtra("order");

            }
        }
    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        manager = new GridLayoutManager(this, 2);
        binding.recView.setLayoutManager(manager);
        if (serviceModel.getLevel2().size() > 0) {
            adapter = new CategoryServiceAdapter(serviceModel.getLevel2(), this);
            binding.recView.setAdapter(adapter);
            binding.llNoItems.setVisibility(View.GONE);

        } else {
            binding.llNoItems.setVisibility(View.VISIBLE);
        }


    }

    public void setItemData(ServiceDataModel.Level2 serviceModel1) {

        Intent intent;
        if (type == 1) {
            intent = new Intent(this, ServiceSentDetailsActivity.class);
        } else {
            intent = new Intent(this, ServiceDetailsActivity.class);

        }
        intent.putExtra("data", serviceModel1);
        intent.putExtra("service_id", serviceModel.getId());
        intent.putExtra("service_name_ar", serviceModel.getAr_title());
        intent.putExtra("service_name_en", serviceModel.getEn_title());
        intent.putExtra("order", orderModel);

        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Intent intent = getIntent();
            if (intent != null) {
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }

    @Override
    public void back() {
        finish();
    }


}
