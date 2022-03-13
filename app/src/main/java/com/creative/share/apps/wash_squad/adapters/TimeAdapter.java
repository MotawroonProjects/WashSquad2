package com.creative.share.apps.wash_squad.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.ServiceDetailsActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_details.subscription_service.SubscriptionServiceActivity;
import com.creative.share.apps.wash_squad.activities_fragments.activity_service_sent_details.ServiceSentDetailsActivity;
import com.creative.share.apps.wash_squad.databinding.TimeRowBinding;
import com.creative.share.apps.wash_squad.models.TimeDataModel;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyHolder> {

    private List<TimeDataModel.TimeModel> timeModelList;
    private Context context;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;

    public TimeAdapter(List<TimeDataModel.TimeModel> timeModelList, Context context) {
        this.timeModelList = timeModelList;
        this.context = context;

    }

    @NonNull
    @Override
    public TimeAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TimeRowBinding timeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.time_row, parent, false);
        return new TimeAdapter.MyHolder(timeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.timeRowBinding.setTime(timeModelList.get(position));
        if (oldHolder == null) {
            oldHolder = myHolder;
        }
        myHolder.itemView.setOnClickListener(view -> {

            if (oldHolder!=null){
                TimeDataModel.TimeModel oldTimeModel = timeModelList.get(oldPos);
                oldTimeModel.setSelected(false);
                timeModelList.set(oldPos, oldTimeModel);
                MyHolder oHolder = (MyHolder) oldHolder;
                oHolder.timeRowBinding.setTime(oldTimeModel);
            }
            currentPos=myHolder.getAdapterPosition();
            TimeDataModel.TimeModel model=timeModelList.get(currentPos);
            model.setSelected(true);

            timeModelList.set(currentPos,model);
            myHolder.timeRowBinding.setTime(model);

            oldHolder=myHolder;
            oldPos=currentPos;
            if (context instanceof ServiceDetailsActivity){
                ServiceDetailsActivity activity=(ServiceDetailsActivity) context;
                activity.setTimeItem(model);
            }
            if (context instanceof SubscriptionServiceActivity){
                SubscriptionServiceActivity activity=(SubscriptionServiceActivity) context;
                activity.setTimeItem(model);
            }
            if (context instanceof ServiceSentDetailsActivity){
                ServiceSentDetailsActivity activity=(ServiceSentDetailsActivity) context;
                activity.setTimeItem((model));
            }

        });
    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private TimeRowBinding timeRowBinding;

        public MyHolder(@NonNull TimeRowBinding timeRowBinding) {
            super(timeRowBinding.getRoot());
            this.timeRowBinding = timeRowBinding;
        }
    }
    public void updateList(List<TimeDataModel.TimeModel> list) {
        if (list != null) {
            this.timeModelList = list;
        }
        notifyDataSetChanged();
    }
}
