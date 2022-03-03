package com.creative.share.apps.wash_squad.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.CartTypeRowBinding;
import com.creative.share.apps.wash_squad.models.CarTypeDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CarTypeAdapter extends BaseAdapter {
    private Context context;
    private List<CarTypeDataModel.CarTypeModel> carTypeModelList;
    private String lang;

    public CarTypeAdapter(Context context, List<CarTypeDataModel.CarTypeModel> carTypeModelList) {
        this.context = context;
        this.carTypeModelList = carTypeModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @Override
    public int getCount() {
        return carTypeModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return carTypeModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") CartTypeRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_type_row,viewGroup,false);
        CarTypeDataModel.CarTypeModel carTypeModel = carTypeModelList.get(i);
        binding.setLang(lang);
        binding.setCarTypeModel(carTypeModel);

        return binding.getRoot();
    }
}
