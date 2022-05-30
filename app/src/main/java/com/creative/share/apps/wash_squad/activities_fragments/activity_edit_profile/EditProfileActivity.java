package com.creative.share.apps.wash_squad.activities_fragments.activity_edit_profile;

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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_change_password.PasswordActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_terms_conditions.TermsActivity;
import com.creative.share.apps.wash_squad.databinding.ActivityEditProfileBinding;
import com.creative.share.apps.wash_squad.databinding.ActivityHelpBinding;
import com.creative.share.apps.wash_squad.databinding.DialogLanguageBinding;
import com.creative.share.apps.wash_squad.databinding.DialogSelectImageBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.EditProfileModel;
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

public class EditProfileActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.EditProfileListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private ActivityEditProfileBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private CountryPicker countryPicker;
    private String code;
    private EditProfileModel edit_profile_model;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
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
        binding.setShowCountryListener(this);

        userModel = preferences.getUserData(this);
        binding.setUsermodel(userModel);
        if (userModel != null) {
            binding.setUsermodel(userModel);
            binding.edtName.setText(userModel.getFull_name());

            edit_profile_model = new EditProfileModel(userModel.getFull_name());

            binding.setEditprofilemodel(edit_profile_model);
            binding.setEditprofilelistener(this);
            binding.tvCode.setText(userModel.getPhone_code().replaceFirst("00", "+"));
            binding.edtPhone.setText(userModel.getPhone());
            code = userModel.getPhone_code();

            createCountryDialog();

//        }

            if (userModel.getLogo()!=null){
                binding.addPhoto.setVisibility(View.GONE);
            }
            binding.image.setOnClickListener(view -> CreateImageAlertDialog());
            binding.llChange.setOnClickListener(view -> {
                Intent intent = new Intent(this, PasswordActivity.class);
                startActivity(intent);
            });
//        binding.btnLogin.setOnClickListener(view -> activity.navigateToSinInActivity());


        }
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(EditProfileActivity.this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(EditProfileActivity.this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(EditProfileActivity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(EditProfileActivity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imgUri1 = getUriFromBitmap(bitmap);

            // edit_profile_model.editImageProfile(userModel.getUser().getId(),imgUri1.toString());
            editImageProfile(userModel.getId(), userModel.getFull_name(), imgUri1.toString());

        } else if (requestCode == IMG_REQ1 && resultCode == RESULT_OK && data != null) {

            imgUri1 = data.getData();
            editImageProfile(userModel.getId(), userModel.getFull_name(), imgUri1.toString());

            //  edit_profile_view_model.editImageProfile(userModel.getUser().getId(),imgUri1.toString());


        }

    }

    private void editImageProfile(int user_id, String full_name, String image) {
        ProgressDialog dialog = Common.createProgressDialog(this, this.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody id_part = Common.getRequestBodyText(String.valueOf(user_id));
        RequestBody name_part = Common.getRequestBodyText(String.valueOf(full_name));

        MultipartBody.Part image_part = Common.getMultiPart(this, Uri.parse(image), "logo");

        Api.getService(Tags.base_url)
                .editUserImage(id_part, name_part, image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());
                            userModel = response.body();
                            preferences.create_update_userData(EditProfileActivity.this, userModel);
                            edit_profile_model = new EditProfileModel(userModel.getFull_name());
Log.e("tttt",Tags.IMAGE_URL+response.body().getLogo());
                            binding.setEditprofilemodel(edit_profile_model);
                            Toast.makeText(EditProfileActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {
                                Toast.makeText(EditProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("respons", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // listener.onFailed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            Log.e("dddd",t.toString());
                            dialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void update(UserModel body) {

        userModel = body;
        preferences.create_update_userData(this, userModel);
        edit_profile_model = new EditProfileModel(userModel.getFull_name());
        binding.setUsermodel(userModel);

        binding.setEditprofilemodel(edit_profile_model);
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();


        code = userModel.getPhone_code();


    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(this);
    }

    @Override
    public void onSelectCountry(Country country) {

        updatePhoneCode(country);
    }

    private void updatePhoneCode(Country country) {
        code = country.getDialCode();
//        binding.tvCode.setText(country.getDialCode());

    }

    @Override
    public void checkDataEditProfile(String name) {

        edit_profile_model = new EditProfileModel(name);
        binding.setEditprofilelistener(this);

        if (edit_profile_model.isDataValid(this)) {
            editProfile(name);
        } else {
            //          binding.edtName.setError(getString(R.string.field_req));

        }
    }

    private void editProfile(String name) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {



            Api.getService(Tags.base_url)
                    .edit_profile(userModel.getId() + "", name)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                userModel = response.body();
                                preferences.create_update_userData(EditProfileActivity.this, userModel);
                                edit_profile_model = new EditProfileModel(userModel.getFull_name());

                                binding.setEditprofilemodel(edit_profile_model);
                                Toast.makeText(EditProfileActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(EditProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 403) {
                                    Toast.makeText(EditProfileActivity.this, R.string.user_not_active, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 404) {
                                    Toast.makeText(EditProfileActivity.this, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 405) {
                                    Toast.makeText(EditProfileActivity.this, R.string.not_active_phone, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(EditProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(EditProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(EditProfileActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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


//    public void refreshOrders() {
//
//        try {
//
//
//            Api.getService(Tags.base_url)
//                    .MyOrder(userModel.getId(), 1)
//                    .enqueue(new Callback<Order_Data_Model>() {
//                        @Override
//                        public void onResponse(Call<Order_Data_Model> call, Response<Order_Data_Model> response) {
//                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
//                                orderModelList.clear();
//                                orderModelList.addAll(response.body().getData());
//                                if (response.body().getData().size() > 0) {
//                                    // rec_sent.setVisibility(View.VISIBLE);
//                                    //  Log.e("data",response.body().getData().get(0).getAr_title());
//
//                                    binding.llNoorder.setVisibility(View.GONE);
//                                    myOrderAdapter.notifyDataSetChanged();
//                                    //   total_page = response.body().getMeta().getLast_page();
//
//                                } else {
//                                    binding.llNoorder.setVisibility(View.VISIBLE);
//
//                                }
//                            } else {
//                                binding.llNoorder.setVisibility(View.VISIBLE);
//
//                                //   Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                try {
//                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Order_Data_Model> call, Throwable t) {
//                            try {
//                                // binding.progBar.setVisibility(View.GONE);
//                                binding.llNoorder.setVisibility(View.VISIBLE);
//
//
//                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                Log.e("error", t.getMessage());
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            binding.llNoorder.setVisibility(View.VISIBLE);
//
//        }
//    }

    @Override
    public void back() {
        finish();
    }

}
