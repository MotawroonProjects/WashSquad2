package com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_edit_profile.EditProfileActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_help.HelpActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_subscribtion.SubscribtionActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_wallet.WalletActivity;
import com.creative.share.apps.wash_squad.activities_fragments.questions_activity.QuestionsActivity;
import com.creative.share.apps.wash_squad.adapters.MyOrderAdapter;
import com.creative.share.apps.wash_squad.databinding.DialogSelectImageBinding;
import com.creative.share.apps.wash_squad.databinding.FragmentProfileBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.models.CouponDataModel;
import com.creative.share.apps.wash_squad.models.DayModel;
import com.creative.share.apps.wash_squad.models.EditProfileModel;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.SettingModel;
import com.creative.share.apps.wash_squad.models.SubscribtionDataModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment implements Listeners.EditProfileListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private CountryPicker countryPicker;
    private String lang;
    private String code;
    private EditProfileModel edit_profile_model;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private int count;
    private int status;
    private ArrayList<DayModel> dayModelList;
    private ArrayList<String> dayModelList2;
    private SettingModel settingModel;

    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        setDays();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setShowCountryListener(this);

        userModel = preferences.getUserData(activity);
        binding.setUsermodel(userModel);
        binding.cardSupport.setOnClickListener(view -> {
            Intent intent = new Intent(activity, QuestionsActivity.class);
            startActivity(intent);
        });
        binding.editUser.setOnClickListener(view -> {
            Intent intent = new Intent(activity, EditProfileActivity.class);
            startActivityForResult(intent, 12);

        });
        binding.imageHelp.setOnClickListener(view -> {
            Intent intent = new Intent(activity, HelpActivity.class);
            startActivityForResult(intent, 11);

        });
        binding.btnDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SubscribtionActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0) {

                } else {

                }
            }
        });
        getSetting();
        binding.cardSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SubscribtionActivity.class);
                startActivity(intent);
            }
        });
        binding.cardWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, WalletActivity.class);
                startActivity(intent);
            }
        });
        binding.cardRate.setOnClickListener(view -> {
            String appId = activity.getPackageName();
            Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appId));
            boolean marketFound = false;

            final List<ResolveInfo> otherApps = activity.getPackageManager()
                    .queryIntentActivities(rateIntent, 0);
            for (ResolveInfo otherApp : otherApps) {
                if (otherApp.activityInfo.applicationInfo.packageName
                        .equals("com.android.vending")) {

                    ActivityInfo otherAppActivity = otherApp.activityInfo;
                    ComponentName componentName = new ComponentName(
                            otherAppActivity.applicationInfo.packageName,
                            otherAppActivity.name
                    );
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    rateIntent.setComponent(componentName);
                    startActivity(rateIntent);
                    marketFound = true;
                    break;

                }
            }

            if (!marketFound) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
                startActivity(webIntent);
            }

        });
//        if (userModel!=null)
//        {
//            binding.setUsermodel(userModel);
//            binding.edtName.setText(userModel.getFull_name());
//
//            edit_profile_model = new EditProfileModel(userModel.getFull_name());
//
//            binding.setEditprofilemodel(edit_profile_model);
//            binding.setEditprofilelistener(this);
//            binding.tvCode.setText(userModel.getPhone_code().replaceFirst("00", "+"));
//            binding.edtPhone.setText(userModel.getPhone());
//            code = userModel.getPhone_code();
//
//            createCountryDialog();
//
//        }
        getSubscribtion();
        binding.image.setOnClickListener(view -> CreateImageAlertDialog());
//        binding.llChange.setOnClickListener(view -> activity.displayFragmentNewpass());
//        binding.btnLogin.setOnClickListener(view -> activity.navigateToSinInActivity());

    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_select_image, null, false);


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
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, IMG_REQ2);
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
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
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


        } else if (requestCode == 11 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("lang")) {
                String lang = data.getStringExtra("lang");
                new Handler()
                        .postDelayed(() -> activity.refreshActivity(lang), 1000);

            } else if (data.hasExtra("data")) {
                activity.DeleteTokenFireBase();

            }
        } else if (requestCode == 12) {
            userModel = preferences.getUserData(activity);
            update(userModel);
        }

    }

    private void editImageProfile(int user_id, String full_name, String image) {
        ProgressDialog dialog = Common.createProgressDialog(activity, activity.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody id_part = Common.getRequestBodyText(String.valueOf(user_id));
        RequestBody name_part = Common.getRequestBodyText(String.valueOf(full_name));

        MultipartBody.Part image_part = Common.getMultiPart(activity, Uri.parse(image), "logo");

        Api.getService(Tags.base_url)
                .editUserImage(id_part, name_part, image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());

                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            update(response.body());

                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void update(UserModel body) {

        userModel = body;
        preferences.create_update_userData(activity, userModel);
        edit_profile_model = new EditProfileModel(userModel.getFull_name());
        binding.setUsermodel(userModel);

        binding.setEditprofilemodel(edit_profile_model);
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();


        code = userModel.getPhone_code();


    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(activity);
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

        if (edit_profile_model.isDataValid(activity)) {
            editProfile(name);
        } else {
            //          binding.edtName.setError(getString(R.string.field_req));

        }
    }

    private void editProfile(String name) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
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
                                preferences.create_update_userData(activity, userModel);
                                edit_profile_model = new EditProfileModel(userModel.getFull_name());

                                binding.setEditprofilemodel(edit_profile_model);
                                Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();


                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 403) {
                                    Toast.makeText(activity, R.string.user_not_active, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 404) {
                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 405) {
                                    Toast.makeText(activity, R.string.not_active_phone, Toast.LENGTH_SHORT).show();

                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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

    private void getSubscribtion() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("data", "ddd");
        Api.getService(Tags.base_url)
                .getSubscribtion(userModel.getId() + "")
                .enqueue(new Callback<SubscribtionDataModel>() {
                    @Override
                    public void onResponse(Call<SubscribtionDataModel> call, Response<SubscribtionDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            updateui(response.body());

                        } else {
                            if (response.code() == 201) {
                                binding.llData.setVisibility(View.GONE);
                                binding.tvNoDetails.setVisibility(View.VISIBLE);
                            }
                            // binding.swipeRefresh.setRefreshing(false);

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                                //   binding.llNoCoupon.setVisibility(View.VISIBLE);
                            } else if (response.code() == 500) {
                                // Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscribtionDataModel> call, Throwable t) {
                        try {
                            // binding.swipeRefresh.setRefreshing(false);

                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //     Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    //    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateui(SubscribtionDataModel body) {
        status = 0;
        count = 0;
        binding.llData.setVisibility(View.VISIBLE);
        binding.tvNoDetails.setVisibility(View.GONE);

        for (int i = 0; i < body.getWash_sub().size(); i++) {
            if (body.getWash_sub().get(i).getStatus().equals("done")) {
                count += 1;
            } else {
                binding.setModel(body.getWash_sub().get(i));
                binding.setDay(dayModelList.get(dayModelList2.indexOf(body.getWash_sub().get(i).getDay().toUpperCase())).getDay_text());

                status = 1;

            }
        }
        if (status == 1) {
            binding.btnDetials.setVisibility(View.VISIBLE);
            binding.btnSubscribe.setText(getResources().getString(R.string.postpone_an_appointment));
        } else {
            binding.btnDetials.setVisibility(View.GONE);
        }
        binding.setCount((body.getWash_sub().size() - count) + "");

    }
    private void setDays() {
        dayModelList = new ArrayList<>();
        dayModelList.add(new DayModel(getString(R.string.Saturday)));
        dayModelList.add(new DayModel(getString(R.string.sunday)));
        dayModelList.add(new DayModel(getString(R.string.monday)));
        dayModelList.add(new DayModel(getString(R.string.tuesday)));
        dayModelList.add(new DayModel(getString(R.string.wendesday)));
        dayModelList.add(new DayModel(getString(R.string.thursday)));
        dayModelList.add(new DayModel(getString(R.string.friday)));
        dayModelList2 = new ArrayList<>();
        dayModelList2.add("SATURDAY");
        dayModelList2.add("SUNDAY");
        dayModelList2.add("MONDAY");
        dayModelList2.add("TUESDAY");
        dayModelList2.add("WEDNESDAY");
        dayModelList2.add("THURSDAY");
        dayModelList2.add("FRIDAY");
    }
    private void getSetting() {

        Api.getService(Tags.base_url)
                .getStting()
                .enqueue(new Callback<SettingModel>() {
                    @Override
                    public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            settingModel = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<SettingModel> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
    }


}
