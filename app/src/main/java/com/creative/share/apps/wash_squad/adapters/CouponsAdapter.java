package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.CouponRowBinding;
import com.creative.share.apps.wash_squad.databinding.OfferRowBinding;
import com.creative.share.apps.wash_squad.models.CouponDataModel;
import com.creative.share.apps.wash_squad.models.OfferDataModel;

import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.MyHolder> {

    private List<CouponDataModel.CouponModel> list;
    private Context context;

    public CouponsAdapter(List<CouponDataModel.CouponModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CouponRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.coupon_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        CouponDataModel.CouponModel model = list.get(position);
        holder.binding.setModel(model);





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CouponRowBinding binding;
        public MyHolder(@NonNull CouponRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
