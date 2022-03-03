package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.wash_squad.databinding.MainServiceRowBinding;
import com.creative.share.apps.wash_squad.databinding.ProductRowBinding;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;
import com.creative.share.apps.wash_squad.models.ServiceDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ServiceHolder> {

    private List<Order_Data_Model.Products> list;
    private Context context;
    private String lang;

    public ProductAdapter(List<Order_Data_Model.Products> list, Context context) {
        this.list = list;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowBinding serviceRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.product_row,parent,false);
        return new ServiceHolder(serviceRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {

        Order_Data_Model.Products products = list.get(position);
        holder.binding.setLang(lang);
        holder.binding.setModel(products);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {
        private ProductRowBinding binding;
        public ServiceHolder(@NonNull ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
