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

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_choose_service.ChooseServiceSentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_category.ServiceCategoryActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.adapters.MainServiceAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentMainBinding;
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

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private List<ServiceDataModel.ServiceModel> serviceModelList;
    private MainServiceAdapter adapter;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        serviceModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        manager = new GridLayoutManager(activity,2);
        binding.recView.setLayoutManager(manager);
        adapter = new MainServiceAdapter(serviceModelList,activity,this);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(activity,R.color.colorPrimary),ContextCompat.getColor(activity,R.color.color_second), Color.RED,Color.BLUE);
        binding.swipeRefresh.setOnRefreshListener(this::getServices);
        getServices();



    }

    private void getServices() {

        Api.getService(Tags.base_url)
                .getAllServices()
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {
                       binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            serviceModelList.clear();
                            serviceModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            binding.llNoServices.setVisibility(View.GONE);

                        }else
                        {
                            try {

                                Log.e("error",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code()==404)
                            {
                                binding.llNoServices.setVisibility(View.VISIBLE);
                            }else if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            }else
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceDataModel> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage()!=null)
                            {
                                Log.e("error",t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                {
                                    Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }catch (Exception e){}
                    }
                });
    }



    public void setItemData(ServiceDataModel.ServiceModel serviceModel) {
        Intent intent = new Intent(activity, ServiceCategoryActivity.class);
        intent.putExtra("data",serviceModel);

        startActivityForResult(intent,1000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            activity.DisplayFragmentOrder();
        }
    }

    public void setItemData2(ServiceDataModel.ServiceModel serviceModel1) {
        Intent intent=new Intent(activity, ChooseServiceSentActivity.class);
        intent.putExtra("data",serviceModel1);
        startActivityForResult(intent,1000);
    }


    public void setItemData3(ServiceDataModel.ServiceModel serviceModel1) {
        Intent intent=new Intent(activity, SubscriptionServiceActivity.class);
        intent.putExtra("data",serviceModel1);
        startActivityForResult(intent,1000);
    }
}
