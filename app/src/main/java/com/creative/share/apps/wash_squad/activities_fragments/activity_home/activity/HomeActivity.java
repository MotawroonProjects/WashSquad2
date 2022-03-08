package com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_help.HelpActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Cart;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Home;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_offers.Fragment_Offer;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_order.Fragment_Order;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_profile.Fragment_Order_Detials;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_profile.Fragment_Order_Detials_Evaluation;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_profile.Fragment_Profile;
import com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in.Fragment_Newpass;
import com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.wash_squad.databinding.ActivityHomeBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.singleton.SingleTon;
import com.creative.share.apps.wash_squad.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private int fragment_count = 0;
    private Fragment_Home fragment_home;
    private Fragment_Main fragment_main;
    private Fragment_Cart fragment_cart;
    private Fragment_Profile fragment_profile;
    private Fragment_Order_Detials_Evaluation fragment_order_detials_evaluation;
    private Fragment_Order_Detials fragment_order_detials;
    private Fragment_Offer fragment_offer;
    private Fragment_Order fragment_order;

    private Preferences preferences;
    private UserModel userModel;
    private SingleTon singleTon;
    private Fragment_Newpass fragment_newpass;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();

        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentMain();

        }


    }


    private void initView() {
        singleTon = SingleTon.newInstance();
        Paper.init(this);
        fragmentManager = this.getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        if (userModel != null) {
            // EventBus.getDefault().register(this);
            updateTokenFireBase();

        }
        String lastVisit = preferences.getLastVisit(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String now = dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

        if (!lastVisit.equals(now)) {
            updateVisit(now, (Calendar.getInstance().getTimeInMillis() / 1000));

        }

        binding.imageHelp.setOnClickListener(view -> {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivityForResult(intent, 11);

        });

//        binding.imageCart.setOnClickListener(view -> DisplayFragmentCart());
//
//        binding.imageLogout.setOnClickListener(view -> {
//            if (userModel != null) {
//                //logout();
//                DeleteTokenFireBase();
//            } else {
//                navigateToSinInActivity();
//            }
//
//        });

    }

    private void logout() {

      /*  Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();*/
//Log.e("llll","kkkkkk");
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .logout(userModel.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                // Log.e("token",response.body().getName());
                                preferences.create_update_userData(HomeActivity.this, null);
                                preferences.createSession(HomeActivity.this, Tags.session_logout);
                                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.e("llll", "kkkkkk");

                               /* if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
                                } else*/
                                if (response.code() == 500) {
                                    Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("lll", e.getMessage().toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cart_count = singleTon.getItemsCount();
//        updateCount(cart_count);
    }

//    public void updateCount(int cart_count) {
//        if (fragment_home != null && fragment_home.isAdded()) {
//            binding.tvCartCount.setText(String.valueOf(cart_count));
//        }
//    }

    private void updateVisit(String now, long time) {
        /*Api.getService(Tags.base_url)
                .updateVisit(time,2)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            preferences.setLastVisit(HomeActivity.this,now);
                        }else
                        {
                            try {
                                Log.e("errorVisitCode",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage()+"_");
                        }catch (Exception e){}
                    }
                });*/
    }

    public void DisplayFragmentHome() {

        try {
            fragment_count += 1;
            if (fragment_home == null) {
                fragment_home = Fragment_Home.newInstance();
            }

            if (fragment_home.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_home).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

            }
            binding.toolbar.setVisibility(View.VISIBLE);

        } catch (Exception e) {
        }

    }

    public void DisplayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }

            if (fragment_order != null && fragment_order.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_order).commit();
            }
            if (fragment_offer != null && fragment_offer.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offer).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }
            binding.toolbar.setVisibility(View.VISIBLE);

            binding.setTitle(getString(R.string.services));
            binding.imageHelp.setVisibility(View.GONE);
            if (fragment_home != null && fragment_home.isAdded()) {
                fragment_home.updateBottomNavigationPosition(3);
            }
        } catch (Exception e) {
        }


    }

    public void DisplayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_offer != null && fragment_offer.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offer).commit();
            }
            if (fragment_order != null && fragment_order.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_order).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }
            binding.toolbar.setVisibility(View.GONE);
            //binding.setTitle(getString(R.string.profile));
           // binding.imageHelp.setVisibility(View.VISIBLE);
            if (fragment_home != null && fragment_home.isAdded()) {
                fragment_home.updateBottomNavigationPosition(0);
            }
        } catch (Exception e) {
        }


    }

    public void DisplayFragmentOffer() {
        try {
            if (fragment_offer == null) {
                fragment_offer = Fragment_Offer.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_order != null && fragment_order.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_order).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }
            if (fragment_offer.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_offer).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_offer, "fragment_offer").addToBackStack("fragment_offer").commit();

            }
            binding.toolbar.setVisibility(View.VISIBLE);

            binding.setTitle(getString(R.string.offer));
            binding.imageHelp.setVisibility(View.GONE);
            if (fragment_home != null && fragment_home.isAdded()) {
                fragment_home.updateBottomNavigationPosition(2);
            }
        } catch (Exception e) {
        }


    }

    public void DisplayFragmentCart() {
        try {
            fragment_cart = Fragment_Cart.newInstance();

            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }

            if (fragment_offer != null && fragment_offer.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offer).commit();
            }

            if (fragment_order != null && fragment_order.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_order).commit();
            }

            if (fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_cart).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_cart, "fragment_cart").addToBackStack("fragment_cart").commit();

            }
            binding.toolbar.setVisibility(View.VISIBLE);

            binding.setTitle(getString(R.string.cart));
            binding.imageHelp.setVisibility(View.GONE);

        } catch (Exception e) {
        }


    }

    public void DisplayFragmentOrder() {
        try {
            if (fragment_order == null) {
                fragment_order = Fragment_Order.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_offer != null && fragment_offer.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offer).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }
            if (fragment_order.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_order).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_order, "fragment_order").addToBackStack("fragment_order").commit();

            }
            binding.toolbar.setVisibility(View.VISIBLE);

            binding.setTitle(getString(R.string.my_order));
            binding.imageHelp.setVisibility(View.GONE);
            if (fragment_home != null && fragment_home.isAdded()) {
                fragment_home.updateBottomNavigationPosition(1);
            }
        } catch (Exception e) {
        }


    }


    public void displayFragmentNewpass() {
        fragment_count++;
        fragment_newpass = Fragment_Newpass.newInstance(userModel, 2);

        fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_newpass, "fragment_newpass").addToBackStack("fragment_newpass").commit();

    }

/*
    public void DisplayFragmentOrderDetials(Order_Data_Model.OrderModel orderModel) {
        try {
            if (fragment_order_detials == null) {
                fragment_order_detials = Fragment_Order_Detials.newInstance(orderModel);
            } else {
                fragment_order_detials.refresh(orderModel);
            }

            if (fragment_order_detials_evaluation != null && fragment_order_detials_evaluation.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_order_detials_evaluation).commit();
            }

            if (fragment_order_detials.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_order_detials).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_order_details_container, fragment_order_detials, "fragment_order_detials").addToBackStack("fragment_order_detials").commit();

            }

        } catch (Exception e) {
        }


    }
*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("lang")) {
                String lang = data.getStringExtra("lang");
                new Handler()
                        .postDelayed(() -> refreshActivity(lang), 1000);

            }
        }
    }

    public void refreshActivity(String lang) {
        preferences.selectedLanguage(this, lang);
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (fragment_count > 1) {
            super.onBackPressed();
            fragment_count--;

        } else {
            if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
                if (userModel == null) {
                    navigateToSinInActivity();
                } else {
                    finish();
                }
            } else {
                DisplayFragmentMain();
            }

        }
    }

    public void navigateToSinInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


    public void refreshprofile(UserModel userModel) {
        this.userModel = userModel;
        if (fragment_profile != null) {
            fragment_profile.update(userModel);
        }
        back();
    }

    private void updateTokenFireBase() {


        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();

                try {

                    try {

                        Api.getService(Tags.base_url)
                                .updatePhoneToken(token, userModel.getId(), "1")
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            Log.e("token", "updated successfully");
                                        } else {
                                            try {

                                                Log.e("error", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        try {

                                            if (t.getMessage() != null) {
                                                Log.e("error", t.getMessage());
                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    } catch (Exception e) {


                    }
                } catch (Exception e) {


                }

            }
        });
    }
    private void DeleteTokenFireBase() {


        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();

                try {

                    try {

                        Api.getService(Tags.base_url)
                                .deltePhoneToken(token, userModel.getId())
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                           logout();
                                        } else {
                                            try {

                                                Log.e("error", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        try {

                                            if (t.getMessage() != null) {
                                                Log.e("error", t.getMessage());
                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    } catch (Exception e) {


                    }
                } catch (Exception e) {


                }

            }
        });
    }


}
