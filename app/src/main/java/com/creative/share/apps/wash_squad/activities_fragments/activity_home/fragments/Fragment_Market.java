package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_choose_service.ChooseServiceSentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.adapters.CategoryAdapter;
import com.creative.share.apps.wash_squad.adapters.MainServiceAdapter;
import com.creative.share.apps.wash_squad.adapters.Product2Adapter;
import com.creative.share.apps.wash_squad.databinding.FragmentMainBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentMarketBinding;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Market extends Fragment {
    private HomeActivity activity;
    private FragmentMarketBinding binding;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
//    private List<ServiceDataModel.ServiceModel> serviceModelList;
//    private MainServiceAdapter adapter;

    public static Fragment_Market newInstance() {
        return new Fragment_Market();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
      //  serviceModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);

      //  binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        manager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false);
        binding.recyclerCategory.setLayoutManager(manager);
        binding.recyclerCategory.setAdapter(new CategoryAdapter(new ArrayList<>(),activity));
binding.recyclerProduct.setLayoutManager(new GridLayoutManager(activity,2));
binding.recyclerProduct.setAdapter(new Product2Adapter(new ArrayList<>(),activity));
        //        adapter = new MainServiceAdapter(serviceModelList,activity,this);
//        binding.recView.setAdapter(adapter);
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(activity,R.color.colorPrimary),ContextCompat.getColor(activity,R.color.color_second), Color.RED,Color.BLUE);
//        binding.swipeRefresh.setOnRefreshListener(this::getServices);



    }


}
