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
import com.creative.share.apps.wash_squad.activities_fragments.activity_product_details.ProductDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_web_view.WebViewActivity;
import com.creative.share.apps.wash_squad.adapters.CategoryAdapter;
import com.creative.share.apps.wash_squad.adapters.MainServiceAdapter;
import com.creative.share.apps.wash_squad.adapters.Product2Adapter;
import com.creative.share.apps.wash_squad.databinding.FragmentMainBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentMarketBinding;
import com.creative.share.apps.wash_squad.models.CategoryDataModel;
import com.creative.share.apps.wash_squad.models.CategoryModel;
import com.creative.share.apps.wash_squad.models.ProductDataModel;
import com.creative.share.apps.wash_squad.models.ProductModel;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
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

public class Fragment_Market extends Fragment {
    private HomeActivity activity;
    private FragmentMarketBinding binding;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelList;
    private Product2Adapter product2Adapter;
    private List<ProductModel> productModelList;
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
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        categoryModelList = new ArrayList<>();
        productModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);

        //  binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        categoryAdapter = new CategoryAdapter(categoryModelList, activity, this, lang);
        product2Adapter = new Product2Adapter(productModelList, activity,this,lang );
        manager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        binding.recyclerCategory.setLayoutManager(manager);
        binding.recyclerCategory.setAdapter(categoryAdapter);
        binding.recyclerProduct.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recyclerProduct.setAdapter(product2Adapter);
        getCategories();
        //        adapter = new MainServiceAdapter(serviceModelList,activity,this);
//        binding.recView.setAdapter(adapter);
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(activity,R.color.colorPrimary),ContextCompat.getColor(activity,R.color.color_second), Color.RED,Color.BLUE);
//        binding.swipeRefresh.setOnRefreshListener(this::getServices);


    }

    private void getCategories() {
        binding.progBarCategory.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getCategories()
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            binding.progBarCategory.setVisibility(View.GONE);
                            categoryModelList.clear();
                            categoryModelList.addAll(response.body().getData());
                            categoryAdapter.notifyDataSetChanged();
                            if(categoryModelList.size()>0){
                                binding.llNoItems.setVisibility(View.GONE);
                            }
                            else {
                                binding.llNoItems.setVisibility(View.VISIBLE);
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBarCategory.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void getProducts(String category_id) {
        binding.progBarOffers.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getProducts(category_id)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            binding.progBarOffers.setVisibility(View.GONE);
                            productModelList.clear();
                            productModelList.addAll(response.body().getData());
                            product2Adapter.notifyDataSetChanged();
                            if(productModelList.size()>0){
                                binding.llNoItems.setVisibility(View.GONE);
                            }
                            else {
                                binding.llNoItems.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            binding.progBarCategory.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    public void setItemCategory(CategoryModel model, int position) {
        getProducts(model.getId());
    }

    public void showProductDetials(String id) {
        Intent intent=new Intent(activity, ProductDetailsActivity.class);
        intent.putExtra("product_id",id);
        startActivity(intent);
    }

    public void showLink(String linkk) {

                Intent intent=new Intent(activity, WebViewActivity.class);
                intent.putExtra("url",linkk);
                startActivity(intent);

    }
}
