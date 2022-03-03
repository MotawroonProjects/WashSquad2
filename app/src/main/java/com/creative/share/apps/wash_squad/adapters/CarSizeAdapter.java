package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.databinding.SizeRowBinding;
import com.creative.share.apps.wash_squad.models.CarSizeDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CarSizeAdapter extends RecyclerView.Adapter<CarSizeAdapter.MyHolder> {

    private List<CarSizeDataModel.CarSizeModel> carSizeModelList;
    private Context context;
    private String lang;
    private ServiceDetailsActivity activity;
    private int selected_pos = -1;

    public CarSizeAdapter(List<CarSizeDataModel.CarSizeModel> carSizeModelList, Context context) {
        this.carSizeModelList = carSizeModelList;
        this.context = context;
        activity = (ServiceDetailsActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SizeRowBinding sizeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.size_row,parent,false);
        return new MyHolder(sizeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        CarSizeDataModel.CarSizeModel carSizeModel = carSizeModelList.get(position);
        holder.sizeRowBinding.setLang(lang);
        holder.sizeRowBinding.setCarSizeModel(carSizeModel);

        if (selected_pos==position)
        {
            holder.sizeRowBinding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.color_second));
            holder.sizeRowBinding.ll.setBackgroundResource(R.drawable.selected_car_size_bg);
        }else
            {
                holder.sizeRowBinding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.gray5));
                holder.sizeRowBinding.ll.setBackgroundResource(R.drawable.expand_bg);

            }
/*
        holder.itemView.setOnClickListener(view -> {
            CarSizeDataModel.CarSizeModel carSizeModel1 = carSizeModelList.get(holder.getAdapterPosition());
            activity.setItemCarSizeSelected(carSizeModel1);
            selected_pos = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
*/
    }

    @Override
    public int getItemCount() {
        return carSizeModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private SizeRowBinding sizeRowBinding;
        public MyHolder(@NonNull SizeRowBinding sizeRowBinding) {
            super(sizeRowBinding.getRoot());
            this.sizeRowBinding = sizeRowBinding;
        }
    }

    public void setSelection(int selected_pos)
    {
        this.selected_pos = selected_pos;
        notifyDataSetChanged();
    }

}
