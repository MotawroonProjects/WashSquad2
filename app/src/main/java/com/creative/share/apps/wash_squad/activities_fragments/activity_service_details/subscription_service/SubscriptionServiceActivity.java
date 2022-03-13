package com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaymentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.adapters.AdditionalServiceAdapter;
import com.creative.share.apps.wash_squad.adapters.BouquetAdapter;
import com.creative.share.apps.wash_squad.adapters.CarBrandAdapter;
import com.creative.share.apps.wash_squad.adapters.CarSizeAdapter;
import com.creative.share.apps.wash_squad.adapters.CarTypeAdapter;
import com.creative.share.apps.wash_squad.adapters.DayAdapter;
import com.creative.share.apps.wash_squad.adapters.TimeAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivitySubscriptionServiceBinding;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.CarSizeDataModel;
import com.creative.share.apps.wash_squad.models.CarTypeDataModel;
import com.creative.share.apps.wash_squad.models.DayModel;
import com.creative.share.apps.wash_squad.models.ItemSubscribeToUpload;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.SelectedLocation;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.models.TimeDataModel;
import com.creative.share.apps.wash_squad.models.UserModel;
import com.creative.share.apps.wash_squad.preferences.Preferences;
import com.creative.share.apps.wash_squad.remote.Api;
import com.creative.share.apps.wash_squad.share.Common;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionServiceActivity extends AppCompatActivity {
    private ActivitySubscriptionServiceBinding binding;
    private String lang;
    private SelectedLocation selectedLocation;
    private ServiceDataModel.ServiceModel serviceModel;
    private long date = 0;
    private List<ServiceDataModel.Level2> level2List;
    private List<CarTypeDataModel.CarTypeModel> carTypeModelList;
    private List<CarTypeDataModel.CarBrandModel> carBrandModelList;
    private AdditionalServiceAdapter additionalServiceAdapter;
    private BouquetAdapter bouquetAdapter;
    private CarTypeAdapter carTypeAdapter;
    private CarBrandAdapter carBrandAdapter;
    private GridLayoutManager manager1;
    private LinearLayoutManager manager2;
    private TimeDataModel.TimeModel timeModel;
    private DayModel dayModel;
    private String d;
    private List<ServiceDataModel.Level3> additional_service;
    private ItemSubscribeToUpload itemToUpload;
    private int service_id;
    private String service_name_ar, service_name_en;
    private UserModel userModel;
    private Preferences preferences;
    private List<ItemToUpload.SubServiceModel> subServiceModelList;
    private int count = 1;
    private int size_id = 0;
    private double total = 0.0, final_total = 0.0;
    private TimeAdapter timeAdapter;
    private DayAdapter dayAdapter;
    private List<TimeDataModel.TimeModel> timeModelList;
    private List<DayModel> dayModelList;
    private String selected_day;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_service);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            serviceModel = (ServiceDataModel.ServiceModel) intent.getSerializableExtra("data");
            service_id = intent.getIntExtra("service_id", 0);
            service_name_ar = intent.getStringExtra("service_name_ar");
            service_name_en = intent.getStringExtra("service_name_en");

        }
    }

    private void initView() {

        binding.consTime.setEnabled(false);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        timeModelList = new ArrayList<>();
        dayModelList = new ArrayList<>();
        dayModelList.add(new DayModel("SATURDAY"));
        dayModelList.add(new DayModel("SUNDAY"));
        dayModelList.add(new DayModel("MONDAY"));
        dayModelList.add(new DayModel("TUESDAY"));
        dayModelList.add(new DayModel("WEDNESDAY"));
        dayModelList.add(new DayModel("THURSDAY"));
        dayModelList.add(new DayModel("FRIDAY"));
//        dayAdapter.updateList(dayModelList);
        timeAdapter = new TimeAdapter(timeModelList, this);
        dayAdapter = new DayAdapter(dayModelList, this);
        carBrandModelList = new ArrayList<>();
        carBrandModelList.add(new CarTypeDataModel.CarBrandModel("إختر الماركة", "Choose brand"));
        subServiceModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        binding.setPrice(0.0);
        binding.setTotal(total);
        userModel = preferences.getUserData(this);
        itemToUpload = new ItemSubscribeToUpload();
        itemToUpload.setSub_services(subServiceModelList);
        itemToUpload.setService_id(service_id);
        itemToUpload.setAr_service_type(service_name_ar);
        itemToUpload.setEn_service_type(service_name_en);
        // itemToUpload.setLevel2(serviceModel.getLevel2());
        binding.setItemModel(itemToUpload);
        itemToUpload.setSub_serv_id(serviceModel.getId());
        additional_service = new ArrayList<>();
        level2List = new ArrayList<>();
        level2List.addAll(serviceModel.getLevel2());
        carTypeModelList = new ArrayList<>();
        carTypeModelList.add(new CarTypeDataModel.CarTypeModel("نوع السيارة", "Car type"));
        binding.tvDetails.setPaintFlags(binding.tvDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        carBrandAdapter = new CarBrandAdapter(this, carBrandModelList);
        binding.spinnerBrand.setAdapter(carBrandAdapter);

        binding.llBack.setOnClickListener(view -> finish());

        binding.setLevel2(serviceModel);
        if (lang.equals("ar")) {
            if (serviceModel.getAr_des() != null && !TextUtils.isEmpty(serviceModel.getAr_des())) {
                binding.tvDetails.setVisibility(View.VISIBLE);
            } else {
                binding.tvDetails.setVisibility(View.GONE);

            }
        } else {
            if (serviceModel.getEn_des() != null && !TextUtils.isEmpty(serviceModel.getEn_des())) {
                binding.tvDetails.setVisibility(View.VISIBLE);
            } else {
                binding.tvDetails.setVisibility(View.GONE);

            }
        }

        manager1 = new GridLayoutManager(this, 3);
        bouquetAdapter = new BouquetAdapter(level2List, this);
        binding.recView.setLayoutManager(manager1);
        binding.recView.setAdapter(bouquetAdapter);
        carTypeAdapter = new CarTypeAdapter(this, carTypeModelList);
        binding.spinner.setAdapter(carTypeAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    carBrandModelList.clear();
                    count = 1;
                    total = 0.0;
                    binding.setTotal(total);

                    binding.setPrice(0.0);
                    binding.tvCount.setText(String.valueOf(count));
                    itemToUpload.setCarType_id(0);
                    itemToUpload.setAr_car_type("");
                    itemToUpload.setEn_car_type("");
                    itemToUpload.setBrand_id(0);
                    binding.setItemModel(itemToUpload);
                    carBrandModelList.add(new CarTypeDataModel.CarBrandModel("إختر الماركة", "Choose brand"));
                    carBrandAdapter.notifyDataSetChanged();

                    if (additionalServiceAdapter != null) {
                        additionalServiceAdapter.clearSelection();
                    }

                    if (bouquetAdapter != null) {
//                        bouquetAdapter.setSelection(-1);
                    }

                    additional_service.clear();


                } else {
                    additional_service.clear();
                    carBrandModelList.clear();
                    carBrandModelList.add(new CarTypeDataModel.CarBrandModel("إختر الماركة", "Choose brand"));

                    itemToUpload.setCarType_id(carTypeModelList.get(i).getId());
                    itemToUpload.setAr_car_type(carTypeModelList.get(i).getAr_title());
                    itemToUpload.setEn_car_type(carTypeModelList.get(i).getEn_title());

                    binding.setItemModel(itemToUpload);

                    carBrandModelList.addAll(carTypeModelList.get(i).getLevel2());
                    carBrandAdapter.notifyDataSetChanged();
                    binding.spinnerBrand.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    count = 1;
                    total = 0.0;
                    additional_service.clear();

                    binding.setTotal(total);

                    binding.setPrice(0.0);
                    binding.tvCount.setText(String.valueOf(count));

                    itemToUpload.setCarSize_id(0);
                    itemToUpload.setService_price(0);
                    itemToUpload.setBrand_id(0);
                    itemToUpload.setEn_brand_name("");
                    itemToUpload.setAr_brand_name("");
                    binding.setItemModel(itemToUpload);
                    if (bouquetAdapter != null) {
//                        bouquetAdapter.setSelection(-1);
                    }

                    if (additionalServiceAdapter != null) {
                        additionalServiceAdapter.clearSelection();
                    }

                } else {
                    itemToUpload.setService_price(carBrandModelList.get(i).getSize_price());
                    itemToUpload.setBrand_id(carBrandModelList.get(i).getId());
                    itemToUpload.setEn_brand_name(carBrandModelList.get(i).getEn_title());
                    itemToUpload.setAr_brand_name(carBrandModelList.get(i).getAr_title());

                    binding.setItemModel(itemToUpload);

                    int pos = getCarSizeItemPos(carBrandModelList.get(i).getSize());

                    if (pos == -1) {
                        if (bouquetAdapter != null) {
//                            bouquetAdapter.setSelection(-1);
                        }
                        size_id = 0;
                        itemToUpload.setCarSize_id(0);

                    } else {
                        if (bouquetAdapter != null) {
//                            bouquetAdapter.setSelection(pos);
                        }

                        additional_service.clear();
                        size_id = level2List.get(pos).getId();
//                        getPrice(serviceModel.getId(), carSizeModelList.get(pos).getId());
                        itemToUpload.setCarSize_id(level2List.get(pos).getId());

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        manager2 = new LinearLayoutManager(this);
        binding.recViewService.setLayoutManager(manager2);

        binding.closeDay.setOnClickListener(view -> binding.flDay.setVisibility(View.GONE));
        binding.closeTime.setOnClickListener(view -> binding.flTime.setVisibility(View.GONE));
        binding.consDay.setOnClickListener(view -> {
            binding.flDay.setVisibility(View.VISIBLE);

        });
        binding.recViewDay.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewDay.setAdapter(dayAdapter);
        binding.recViewTime.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewTime.setAdapter(timeAdapter);
        binding.consTime.setOnClickListener(view -> {
            binding.flTime.setVisibility(View.VISIBLE);

        });

        binding.tvDone1.setOnClickListener(view -> {
            binding.flDay.setVisibility(View.GONE);
            binding.tvDay.setText(dayModel.getDay_text());
            selected_day=dayModel.getDay_text();
            binding.consTime.setEnabled(true);
            getTime();
        });
        binding.tvDone.setOnClickListener(view -> {
            binding.flTime.setVisibility(View.GONE);
            binding.tvTime.setText(timeModel.getTime_text() + timeModel.getType());

        });

        binding.tvDetails.setOnClickListener(view -> {
            if (binding.expandLayout.isExpanded()) {
                binding.expandLayout.collapse(true);
                binding.tvDetails.setText(getString(R.string.more_details));
            } else {
                binding.expandLayout.setExpanded(true, true);
                binding.tvDetails.setText(getString(R.string.less_details));

            }
        });

        binding.btnSendOrder.setOnClickListener(view -> {
            if (itemToUpload.isDataValidStep1(this)) {
                if (userModel != null) {
                    itemToUpload.setUser_id(userModel.getId());
                    itemToUpload.setUser_name(userModel.getFull_name());
                    itemToUpload.setUser_phone(userModel.getPhone());
                    itemToUpload.setTotal_price(final_total);
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("item", itemToUpload);
                    startActivityForResult(intent, 4);
                } else {
                    Common.CreateNoSignAlertDialog(this);
                }


            }

        });

        binding.imageIncrease.setOnClickListener(view -> {
            count++;
            itemToUpload.setAmount(count);
            binding.tvCount.setText(String.valueOf(count));
            final_total = total * count;

            binding.setTotal(final_total);
        });

        binding.imageDecrease.setOnClickListener(view -> {
            if (count > 1) {
                count--;
                itemToUpload.setAmount(count);

                final_total = total * count;
                binding.setTotal(final_total);
                binding.setTotal(total);
                binding.tvCount.setText(String.valueOf(count));
            }

        });
//        getCarSize();
        getCarType();


    }


    private void getPriceForAdditionalService(ServiceDataModel.Level3 level3, int size_id) {

        ProgressDialog dialog = Common.createProgressDialog(SubscriptionServiceActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getPrice(level3.getId(), size_id)
                .enqueue(new Callback<Double>() {
                    @Override
                    public void onResponse(Call<Double> call, Response<Double> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            Log.e("asss", response.body() + "__");
                            total = total + response.body();
                            final_total = total * count;
                            binding.setTotal(final_total);

                            level3.setPrice(response.body());
                            additional_service.add(level3);

                            subServiceModelList.clear();

                            for (ServiceDataModel.Level3 level3 : additional_service) {
                                ItemToUpload.SubServiceModel subServiceModel = new ItemToUpload.SubServiceModel(level3.getId(), level3.getPrice(), level3.getAr_title(), level3.getEn_title());
                                subServiceModelList.add(subServiceModel);


                            }

                            itemToUpload.setSub_services(subServiceModelList);
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(SubscriptionServiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SubscriptionServiceActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Double> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SubscriptionServiceActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SubscriptionServiceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

//    private void getCarSize() {
//
//        ProgressDialog dialog = Common.createProgressDialog(SubscriptionServiceActivity.this, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//
//        Api.getService(Tags.base_url)
//                .getCarSizes()
//                .enqueue(new Callback<CarSizeDataModel>() {
//                    @Override
//                    public void onResponse(Call<CarSizeDataModel> call, Response<CarSizeDataModel> response) {
//                        dialog.dismiss();
//                        if (response.isSuccessful() && response.body() != null) {
//                            carSizeModelList.clear();
//                            carSizeModelList.addAll(response.body().getData());
//                            bouquetAdapter.notifyDataSetChanged();
//
//                        } else {
//                            try {
//
//                                Log.e("error", response.code() + "_" + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            if (response.code() == 404) {
//                            } else if (response.code() == 500) {
//                                Toast.makeText(SubscriptionServiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Toast.makeText(SubscriptionServiceActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<CarSizeDataModel> call, Throwable t) {
//                        try {
//                            dialog.dismiss();
//                            if (t.getMessage() != null) {
//                                Log.e("error", t.getMessage());
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(SubscriptionServiceActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(SubscriptionServiceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//    }

    private void getCarType() {
        ProgressDialog dialog = Common.createProgressDialog(SubscriptionServiceActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getCarTypes()
                .enqueue(new Callback<CarTypeDataModel>() {
                    @Override
                    public void onResponse(Call<CarTypeDataModel> call, Response<CarTypeDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            carTypeModelList.addAll(response.body().getData());
                            carTypeAdapter.notifyDataSetChanged();

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(SubscriptionServiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SubscriptionServiceActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CarTypeDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SubscriptionServiceActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SubscriptionServiceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("location")) {
                selectedLocation = (SelectedLocation) data.getSerializableExtra("location");
                binding.setLocation(selectedLocation);
                itemToUpload.setLatitude(String.valueOf(selectedLocation.getLat()));
                itemToUpload.setLongitude(String.valueOf(selectedLocation.getLng()));
                itemToUpload.setAddress(selectedLocation.getAddress());
                binding.setItemModel(itemToUpload);

            }
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("date")) {
                binding.tvTime.setText("");
                timeModel = null;
                itemToUpload.setTime("");
                itemToUpload.setTime_type("");
                itemToUpload.setOrder_time_id(0);
                date = data.getLongExtra("date", 0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                d = dateFormat.format(new Date(date));
                binding.tvDay.setText(d);
                itemToUpload.setOrder_day(d);
                binding.setItemModel(itemToUpload);


            }
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("data")) {
                timeModel = (TimeDataModel.TimeModel) data.getSerializableExtra("data");
                String am_pm = timeModel.getType().equals("1") ? getString(R.string.am) : getString(R.string.pm);
                String time = timeModel.getTime_text();
                binding.tvTime.setText(String.format("%s %s", time, am_pm));

                itemToUpload.setOrder_time_id(timeModel.getId());
                itemToUpload.setTime(time);
                itemToUpload.setTime_type(am_pm);
                binding.setItemModel(itemToUpload);
            }
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            Intent intent = getIntent();
            if (intent != null) {

                setResult(RESULT_OK, intent);

            }
            finish();
        }
    }

    public void setItemCarSizeSelected(CarSizeDataModel.CarSizeModel carSizeModel) {
        itemToUpload.setCarSize_id(carSizeModel.getId());
        itemToUpload.setService_price(Double.parseDouble(carSizeModel.getPrice()));
        binding.setItemModel(itemToUpload);
    }


    public void setItemAdditionService(ServiceDataModel.Level3 serviceModel) {

        if (!hasItem(serviceModel)) {

            if (size_id != 0) {
                getPriceForAdditionalService(serviceModel, size_id);
            } else {
                if (additionalServiceAdapter != null) {
                    additionalServiceAdapter.clearSelection();
                }
                Toast.makeText(this, getString(R.string.ch_car_size), Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void removeAdditionalItem(ServiceDataModel.Level3 m_level3) {
        additional_service.remove(getItemPos(m_level3));
        Log.e("vvvvvvv", m_level3.getPrice() + "__");

        total = total - m_level3.getPrice();

        final_total = total * count;

        Log.e("bbb", final_total + "__");

        binding.setTotal(final_total);

        List<ItemToUpload.SubServiceModel> subServiceModelList = new ArrayList<>();
        for (ServiceDataModel.Level3 level3 : additional_service) {
            ItemToUpload.SubServiceModel subServiceModel = new ItemToUpload.SubServiceModel(level3.getId(), level3.getPrice(), level3.getAr_title(), level3.getEn_title());
            subServiceModelList.add(subServiceModel);

        }
        itemToUpload.setSub_services(subServiceModelList);
    }

    private boolean hasItem(ServiceDataModel.Level3 serviceModel) {
        for (ServiceDataModel.Level3 serviceModel2 : additional_service) {
            if (serviceModel.getId() == serviceModel2.getId()) {
                return true;
            }
        }
        return false;
    }

    private int getItemPos(ServiceDataModel.Level3 level3) {
        for (int i = 0; i < additional_service.size(); i++) {
            if (level3.getId() == additional_service.get(i).getId()) {
                return i;
            }
        }

        return 0;
    }

    private int getCarSizeItemPos(String id) {
        int position = -1;
        for (int pos = 0; pos < level2List.size(); pos++) {
            if (Integer.parseInt(id) == level2List.get(pos).getId()) {
                return pos;
            }
        }
        return position;
    }

    private void getTime() {
        Api.getService(Tags.base_url)
                .getTime(selected_day)
                .enqueue(new Callback<TimeDataModel>() {
                    @Override
                    public void onResponse(Call<TimeDataModel> call, Response<TimeDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            timeModelList.clear();
                            timeModelList.addAll(response.body().getData());
                            timeAdapter.updateList(timeModelList);

                            // updateUI(response.body().getData());

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(SubscriptionServiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SubscriptionServiceActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TimeDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SubscriptionServiceActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SubscriptionServiceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void setTimeItem(TimeDataModel.TimeModel model) {
        binding.tvDone.setVisibility(View.VISIBLE);
        this.timeModel = model;
    }

    public void setDayItem(DayModel model) {
        binding.tvDone1.setVisibility(View.VISIBLE);
        this.dayModel = model;
    }
}