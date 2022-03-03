package com.creative.share.apps.wash_squad.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.CartBrandRowBinding;
import com.creative.share.apps.wash_squad.models.CarTypeDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CarBrandAdapter extends BaseAdapter {
    private Context context;
    private List<CarTypeDataModel.CarBrandModel> carBrandModelList;
    private String lang;

    public CarBrandAdapter(Context context, List<CarTypeDataModel.CarBrandModel> carBrandModelList) {
        this.context = context;
        this.carBrandModelList = carBrandModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @Override
    public int getCount() {
        return carBrandModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return carBrandModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") CartBrandRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_brand_row,viewGroup,false);
        CarTypeDataModel.CarBrandModel carBrandModel = carBrandModelList.get(i);
        binding.setLang(lang);
        binding.setCarBrandModel(carBrandModel);

        return binding.getRoot();
    }
}
