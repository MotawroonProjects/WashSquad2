package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_order;

import android.app.ProgressDialog;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.creative.share.apps.wash_squad.adapters.MyOrderAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentCurrentPreviousOrderBinding;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Current_Order extends Fragment {

    private HomeActivity activity;
    private FragmentCurrentPreviousOrderBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private MyOrderAdapter myOrderAdapter;
    private List<Order_Data_Model.OrderModel> orderModelList;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private int current_page = 1;


    public static Fragment_Current_Order newInstance() {
        return new Fragment_Current_Order();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_previous_order,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        orderModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        myOrderAdapter = new MyOrderAdapter(orderModelList, activity, this);
        binding.recView.setAdapter(myOrderAdapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0) {
                    int totalItems = myOrderAdapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();

                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 5 && !isLoading) {
                        isLoading = true;
                        orderModelList.add(null);
                        myOrderAdapter.notifyItemInserted(orderModelList.size() - 1);
                        if (userModel != null) {
                            int page = current_page + 1;
                            loadMore(page);
                        }
                    }


                }
            }
        });

        getOrders();

    }


    public void getOrders() {

        try {


            Log.e("mmmmmmm",userModel.getId()+"");
            Api.getService(Tags.base_url)
                    .currentOrder(userModel.getId(), 1)
                    .enqueue(new Callback<Order_Data_Model>() {
                        @Override
                        public void onResponse(Call<Order_Data_Model> call, Response<Order_Data_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                orderModelList.clear();
                                orderModelList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    myOrderAdapter.notifyDataSetChanged();

                                } else {
                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {

                                if (response.code()==404)
                                {
                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }else if (response.code()==500){
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                                }


                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Data_Model> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);


                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void loadMore(int page) {
        try {


            Api.getService(Tags.base_url)
                    .MyOrder(userModel.getId(), page)
                    .enqueue(new Callback<Order_Data_Model>() {
                        @Override
                        public void onResponse(Call<Order_Data_Model> call, Response<Order_Data_Model> response) {
                            orderModelList.remove(orderModelList.size() - 1);
                            myOrderAdapter.notifyItemRemoved(orderModelList.size() - 1);
                            isLoading = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                orderModelList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page = response.body().getCurrent_page();
                                myOrderAdapter.notifyDataSetChanged();

                            } else {
                                //     Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Data_Model> call, Throwable t) {
                            try {
                                orderModelList.remove(orderModelList.size() - 1);
                                myOrderAdapter.notifyItemRemoved(orderModelList.size() - 1);
                                isLoading = false;
                                //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            orderModelList.remove(orderModelList.size() - 1);
            myOrderAdapter.notifyItemRemoved(orderModelList.size() - 1);
            isLoading = false;
        }
    }

    public void setItemData(Order_Data_Model.OrderModel orderModel) {
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra("data",orderModel);
        startActivity(intent);

    }


}
