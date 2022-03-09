package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.AdditionalRowBinding;
import com.creative.share.apps.wash_squad.databinding.WashRowBinding;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.SubscribtionDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class WashAdapter extends RecyclerView.Adapter<WashAdapter.MyHolder> {

    private List<SubscribtionDataModel.WashSub> subServiceModelList;
    private Context context;
    private String lang;

    public WashAdapter(List<SubscribtionDataModel.WashSub> subServiceModelList, Context context) {
        this.subServiceModelList = subServiceModelList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WashRowBinding additionalRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.wash_row, parent, false);
        return new MyHolder(additionalRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.additionalRowBinding.setLang(lang);
        holder.additionalRowBinding.setModel(subServiceModelList.get(position));
        if (position % 2 == 0) {
            holder.additionalRowBinding.ll.setBackgroundColor(context.getResources().getColor(R.color.gray2));
        }


    }

    @Override
    public int getItemCount() {
        return subServiceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private WashRowBinding additionalRowBinding;

        public MyHolder(@NonNull WashRowBinding additionalRowBinding) {
            super(additionalRowBinding.getRoot());
            this.additionalRowBinding = additionalRowBinding;
        }
    }

}
