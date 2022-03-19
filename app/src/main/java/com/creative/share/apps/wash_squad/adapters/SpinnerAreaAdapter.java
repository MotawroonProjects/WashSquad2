package com.creative.share.apps.wash_squad.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.AreaRowBinding;
import com.creative.share.apps.wash_squad.databinding.CartTypeRowBinding;
import com.creative.share.apps.wash_squad.models.AreaModel;
import com.creative.share.apps.wash_squad.models.CarTypeDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SpinnerAreaAdapter extends BaseAdapter {
    private Context context;
    private List<AreaModel> areaModelList;
    private String lang;

    public SpinnerAreaAdapter(Context context, List<AreaModel> areaModelList) {
        this.context = context;
        this.areaModelList = areaModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @Override
    public int getCount() {
        return areaModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return areaModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") AreaRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.area_row,viewGroup,false);
        AreaModel carTypeModel = areaModelList.get(i);
        binding.setLang(lang);
        binding.setAreaModel(carTypeModel);

        return binding.getRoot();
    }
}
