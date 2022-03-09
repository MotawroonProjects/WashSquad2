package com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.databinding.DialogAlertBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentForgetpasswordBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentSignInBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.models.ForgetModel;
import com.creative.share.apps.wash_squad.models.LoginModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ForgetPassword extends Fragment implements Listeners.CreateAccountListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener, Listeners.ForgetpasswordListner {
    private FragmentForgetpasswordBinding binding;
    private SignInActivity activity;
    private String lang;
    private Preferences preferences;
    private CountryPicker countryPicker;
    private ForgetModel forgetModel;

    public static Fragment_ForgetPassword newInstance() {
        return new Fragment_ForgetPassword();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_forgetpassword, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        forgetModel = new ForgetModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setForgetModel(forgetModel);
        binding.setCreateAccountListener(this);
        binding.setForgetpasswordListner(this);
        binding.setShowCountryListener(this);
        createCountryDialog();




    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM()!=null)
        {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        }else
            {
                String code = "+966";
                binding.tvCode.setText(code);
                forgetModel.setPhone_code(code.replace("+","00"));

            }

    }




    @Override
    public void checkDataForget(String phone_code, String phone) {
        if (phone.startsWith("0"))
        {
            phone = phone.replaceFirst("0","");
        }
        forgetModel = new ForgetModel(phone_code,phone);
        binding.setForgetModel(forgetModel);

        if (forgetModel.isDataValid(activity))
        {
            forget(phone_code,phone);
        }
    }




    @Override
    public void createNewAccount() {
        activity.displayFragmentSignUp();
    }

    @Override
    public void showDialog() {
        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
      updatePhoneCode(country);
    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        forgetModel.setPhone_code(country.getDialCode().replace("+","00"));

    }

    private void forget(String phone_code, String phone)
    {
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .forget(phone,phone_code)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();

                            if (response.isSuccessful())
                            {
                                //Log.e("data",response.body().getPassword_token());
                                CreateAlertDialog(response.body());
                            }
                            else
                            {
                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    //  Log.e("error",response.code()+"_"+response.errorBody()+response.message()+password+phone+phone_code);

                                } else if (response.code()==403)
                                {
                                    Toast.makeText(activity, R.string.user_not_active, Toast.LENGTH_SHORT).show();

                                }else if (response.code()==404)
                                {
                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else if (response.code()==406)
                                {
                                    Toast.makeText(activity, R.string.not_active_phone, Toast.LENGTH_SHORT).show();

                                }else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
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

    private void CreateAlertDialog(UserModel userModel)
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(getString(R.string.you_will_receive_4_digit));

        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
            activity.displayFragmentCodeVerification(userModel,1);


        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }



}
