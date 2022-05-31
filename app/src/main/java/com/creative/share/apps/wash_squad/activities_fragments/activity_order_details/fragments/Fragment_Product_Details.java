package com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.creative.share.apps.wash_squad.databinding.FragmentProductDetailsBinding;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.tags.Tags;

import io.paperdb.Paper;

public class Fragment_Product_Details extends Fragment {
    private FragmentProductDetailsBinding binding;
    private static final String tag= "DATA";
    private Order_Data_Model.OrderModel orderModel;
    private OrderDetailsActivity activity;
    private String lang;

    public static Fragment_Product_Details newInstance(Order_Data_Model.OrderModel orderModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(tag,orderModel);
        Fragment_Product_Details fragment_product_details = new Fragment_Product_Details();
        fragment_product_details.setArguments(bundle);
        return fragment_product_details;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details,container,false);
        initView();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setModel(orderModel);
    }

    private void initView() {

        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            orderModel = (Order_Data_Model.OrderModel) bundle.getSerializable(tag);
        }
        binding.llprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Tags.base_url + "api/order/print/" + orderModel.getId();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
//                Intent intent = new Intent(OrderDetailsActivity.this, PrintActivity.class);
//                intent.putExtra("url", );
//                startActivity(intent);
            }
        });

    }


}
