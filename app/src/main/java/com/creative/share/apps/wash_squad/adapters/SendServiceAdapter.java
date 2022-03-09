package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_choose_service.ChooseServiceSentActivity;
import com.creative.share.apps.wash_squad.databinding.MainServiceRowBinding;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SendServiceAdapter extends RecyclerView.Adapter<SendServiceAdapter.ServiceHolder>{
    private List<ServiceDataModel.ServiceModel> serviceModelList;
    private Context context;
    private String lang;

    public SendServiceAdapter(List<ServiceDataModel.ServiceModel> serviceModelList,Context context) {
        this.serviceModelList = serviceModelList;
        this.context=context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainServiceRowBinding serviceRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.main_service_row,parent,false);
        return new ServiceHolder(serviceRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        ServiceDataModel.ServiceModel serviceModel = serviceModelList.get(position);
        holder.serviceRowBinding.setLang(lang);
        holder.serviceRowBinding.setServiceModel(serviceModel);
        holder.itemView.setOnClickListener(view -> {
            ServiceDataModel.ServiceModel serviceModel1 = serviceModelList.get(holder.getAdapterPosition());
            ChooseServiceSentActivity activity=(ChooseServiceSentActivity) context;
            activity.setItemData(serviceModel1);

        });
    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }


    public class ServiceHolder extends RecyclerView.ViewHolder {
        private MainServiceRowBinding serviceRowBinding;
        public ServiceHolder(@NonNull MainServiceRowBinding serviceRowBinding) {
            super(serviceRowBinding.getRoot());
            this.serviceRowBinding = serviceRowBinding;
        }
    }
}
