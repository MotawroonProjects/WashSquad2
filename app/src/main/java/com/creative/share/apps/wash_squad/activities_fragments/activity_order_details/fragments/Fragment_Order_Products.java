package com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.creative.share.apps.wash_squad.adapters.ProductAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentProductDetailsBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentProductsBinding;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_Order_Products extends Fragment {
    private FragmentProductsBinding binding;
    private static final String tag = "DATA";
    private Order_Data_Model.OrderModel orderModel;
    private OrderDetailsActivity activity;
    private List<Order_Data_Model.Products> productsList;
    private ProductAdapter adapter;

    public static Fragment_Order_Products newInstance(Order_Data_Model.OrderModel orderModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(tag, orderModel);
        Fragment_Order_Products fragment_order_products = new Fragment_Order_Products();
        fragment_order_products.setArguments(bundle);
        return fragment_order_products;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        productsList = new ArrayList<>();

        activity = (OrderDetailsActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderModel = (Order_Data_Model.OrderModel) bundle.getSerializable(tag);
        }
        if (orderModel.getOrder_sub_services() != null) {
            productsList.addAll(orderModel.getOrder_sub_services());
        }
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new ProductAdapter(productsList, activity);
        binding.recView.setAdapter(adapter);
    }

}
