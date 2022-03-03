package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.adapters.CartAdapterAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentCartBinding;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.singleton.SingleTon;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Cart extends Fragment {

    private HomeActivity activity;
    private FragmentCartBinding binding;
    private Preferences preferences;
    private LinearLayoutManager manager;
    private UserModel userModel;
    private CartAdapterAdapter adapter;
    private SingleTon singleTon;
    private List<ItemToUpload> itemToUploadList;

    public static Fragment_Cart newInstance() {
        return new Fragment_Cart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        singleTon = SingleTon.newInstance();

        itemToUploadList = singleTon.getData();
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        adapter = new CartAdapterAdapter(itemToUploadList,activity,this);
        binding.recView.setAdapter(adapter);
        binding.recView.setNestedScrollingEnabled(true);
        if (singleTon.getItemsCount()>0)
        {
            binding.ll.setVisibility(View.GONE);
            binding.btnOtherOrder.setVisibility(View.VISIBLE);



        }else
            {
                binding.btnOtherOrder.setVisibility(View.GONE);

                binding.ll.setVisibility(View.VISIBLE);
            }

        binding.btnOrderNow.setOnClickListener(view -> activity.DisplayFragmentMain());
        binding.btnOtherOrder.setOnClickListener(view -> activity.DisplayFragmentMain());

    }

    public void setItemDataToUpload(ItemToUpload itemToUpload, int adapterPosition)
    {

        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .addOrder(itemToUpload)
                    .enqueue(new Callback<Order_Data_Model.OrderModel>() {
                        @Override
                        public void onResponse(Call<Order_Data_Model.OrderModel> call, Response<Order_Data_Model.OrderModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                itemToUploadList.remove(adapterPosition);
                                adapter.notifyDataSetChanged();
                                activity.updateCount(itemToUploadList.size());
                                if (itemToUploadList.size()==0)
                                {
                                    binding.btnOtherOrder.setVisibility(View.GONE);
                                    binding.ll.setVisibility(View.VISIBLE);
                                    singleTon.clear();
                                }
                                Common.CreateDialogAlert(activity,getString(R.string.order_sent_suc)+" "+response.body().getId());

                            }else
                            {
                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                }else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Order_Data_Model.OrderModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
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
        }catch (Exception e){
            dialog.dismiss();

        }

    }
}
