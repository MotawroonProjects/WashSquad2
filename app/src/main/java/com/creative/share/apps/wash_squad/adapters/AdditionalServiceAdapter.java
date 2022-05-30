package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_sent_details.ServiceSentDetailsActivity;
import com.creative.share.apps.wash_squad.databinding.AdditionalServiceRowBinding;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AdditionalServiceAdapter extends RecyclerView.Adapter<AdditionalServiceAdapter.MyHolder> {

    private List<ServiceDataModel.Level3> serviceModelList;
    private Context context;
    private String lang;
    private SparseBooleanArray sparseBooleanArray;

    public AdditionalServiceAdapter(List<ServiceDataModel.Level3> serviceModelList, Context context) {
        this.serviceModelList = serviceModelList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        sparseBooleanArray = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdditionalServiceRowBinding additionalServiceRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.additional_service_row, parent, false);
        return new MyHolder(additionalServiceRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        ServiceDataModel.Level3 serviceModel = serviceModelList.get(position);
        holder.additionalServiceRowBinding.setLang(lang);
        holder.additionalServiceRowBinding.setLevel3(serviceModel);
        if (serviceModel.isSelected()) {
            sparseBooleanArray.put(position, true);
        }
        if (sparseBooleanArray.get(position, false)) {
            holder.additionalServiceRowBinding.checkbox.setChecked(true);
        } else {
            holder.additionalServiceRowBinding.checkbox.setChecked(false);

        }

        holder.additionalServiceRowBinding.checkbox.setOnClickListener(view -> {

            if (holder.additionalServiceRowBinding.checkbox.isChecked()) {

                Log.e("ddd", "tttt");
                sparseBooleanArray.put(holder.getAdapterPosition(), true);

                ServiceDataModel.Level3 serviceModel1 = serviceModelList.get(holder.getAdapterPosition());
                if (context instanceof ServiceDetailsActivity) {
                    ServiceDetailsActivity activity = (ServiceDetailsActivity) context;
                    activity.setItemAdditionService(serviceModel1);
                } else if (context instanceof ServiceSentDetailsActivity) {
                    ServiceSentDetailsActivity activity = (ServiceSentDetailsActivity) context;
                    activity.setItemAdditionService(serviceModel1);
                }

                notifyDataSetChanged();


            } else {

                Log.e("bb", "rrr");

                sparseBooleanArray.put(holder.getAdapterPosition(), false);

                ServiceDataModel.Level3 serviceModel1 = serviceModelList.get(holder.getAdapterPosition());
                if (context instanceof ServiceDetailsActivity) {
                    ServiceDetailsActivity activity = (ServiceDetailsActivity) context;
                    activity.removeAdditionalItem(serviceModel1);
                } else if (context instanceof ServiceSentDetailsActivity) {
                    ServiceSentDetailsActivity activity = (ServiceSentDetailsActivity) context;
                    activity.removeAdditionalItem(serviceModel1);
                }
                notifyDataSetChanged();

            }


        });


    }

    public void clearSelection() {
        this.sparseBooleanArray.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public void updatelist(List<ServiceDataModel.Level3> level2) {
        this.serviceModelList = level2;
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private AdditionalServiceRowBinding additionalServiceRowBinding;

        public MyHolder(@NonNull AdditionalServiceRowBinding additionalServiceRowBinding) {
            super(additionalServiceRowBinding.getRoot());
            this.additionalServiceRowBinding = additionalServiceRowBinding;
        }
    }

}
