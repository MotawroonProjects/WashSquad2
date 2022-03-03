package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_offers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_order.Fragment_Previous_Order;
import com.creative.share.apps.wash_squad.adapters.ViewPagerAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentOfferBinding;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Offer extends Fragment {

    private HomeActivity activity;
    private FragmentOfferBinding binding;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> title;
    public static Fragment_Offer newInstance() {
        return new Fragment_Offer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        fragmentList = new ArrayList<>();
        title = new ArrayList<>();
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(2);

        fragmentList.add(Fragment_Offers.newInstance());
        fragmentList.add(Fragment_Coupon.newInstance());
        title.add(getString(R.string.offer));
        title.add(getString(R.string.coupons));
        adapter = new ViewPagerAdapter(getChildFragmentManager(),1);
        adapter.addFragment(fragmentList);
        adapter.addTitles(title);
        binding.pager.setAdapter(adapter);

        for (int i =0;i<binding.tab.getChildCount();i++)
        {
            View view =  ((ViewGroup)binding.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams  params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(10,0,10,0);
        }

    }

}
