package com.creative.share.apps.wash_squad.activities_fragments.activity_service_details;

import android.annotation.SuppressLint;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadrosid.svgloader.SvgLoader;
import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_payment.PaymentActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_time.TimeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.calendar_activity.CalendarActivity;
import com.creative.share.apps.wash_squad.adapters.AdditionalServiceAdapter;
import com.creative.share.apps.wash_squad.adapters.CarBrandAdapter;
import com.creative.share.apps.wash_squad.adapters.CarSizeAdapter;
import com.creative.share.apps.wash_squad.adapters.CarTypeAdapter;
import com.creative.share.apps.wash_squad.adapters.SpinnerAreaAdapter;
import com.creative.share.apps.wash_squad.adapters.TimeAdapter;
import com.creative.share.apps.wash_squad.databinding.ActivityServiceDetailsBinding;
import com.creative.share.apps.wash_squad.interfaces.Listeners;
import com.creative.share.apps.wash_squad.language.LanguageHelper;
import com.creative.share.apps.wash_squad.models.AreaDataModel;
import com.creative.share.apps.wash_squad.models.AreaModel;
import com.creative.share.apps.wash_squad.models.CarSizeDataModel;
import com.creative.share.apps.wash_squad.models.CarTypeDataModel;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
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

public class ServiceDetailsActivity extends AppCompatActivity {
    private ActivityServiceDetailsBinding binding;
    private String lang;
    private SelectedLocation selectedLocation;
    private ServiceDataModel.Level2 serviceModel;
    private long date = 0;
    private List<CarSizeDataModel.CarSizeModel> carSizeModelList;
    private List<CarTypeDataModel.CarTypeModel> carTypeModelList;
    private List<CarTypeDataModel.CarBrandModel> carBrandModelList;
    private AdditionalServiceAdapter additionalServiceAdapter;
    private CarSizeAdapter carSizeAdapter;
    private CarTypeAdapter carTypeAdapter;
    private CarBrandAdapter carBrandAdapter;
    private GridLayoutManager manager1;
    private LinearLayoutManager manager2;
    private TimeDataModel.TimeModel timeModel;
    private String d;
    private SpinnerAreaAdapter spinnerAreaAdapter;
    private List<AreaModel> areaModelList;
    private int first;

    private List<ServiceDataModel.Level3> additional_service;
    private ItemToUpload itemToUpload;
    private int service_id;
    private String service_name_ar, service_name_en;
    private UserModel userModel;
    private Preferences preferences;
    private List<ItemToUpload.SubServiceModel> subServiceModelList;
    private int count = 1;
    private int size_id = 0;
    private double total = 0.0, final_total = 0.0;
    private TimeAdapter timeAdapter;
    private List<TimeDataModel.TimeModel> timeModelList;
    private String selected_date;
    private Order_Data_Model.OrderModel orderModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_details);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            serviceModel = (ServiceDataModel.Level2) intent.getSerializableExtra("data");
            service_id = intent.getIntExtra("service_id", 0);
            service_name_ar = intent.getStringExtra("service_name_ar");
            service_name_en = intent.getStringExtra("service_name_en");
            if (intent.getSerializableExtra("order") != null) {
                orderModel = (Order_Data_Model.OrderModel) intent.getSerializableExtra("order");

            }

        }
    }


    private void initView() {
        binding.consTime.setEnabled(false);

        timeModelList = new ArrayList<>();
        timeAdapter = new TimeAdapter(timeModelList, this);
        carBrandModelList = new ArrayList<>();
         carBrandModelList.add(new CarTypeDataModel.CarBrandModel("ماركة السيارة", "Car brand"));


        subServiceModelList = new ArrayList<>();
        areaModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        binding.setPrice(0.0);
        binding.setTotal(total);
        userModel = preferences.getUserData(this);
        itemToUpload = new ItemToUpload();
        itemToUpload.setSub_services(subServiceModelList);
        itemToUpload.setService_id(service_id);
        itemToUpload.setSub_serv_id(serviceModel.getId());
        itemToUpload.setAr_service_type(serviceModel.getAr_title());
        itemToUpload.setEn_service_type(serviceModel.getEn_title());

        // itemToUpload.setLevel2(serviceModel.getLevel2());
        binding.setItemModel(itemToUpload);

        //   itemToUpload.setSub_serv_id(serviceModel.getId());
        additional_service = new ArrayList<>();
        carSizeModelList = new ArrayList<>();
        carTypeModelList = new ArrayList<>();
        carTypeModelList.add(new CarTypeDataModel.CarTypeModel("نوع السيارة", "Car type"));
        binding.tvDetails.setPaintFlags(binding.tvDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        carBrandAdapter = new CarBrandAdapter(this, carBrandModelList);
        binding.spinnerBrand.setAdapter(carBrandAdapter);
        spinnerAreaAdapter = new SpinnerAreaAdapter(this, areaModelList);
        binding.spinnerArea.setAdapter(spinnerAreaAdapter);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
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


        manager1 = new GridLayoutManager(this, 2);
        carSizeAdapter = new CarSizeAdapter(carSizeModelList, this);
        binding.recView.setLayoutManager(manager1);
        binding.recView.setAdapter(carSizeAdapter);
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
                    carBrandModelList.add(new CarTypeDataModel.CarBrandModel("ماركة السيارة", "Car brand"));
                    carBrandAdapter.notifyDataSetChanged();

                    if (additionalServiceAdapter != null) {
                        additionalServiceAdapter.clearSelection();
                    }

                    if (carSizeAdapter != null) {
                        carSizeAdapter.setSelection(-1);
                    }

                    additional_service.clear();


                } else {
                    additional_service.clear();
                    carBrandModelList.clear();
                    carBrandModelList.add(new CarTypeDataModel.CarBrandModel("ماركة السيارة", "Car brand"));

                    itemToUpload.setCarType_id(carTypeModelList.get(i).getId());
                    itemToUpload.setAr_car_type(carTypeModelList.get(i).getAr_title());
                    itemToUpload.setEn_car_type(carTypeModelList.get(i).getEn_title());

                    binding.setItemModel(itemToUpload);

                    carBrandModelList.addAll(carTypeModelList.get(i).getLevel2());
                    carBrandAdapter.notifyDataSetChanged();
                    binding.spinnerBrand.setSelection(0);
                    if (orderModel != null && first == 1) {
                        updatebrand();
                    }
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
                    if (carSizeAdapter != null) {
                        carSizeAdapter.setSelection(-1);
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
                        if (carSizeAdapter != null) {
                            carSizeAdapter.setSelection(-1);
                        }
                        size_id = 0;
                        itemToUpload.setCarSize_id(0);

                    } else {
                        if (carSizeAdapter != null) {
                            carSizeAdapter.setSelection(pos);
                        }

                        additional_service.clear();

                        size_id = carSizeModelList.get(pos).getId();

                        getPrice(serviceModel.getId(), carSizeModelList.get(pos).getId());
                        itemToUpload.setCarSize_id(carSizeModelList.get(pos).getId());

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        manager2 = new LinearLayoutManager(this);
        binding.recViewService.setLayoutManager(manager2);

        if (serviceModel.getLevel3().size() > 0) {
            binding.llAdditional.setVisibility(View.VISIBLE);
            additionalServiceAdapter = new AdditionalServiceAdapter(serviceModel.getLevel3(), this);
            binding.recViewService.setAdapter(additionalServiceAdapter);

        } else {
            binding.llAdditional.setVisibility(View.GONE);
        }


        binding.consMap.setOnClickListener(view -> {
            Intent intent = new Intent(ServiceDetailsActivity.this, MapActivity.class);
            startActivityForResult(intent, 1);
        });
        binding.closeCalender.setOnClickListener(view -> binding.flCalender.setVisibility(View.GONE));
        binding.closeTime.setOnClickListener(view -> binding.flTime.setVisibility(View.GONE));

        binding.consDate.setOnClickListener(view -> openCalender());
        binding.recViewTime.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recViewTime.setAdapter(timeAdapter);
        binding.consTime.setOnClickListener(view -> {
            binding.flTime.setVisibility(View.VISIBLE);

        });
        binding.tvDone.setOnClickListener(view -> {
            binding.flTime.setVisibility(View.GONE);
            binding.tvTime.setText(timeModel.getTime_text());
            itemToUpload.setTime(timeModel.getTime_text());
            itemToUpload.setTime_type(timeModel.getType());
            itemToUpload.setOrder_time_id(timeModel.getId());

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
                    itemToUpload.setCar_blade_number(itemToUpload.getVehicleNumber() + itemToUpload.getVehicleChar());

                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("item", itemToUpload);
                    if(orderModel!=null){
                        intent.putExtra("order",orderModel);
                    }
                    startActivityForResult(intent, 4);
                } else {
                    Common.CreateNoSignAlertDialog(this);
                }


            }
            else{
                Toast.makeText(this,getResources().getString(R.string.complete_info),Toast.LENGTH_LONG).show();

            }
        });

        binding.imageIncrease.setOnClickListener(view -> {
            double service_price = itemToUpload.getService_price() / count;

            count++;
            itemToUpload.setAmount(count);
            binding.tvCount.setText(String.valueOf(count));
            final_total = total * count;

            binding.setTotal(final_total);
            // itemToUpload.setTotal_price(final_total+);
            itemToUpload.setService_price(service_price * count);

        });

        binding.imageDecrease.setOnClickListener(view -> {
            if (count > 1) {
                double service_price = itemToUpload.getService_price() / count;
                count--;
                itemToUpload.setAmount(count);

                final_total = total * count;
                binding.setTotal(final_total);
                // itemToUpload.setService_price(final_total);
                itemToUpload.setService_price(service_price * count);

                // binding.setTotal(total);
                binding.tvCount.setText(String.valueOf(count));
            }

        });
        binding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {


                } else {

                    itemToUpload.setPlace_id(areaModelList.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SvgLoader.pluck()
                .with(this)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(Tags.IMAGE_URL+serviceModel.getImage(),binding.image);

        getCarSize();
        getCarType();
        getArea();

    }

    private void updatesubService() {
        first=0;
      //  Log.e("suuuu",serviceModel.getLevel3().size()+" "+orderModel.getOrder_sub_services().size());
      if(orderModel.getOrder_sub_services()!=null){
       for (int i = 0; i < serviceModel.getLevel3().size(); i++) {

            for (int j = 0; j < orderModel.getOrder_sub_services().size(); j++) {
                Log.e("kdkdkkd", serviceModel.getLevel3().get(i).getId() + " " + orderModel.getOrder_sub_services().get(j).getSub_service_id());

                if (serviceModel.getLevel3().get(i).getId()==orderModel.getOrder_sub_services().get(j).getSub_service_id()) {

                    ServiceDataModel.Level3 serLevel2 = serviceModel.getLevel3().get(i);
                    serLevel2.setSelected(true);
                    serviceModel.getLevel3().set(i, serLevel2);
                    setItemAdditionService(serLevel2);
                }
            }
        }}
        additionalServiceAdapter.updatelist(serviceModel.getLevel3());
        additionalServiceAdapter.notifyDataSetChanged();
    }

    private void updatebrand() {
        for (int i = 0; i < carBrandModelList.size(); i++) {
            Log.e("datab", carBrandModelList.get(i).getId() + " " + orderModel.getBrand_id());
            if (carBrandModelList.get(i).getId() == orderModel.getBrand_id()) {
                binding.spinnerBrand.setSelection(i);
                break;

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateDataOrder() {
        if (selectedLocation == null) {
            selectedLocation = new SelectedLocation(orderModel.getLatitude(), orderModel.getLongitude(), orderModel.getAddress());
            binding.setLocation(selectedLocation);
        }
        itemToUpload.setUser_phone(orderModel.getUser_phone());
        itemToUpload.setAmount(Integer.parseInt(orderModel.getNumber_of_cars()));
        if (orderModel.getPlace_id() != null) {
            itemToUpload.setPlace_id(Integer.parseInt(orderModel.getPlace_id()));
        }
        if (orderModel.getCar_blade_number() != null) {
            // Log.e(";llll",orderModel.getcar_blade_number());
            try {
                itemToUpload.setVehicleChar(orderModel.getCar_blade_number().substring(0, 3));
                itemToUpload.setVehicleNumber(orderModel.getCar_blade_number().substring(3, 7));
            } catch (Exception e) {

            }

        }

        itemToUpload.setAddress(orderModel.getAddress());
        itemToUpload.setLatitude(orderModel.getLatitude() + "");
        itemToUpload.setLongitude(orderModel.getLongitude() + "");
        itemToUpload.setCarSize_id(orderModel.getSize_id());
        itemToUpload.setBrand_id(orderModel.getBrand_id());

        itemToUpload.setTime(orderModel.getOrder_time());
        binding.tvTime.setText(orderModel.getOrder_time());
        // itemToUpload.setTime_type(orderModel.getType());
        itemToUpload.setOrder_time_id(orderModel.getOrder_time_id());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        selected_date = dateFormat.format(new Date(orderModel.getOrder_date() * 1000));
        binding.flCalender.setVisibility(View.GONE);
        binding.tvDate.setText(selected_date);
        itemToUpload.setOrder_date(selected_date);
        binding.consTime.setEnabled(true);
        binding.setItemModel(itemToUpload);
        for (int i = 0; i < carTypeModelList.size(); i++) {
            Log.e("data", carTypeModelList.get(i).getId() + " " + orderModel.getType_id());
            if (carTypeModelList.get(i).getId() == orderModel.getType_id()) {

                binding.spinner.setSelection(i);
                first = 1;
                break;

            }
        }

        getTime();
        // itemToUpload.setAr_service_type(orderModel.getar);

//    }
    }

    public void setTimeItem(TimeDataModel.TimeModel model) {
        //timeAdapter.updateList(model);
        binding.tvDone.setVisibility(View.VISIBLE);
        this.timeModel = model;
    }

    private void getArea() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        /// Log.e("data", "ddd");
        Api.getService(Tags.base_url)
                .getArea()
                .enqueue(new Callback<AreaDataModel>() {
                    @Override
                    public void onResponse(Call<AreaDataModel> call, Response<AreaDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            areaModelList.clear();
                            areaModelList.add(new AreaModel("الحى", " Area"));
                            areaModelList.addAll(response.body().getData());
                            spinnerAreaAdapter.notifyDataSetChanged();
                            if (orderModel != null) {
                                updateArea();
                            }

                        } else {

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
                    public void onFailure(Call<AreaDataModel> call, Throwable t) {
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

    private void updateArea() {
        for (int i = 0; i < areaModelList.size(); i++) {
            if (orderModel.getPlace_id() != null && orderModel.getPlace_id().equals(areaModelList.get(i).getId() + "")) {
                binding.spinnerArea.setSelection(i);
                break;

            }
        }
    }

    private void openCalender() {
        binding.flCalender.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        binding.calendar.setMinDate(calendar.getTimeInMillis());
        binding.calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            Log.e("date", i + "/" + (i1 + 1) + "/" + i2);
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            selected_date = dateFormat.format(new Date(calendar.getTimeInMillis()));
            binding.flCalender.setVisibility(View.GONE);
            binding.tvDate.setText(selected_date);
            itemToUpload.setOrder_date(selected_date);
            binding.consTime.setEnabled(true);
            getTime();
        });
    }

    private void getPrice(int service_id, int size_id) {


        total = 0.0;
        binding.setTotal(total);

        ProgressDialog dialog = Common.createProgressDialog(ServiceDetailsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getPrice(service_id, size_id)
                .enqueue(new Callback<Double>() {
                    @Override
                    public void onResponse(Call<Double> call, Response<Double> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            binding.setPrice(response.body());
                            total = total + response.body();
                            final_total = total * count;
                            itemToUpload.setService_price(response.body() * count);
                            binding.setTotal(final_total);
                            if (first == 1 && orderModel != null) {
                                updatesubService();
                            }
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(ServiceDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(ServiceDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void getPriceForAdditionalService(ServiceDataModel.Level3 level3, int size_id) {

        ProgressDialog dialog = Common.createProgressDialog(ServiceDetailsActivity.this, getString(R.string.wait));
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

                            level3.setPrice(response.body() + "");
                            additional_service.add(level3);

                            subServiceModelList.clear();

                            for (ServiceDataModel.Level3 level3 : additional_service) {
                                ItemToUpload.SubServiceModel subServiceModel = new ItemToUpload.SubServiceModel(level3.getId(), Double.parseDouble(level3.getPrice()), level3.getAr_title(), level3.getEn_title());
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
                                Toast.makeText(ServiceDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(ServiceDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void getCarSize() {

        ProgressDialog dialog = Common.createProgressDialog(ServiceDetailsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getCarSizes()
                .enqueue(new Callback<CarSizeDataModel>() {
                    @Override
                    public void onResponse(Call<CarSizeDataModel> call, Response<CarSizeDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            carSizeModelList.clear();
                            carSizeModelList.addAll(response.body().getData());
                            carSizeAdapter.notifyDataSetChanged();

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(ServiceDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CarSizeDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ServiceDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void getCarType() {
        ProgressDialog dialog = Common.createProgressDialog(ServiceDetailsActivity.this, getString(R.string.wait));
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
                            if (orderModel != null) {
                                updateDataOrder();
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(ServiceDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                    Toast.makeText(ServiceDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                binding.tvDate.setText(d);
                itemToUpload.setOrder_date(d);
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
       if(additional_service.size()>0){
        additional_service.remove(getItemPos(m_level3));}
        Log.e("vvvvvvv", m_level3.getPrice() + "__");

        total = total - Double.parseDouble(m_level3.getPrice());

        final_total = total * count;

        Log.e("bbb", final_total + "__");

        binding.setTotal(final_total);

        List<ItemToUpload.SubServiceModel> subServiceModelList = new ArrayList<>();
        for (ServiceDataModel.Level3 level3 : additional_service) {
            ItemToUpload.SubServiceModel subServiceModel = new ItemToUpload.SubServiceModel(level3.getId(), Double.parseDouble(level3.getPrice()), level3.getAr_title(), level3.getEn_title());
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
        for (int pos = 0; pos < carSizeModelList.size(); pos++) {
            if (Integer.parseInt(id) == carSizeModelList.get(pos).getId()) {
                return pos;
            }
        }
        return position;
    }

    private void getTime() {
        Api.getService(Tags.base_url)
                .getTime(selected_date)
                .enqueue(new Callback<TimeDataModel>() {
                    @Override
                    public void onResponse(Call<TimeDataModel> call, Response<TimeDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            timeModelList.clear();
                            timeModelList.addAll(response.body().getData());
                            timeAdapter.updateList(timeModelList);
                            if (orderModel != null) {
                                updatetime();
                            }
                            // updateUI(response.body().getData());

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 404) {
                            } else if (response.code() == 500) {
                                Toast.makeText(ServiceDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TimeDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ServiceDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updatetime() {
        for (int i = 0; i < timeModelList.size(); i++) {
            if (orderModel.getOrder_time_id() == timeModelList.get(i).getId()) {
                timeModel = timeModelList.get(i);
                binding.tvTime.setText(timeModel.getTime_text());
                itemToUpload.setTime(timeModel.getTime_text());
                itemToUpload.setTime_type(timeModel.getType());
                itemToUpload.setOrder_time_id(timeModel.getId());
                break;
            }
        }
    }

}

