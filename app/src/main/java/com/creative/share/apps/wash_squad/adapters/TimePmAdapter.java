package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_time.TimeActivity;
import com.creative.share.apps.wash_squad.databinding.TimeRowBinding;
import com.creative.share.apps.wash_squad.models.TimeDataModel;

import java.util.List;

public class TimePmAdapter extends RecyclerView.Adapter<TimePmAdapter.MyHolder> {

    private List<TimeDataModel.TimeModel> timeModelList;
    private Context context;
    private int selected_pos = -1;
    private TimeActivity activity;

    public TimePmAdapter(List<TimeDataModel.TimeModel> timeModelList, Context context) {
        this.timeModelList = timeModelList;
        this.context = context;
        activity = (TimeActivity) context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TimeRowBinding timeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.time_row,parent,false);
        return new MyHolder(timeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        TimeDataModel.TimeModel timeModel = timeModelList.get(position);
        holder.timeRowBinding.setTime(timeModel);
        if (selected_pos==position)
        {
            holder.timeRowBinding.rb.setButtonDrawable(R.drawable.rb_bg2);
            holder.timeRowBinding.rb.setChecked(true);
            holder.timeRowBinding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));


        }else
        {

            if (timeModel.getStatus_en().equals("active"))
            {
                holder.timeRowBinding.rb.setChecked(false);
                holder.timeRowBinding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.gray5));
                holder.timeRowBinding.rb.setButtonDrawable(R.drawable.rb_bg2);


            }else
            {
                holder.timeRowBinding.rb.setChecked(false);
                holder.timeRowBinding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.gray3));
                holder.timeRowBinding.rb.setButtonDrawable(R.drawable.rb_bg3);
            }



        }


        holder.itemView.setOnClickListener(view -> {

            TimeDataModel.TimeModel timeModel2 = timeModelList.get(holder.getAdapterPosition());
            if (timeModel2.getStatus_en().equals("active"))
            {
                activity.setItemPm(timeModel2);
                selected_pos = holder.getAdapterPosition();
                notifyDataSetChanged();
            }


        });




    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    public void clearSelection()
    {
        selected_pos = -1;
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TimeRowBinding timeRowBinding;
        public MyHolder(@NonNull TimeRowBinding timeRowBinding) {
            super(timeRowBinding.getRoot());
            this.timeRowBinding = timeRowBinding;
        }
    }

}
