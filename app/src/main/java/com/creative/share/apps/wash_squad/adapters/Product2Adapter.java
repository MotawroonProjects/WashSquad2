package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.CategoryRowBinding;
import com.creative.share.apps.wash_squad.databinding.Product2RowBinding;
import com.creative.share.apps.wash_squad.models.ProductModel;

import java.util.List;

public class Product2Adapter extends RecyclerView.Adapter<Product2Adapter.MyHolder> {

    private List<ProductModel> list;
    private Context context;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;
    private String lang;
    private Fragment fragment;

    public Product2Adapter(List<ProductModel> list, Context context, Fragment fragment, String lang) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        this.lang = lang;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Product2RowBinding timeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product2_row, parent, false);
        return new MyHolder(timeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.timeRowBinding.tvOldPrice.setPaintFlags(holder.timeRowBinding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        MyHolder myHolder = (MyHolder) holder;
        myHolder.timeRowBinding.setModel(list.get(position));
        myHolder.timeRowBinding.setLang(lang);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (oldHolder != null) {
//                    if(oldPos!=-1){
//                      }
//                }
//                currentPos = holder.getAdapterPosition();
//              notifyDataSetChanged();
//
//                oldHolder = holder;
//                oldPos = currentPos;
//
//
//            }
//        });


//if(currentPos==position){
//    holder.timeRowBinding.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
//    holder.timeRowBinding.tvTitle.setTextColor(context.getResources().getColor(R.color.white));
//}
//else{
//    holder.timeRowBinding.card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
//    holder.timeRowBinding.tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
//
//}


    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void clearSelection() {
        //selected_pos = -1;
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private Product2RowBinding timeRowBinding;

        public MyHolder(@NonNull Product2RowBinding timeRowBinding) {
            super(timeRowBinding.getRoot());
            this.timeRowBinding = timeRowBinding;
        }
    }

}
