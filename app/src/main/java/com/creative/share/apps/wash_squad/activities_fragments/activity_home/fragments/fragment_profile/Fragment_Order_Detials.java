package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_profile;

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
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.databinding.FragmentOrderDetialsBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentOrderDetialsEvaluationBinding;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Order_Detials extends Fragment {

    private HomeActivity activity;
    private FragmentOrderDetialsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    final static private String Tag = "order_detials";
    private Order_Data_Model.OrderModel orderModel;
    public static Fragment_Order_Detials newInstance(Order_Data_Model.OrderModel orderModel) {

        Fragment_Order_Detials fragment_order_detials = new Fragment_Order_Detials();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Tag, orderModel);

        fragment_order_detials.setArguments(bundle);

        return fragment_order_detials;    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_detials, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userModel = preferences.getUserData(activity);
        orderModel = (Order_Data_Model.OrderModel) getArguments().getSerializable(Tag);
        binding.setLang(lang);
        binding.setOrderModel(orderModel);
        binding.tvSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderModel.getSee_images()!=null){
                    createSocialIntent(orderModel.getSee_images());
                }
            }
        });

    }
    private void createSocialIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void refresh(Order_Data_Model.OrderModel orderModel) {
        this.orderModel=orderModel;

        binding.setOrderModel(orderModel);


    }
}
