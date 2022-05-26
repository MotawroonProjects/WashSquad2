package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.databinding.FragmentHomeBinding;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);


        setUpBottomNavigation();
        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:

                    activity.DisplayFragmentProfile();

                    break;
                case 1:
                    activity.DisplayFragmentMain();



                    break;
                case 2:
                    activity.DisplayFragmentOffer();


                    break;
                case 3:
                    activity.DisplayFragmentOrder();
                    break;
                case 4:
                    activity.DisplayFragmentMarket();
                    break;
            }
            return false;
        });

    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.profile2), R.drawable.ic_user);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.services), R.drawable.logo_only);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.offer), R.drawable.ic_offer);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getResources().getString(R.string.my_order), R.drawable.ic_paper);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getResources().getString(R.string.market), R.drawable.ic_nav_cart);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(activity, R.color.color_second));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(activity, R.color.gray5));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);
        binding.ahBottomNav.addItem(item5);

        binding.ahBottomNav.setCurrentItem(1);


    }

    public void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);
    }


    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

}
