package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_order;

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
import com.creative.share.apps.wash_squad.adapters.ViewPagerAdapter;
import com.creative.share.apps.wash_squad.databinding.FragmentOrderBinding;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Order extends Fragment {

    private HomeActivity activity;
    private FragmentOrderBinding binding;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> title;
    private UserModel userModel;
    private Preferences preferences;
    public static Fragment_Order newInstance() {
        return new Fragment_Order();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);

        if (userModel!=null)
        {
            fragmentList = new ArrayList<>();
            title = new ArrayList<>();
            binding.tab.setupWithViewPager(binding.pager);
            binding.pager.setOffscreenPageLimit(2);
            binding.btnLogin.setOnClickListener(view -> activity.navigateToSinInActivity());

            fragmentList.add(Fragment_Current_Order.newInstance());
            fragmentList.add(Fragment_Previous_Order.newInstance());
            title.add(getString(R.string.current));
            title.add(getString(R.string.prevoius));
            adapter = new ViewPagerAdapter(getChildFragmentManager(), 1);
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

        binding.btnLogin.setOnClickListener(v -> {
            activity.navigateToSinInActivity();
        });

    }

}
