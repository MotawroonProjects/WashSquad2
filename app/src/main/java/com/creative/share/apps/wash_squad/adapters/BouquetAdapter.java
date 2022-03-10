package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.databinding.BouquetRowBinding;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class BouquetAdapter extends RecyclerView.Adapter<BouquetAdapter.MyHolder>{
    private List<ServiceDataModel.Level2> level2List;
    private Context context;
    private String lang;
    private SubscriptionServiceActivity activity;
    private int selected_pos = -1;

    public BouquetAdapter(List<ServiceDataModel.Level2> level2List, Context context) {
        this.level2List = level2List;
        this.context = context;
        activity=(SubscriptionServiceActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BouquetRowBinding bouquetRowBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bouquet_row,parent,false);
        return new MyHolder(bouquetRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ServiceDataModel.Level2 level2=level2List.get(position);
        holder.bouquetRowBinding.setLang(lang);
        holder.bouquetRowBinding.setLevel2(level2);
    }

    @Override
    public int getItemCount() {
        return level2List.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private BouquetRowBinding bouquetRowBinding;
        public MyHolder(@NonNull BouquetRowBinding bouquetRowBinding) {
            super(bouquetRowBinding.getRoot());
            this.bouquetRowBinding = bouquetRowBinding;
        }
    }
}
