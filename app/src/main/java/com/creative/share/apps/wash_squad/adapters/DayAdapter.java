package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.databinding.DayRowBinding;
import com.creative.share.apps.wash_squad.models.DayModel;


import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyHolder>{
    private List<DayModel> dayList;
    private Context context;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;

    public DayAdapter(List<DayModel> dayList, Context context) {
        this.dayList = dayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DayRowBinding dayRowBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.day_row,parent,false);
        return new MyHolder(dayRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.dayRowBinding.setDay(dayList.get(position));
        if (oldHolder==null){
            oldHolder=myHolder;
        }
        myHolder.itemView.setOnClickListener(view -> {
            if (oldHolder!=null){
                DayModel oldDay=dayList.get(oldPos);
                oldDay.setSelected(false);
                dayList.set(oldPos,oldDay);

                MyHolder oHolder=(MyHolder) oldHolder;
                oHolder.dayRowBinding.setDay(oldDay);
            }
            currentPos=myHolder.getAdapterPosition();
            DayModel model=dayList.get(currentPos);
            model.setSelected(true);
            dayList.set(currentPos,model);
            myHolder.dayRowBinding.setDay(model);

            oldHolder=myHolder;
            oldPos=currentPos;
            if (context instanceof SubscriptionServiceActivity){
                SubscriptionServiceActivity activity=(SubscriptionServiceActivity) context;
                activity.setDayItem(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private DayRowBinding dayRowBinding;

        public MyHolder(@NonNull DayRowBinding dayRowBinding) {
            super(dayRowBinding.getRoot());
            this.dayRowBinding = dayRowBinding;
        }
    }
    public void updateList(List<DayModel> list) {
        if (list != null) {
            this.dayList = list;
        }
        notifyDataSetChanged();
    }
}
