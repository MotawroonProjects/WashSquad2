package com.creative.share.apps.wash_squad.activities_fragments.activity_change_password;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.ActivityEditProfileBinding;
import com.creative.share.apps.wash_squad.databinding.ActivityNewPasswordBinding;
import com.creative.share.apps.wash_squad.databinding.DialogSelectImageBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.EditProfileModel;
import com.creative.share.apps.wash_squad.models.Password2Model;
import com.creative.share.apps.wash_squad.models.PasswordModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.PasswordListner {
    private ActivityNewPasswordBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private CountryPicker countryPicker;
    private Password2Model passwordModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_password);
        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        preferences = Preferences.newInstance();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        passwordModel = new Password2Model();
        userModel = preferences.getUserData(this);
        binding.setPassModel(passwordModel);
        binding.setPassListener(this);
    }

    private void change_pass(String pass) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .edit_pass(userModel.getId() + "", pass)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                userModel = response.body();
                                preferences.create_update_userData(PasswordActivity.this, userModel);
                                Toast.makeText(PasswordActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                finish();


                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(PasswordActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 403) {
                                    Toast.makeText(PasswordActivity.this, R.string.user_not_active, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 404) {
                                    Toast.makeText(PasswordActivity.this, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 405) {
                                    Toast.makeText(PasswordActivity.this, R.string.not_active_phone, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(PasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(PasswordActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(PasswordActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }
    }


    @Override
    public void back() {
        finish();
    }

    @Override
    public void checkDatapass(String pass) {
        if (passwordModel.isDataValid(this)) {
            change_pass(passwordModel.getPassword());
        }
    }
}
