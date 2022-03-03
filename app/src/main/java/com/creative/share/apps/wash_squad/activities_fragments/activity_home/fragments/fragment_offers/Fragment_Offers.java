package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_offers;

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

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.adapters.OffersAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentOffersBinding;
import com.creative.share.apps.wash_squad.models.OfferDataModel;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment {

    private HomeActivity activity;
    private FragmentOffersBinding binding;
    private List<OfferDataModel.OfferModel> offerModelList;
    private LinearLayoutManager manager;
    private OffersAdapter adapter;
    public static Fragment_Offers  newInstance() {
        return new Fragment_Offers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        offerModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        adapter = new OffersAdapter(offerModelList,activity);
        binding.recView.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::getOffers);
        getOffers();



    }

    private void getOffers() {

        Api.getService(Tags.base_url)
                .getOffers()
                .enqueue(new Callback<OfferDataModel>() {
                    @Override
                    public void onResponse(Call<OfferDataModel> call, Response<OfferDataModel> response) {
                        binding.swipeRefresh.setRefreshing(false);
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            offerModelList.clear();
                            offerModelList.addAll(response.body().getData());

                            if (offerModelList.size()>0)
                            {
                                binding.llNoOffer.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            }else
                                {
                                    binding.llNoOffer.setVisibility(View.VISIBLE);

                                }

                        } else {
                            binding.swipeRefresh.setRefreshing(false);

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                                binding.llNoOffer.setVisibility(View.VISIBLE);
                            } else if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OfferDataModel> call, Throwable t) {
                        try {
                            binding.swipeRefresh.setRefreshing(false);

                            binding.progBar.setVisibility(View.GONE);
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
}
