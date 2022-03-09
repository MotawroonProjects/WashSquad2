package com.creative.share.apps.wash_squad.activities_fragments.activity_sign_in;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.creative.share.apps.wash_squad.databinding.DialogAlertBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentSignUpBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.models.SignUpModel;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_Up extends Fragment implements OnCountryPickerListener, Listeners.ShowCountryDialogListener, Listeners.SignUpListener, Listeners.BackListener {
    private SignInActivity activity;
    private String lang;
    private FragmentSignUpBinding binding;
    private SignUpModel signUpModel;
    private CountryPicker picker;
    private Preferences preferences;


    public static Fragment_Sign_Up newInstance() {
        return new Fragment_Sign_Up();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        signUpModel = new SignUpModel();
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.newInstance();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setShowCountryListener(this);
        binding.setSignUpListener(this);
        binding.setSignUpModel(signUpModel);


        CreateCountryDialog();



    }




    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


        if (picker.getCountryFromSIM() != null) {
            updateUi(picker.getCountryFromSIM());

        } else if (telephonyManager != null && picker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updateUi(picker.getCountryByISO(telephonyManager.getNetworkCountryIso()));


        } else if (picker.getCountryByLocale(Locale.getDefault()) != null) {
            updateUi(picker.getCountryByLocale(Locale.getDefault()));

        } else {
            String code = "+966";
            signUpModel.setPhone_code(code.replace("+", "00966"));
            binding.tvCode.setText(code);

        }


    }

    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);
    }

    private void updateUi(Country country) {
        String code = country.getDialCode();
        signUpModel.setPhone_code(code.replace("+", "00"));
        binding.tvCode.setText(code);
    }

    @Override
    public void showDialog() {
        picker.showDialog(activity);
    }
    @Override
    public void back() {
        activity.back();
    }

    @Override
    public void checkDataSignUp(String name, String phone_code, String phone, String password) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        signUpModel = new SignUpModel(name, phone_code, phone, password);
        binding.setSignUpModel(signUpModel);

        if (signUpModel.isDataValid(activity)) {
            sign_up(name, phone_code, phone, password);
        }
    }

    private void sign_up(String name, String phone_code, String phone, String password) {

        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .signUp(name,phone_code,phone,password,1)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                CreateAlertDialog(response.body());


                            } else {
                                if (response.code() == 422) {
                                    Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                }else if (response.code() == 406) {
                                    Toast.makeText(activity,getString(R.string.em_exist), Toast.LENGTH_SHORT).show();

                                }else
                                    {
                                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                        try {

                                            Log.e("error",response.code()+"_"+response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
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
        } catch (Exception e) {
        }
    }


    private void CreateAlertDialog(UserModel userModel) {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(getString(R.string.you_will_receive_4_digit));

        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
            activity.displayFragmentCodeVerification(userModel,2);


        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


}
