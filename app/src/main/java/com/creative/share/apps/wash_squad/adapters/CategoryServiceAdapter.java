package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_category.ServiceCategoryActivity;
import com.creative.share.apps.wash_squad.databinding.CategoryServiceRowBinding;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;
import com.creative.share.apps.wash_squad.tags.Tags;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CategoryServiceAdapter extends RecyclerView.Adapter<CategoryServiceAdapter.CategoryServiceHolder> {

    private List<ServiceDataModel.Level2> serviceModelList;
    private Context context;
    private String lang;
    private ServiceCategoryActivity activity;

    public CategoryServiceAdapter(List<ServiceDataModel.Level2> serviceModelList, Context context) {
        this.serviceModelList = serviceModelList;
        this.context = context;
        activity = (ServiceCategoryActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public CategoryServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryServiceRowBinding categoryServiceRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.category_service_row,parent,false);
        return new CategoryServiceHolder(categoryServiceRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryServiceHolder holder, int position) {

        ServiceDataModel.Level2 serviceModel = serviceModelList.get(position);
        holder.categoryServiceRowBinding.setLang(lang);
        holder.categoryServiceRowBinding.setCategoryServiceModel(serviceModel);
        holder.itemView.setOnClickListener(view -> {
            ServiceDataModel.Level2 serviceModel1 = serviceModelList.get(holder.getAdapterPosition());
            activity.setItemData(serviceModel1);
        });
        SvgLoader.pluck()
                .with(activity)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(Tags.IMAGE_URL+serviceModelList.get(position).getImage(),holder.categoryServiceRowBinding.image);

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public class CategoryServiceHolder extends RecyclerView.ViewHolder {
        private CategoryServiceRowBinding categoryServiceRowBinding;
        public CategoryServiceHolder(@NonNull CategoryServiceRowBinding categoryServiceRowBinding) {
            super(categoryServiceRowBinding.getRoot());
            this.categoryServiceRowBinding = categoryServiceRowBinding;
        }
    }

}
