package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.AdditionalRowBinding;
import com.creative.share.apps.wash_squad.models.ItemToUpload;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AdditionalAdapter extends RecyclerView.Adapter<AdditionalAdapter.MyHolder> {

    private List<ItemToUpload.SubServiceModel> subServiceModelList;
    private Context context;
    private String lang;

    public AdditionalAdapter(List<ItemToUpload.SubServiceModel> subServiceModelList, Context context) {
        this.subServiceModelList = subServiceModelList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdditionalRowBinding additionalRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.additional_row, parent, false);
        return new MyHolder(additionalRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        ItemToUpload.SubServiceModel subServiceModel = subServiceModelList.get(position);
        holder.additionalRowBinding.setLang(lang);
        holder.additionalRowBinding.setAdditionalModel(subServiceModel);





    }

    @Override
    public int getItemCount() {
        return subServiceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private AdditionalRowBinding additionalRowBinding;

        public MyHolder(@NonNull AdditionalRowBinding additionalRowBinding) {
            super(additionalRowBinding.getRoot());
            this.additionalRowBinding = additionalRowBinding;
        }
    }

}
