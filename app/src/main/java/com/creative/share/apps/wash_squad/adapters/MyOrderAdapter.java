package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_order.Fragment_Current_Order;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_order.Fragment_Previous_Order;
import com.creative.share.apps.wash_squad.databinding.LoadMoreBinding;
import com.creative.share.apps.wash_squad.databinding.OrderRowBinding;
import com.creative.share.apps.wash_squad.models.Order_Data_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<Order_Data_Model.OrderModel> orderModelList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private HomeActivity activity;
    private Fragment fragment;

    public MyOrderAdapter(List<Order_Data_Model.OrderModel> orderModelList, Context context, Fragment fragment) {
        this.orderModelList = orderModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.activity = (HomeActivity) context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more, parent, false);
            return new LoadHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order_Data_Model.OrderModel order_orderModel = orderModelList.get(position);
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setLang(lang);
            myHolder.binding.setModel(order_orderModel);

            if (fragment instanceof Fragment_Current_Order)
            {


                myHolder.binding.btnDetials.setOnClickListener(v -> {
                    Fragment_Current_Order fragment_current_order = (Fragment_Current_Order) fragment;
                    fragment_current_order.setItemData(orderModelList.get(holder.getAdapterPosition()));
                });

            }else if (fragment instanceof Fragment_Previous_Order)
            {


                myHolder.binding.btnDetials.setOnClickListener(v -> {
                    Fragment_Previous_Order fragment_previous_order = (Fragment_Previous_Order) fragment;
                    fragment_previous_order.setItemData(orderModelList.get(holder.getAdapterPosition()));
                });

                /*myHolder.itemView.setOnClickListener(v -> {

                });

                myHolder.binding.btnResend.setOnClickListener(v -> {

                });*/
            }
///*
//        ((MyHolder) holder).binding.btnDetials.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notifyDataSetChanged();
//                if (orderModelList.get(holder.getLayoutPosition()).getRating() > 0&& orderModelList.get(holder.getLayoutPosition()).getStatus()!=3) {
//                    activity.DisplayFragmentOrderDetials(orderModelList.get(eventHolder.getLayoutPosition()));
//                } else {
//                    activity.DisplayFragmentOrderDetialsEvaluation(orderModelList.get(eventHolder.getLayoutPosition()));
//                }
//            }
//        });



        } else {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public MyHolder(OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {

        private LoadMoreBinding binding;

        public LoadHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Order_Data_Model.OrderModel order_Model = orderModelList.get(position);
        if (order_Model != null) {
            return ITEM_DATA;
        } else {
            return LOAD;
        }

    }


}
