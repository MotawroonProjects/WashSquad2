package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.wash_squad.databinding.MainServiceRowBinding;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MainServiceAdapter extends RecyclerView.Adapter<MainServiceAdapter.ServiceHolder> {

    private List<ServiceDataModel.ServiceModel> serviceModelList;
    private Context context;
    private Fragment_Main fragment;
    private String lang;

    public MainServiceAdapter(List<ServiceDataModel.ServiceModel> serviceModelList, Context context, Fragment_Main fragment) {
        this.serviceModelList = serviceModelList;
        this.context = context;
        this.fragment = fragment;
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

            if (holder.getAdapterPosition()==2){
                fragment.setItemData3(serviceModel1);
            }else if (holder.getAdapterPosition()==4){
                fragment.setItemData2(serviceModel1);
            }else {
                fragment.setItemData(serviceModel1);
            }



        });
        Log.e("path",Tags.IMAGE_URL+serviceModelList.get(position).getImage());

        SvgLoader.pluck()
                .with(fragment.getActivity())
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(Tags.IMAGE_URL+serviceModelList.get(position).getImage(),holder.serviceRowBinding.imgService);

        SvgLoader.pluck().close();
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
