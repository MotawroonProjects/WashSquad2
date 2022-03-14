package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.AdditionalRowBinding;
import com.creative.share.apps.wash_squad.databinding.WashRowBinding;
import com.creative.share.apps.wash_squad.models.DayModel;
import com.creative.share.apps.wash_squad.models.ItemToUpload;
import com.creative.share.apps.wash_squad.models.SubscribtionDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class WashAdapter extends RecyclerView.Adapter<WashAdapter.MyHolder> {

    private List<SubscribtionDataModel.WashSub> subServiceModelList;
    private Context context;
    private String lang;
    private ArrayList<DayModel> dayModelList;
    private ArrayList<String> dayModelList2;

    public WashAdapter(List<SubscribtionDataModel.WashSub> subServiceModelList, Context context) {
        this.subServiceModelList = subServiceModelList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        setDays();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WashRowBinding additionalRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.wash_row, parent, false);
        return new MyHolder(additionalRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.additionalRowBinding.setLang(lang);
        holder.additionalRowBinding.setModel(subServiceModelList.get(position));
        holder.additionalRowBinding.setDay(dayModelList.get(dayModelList2.indexOf(subServiceModelList.get(position).getDay().toUpperCase())).getDay_text());

        if (position % 2 == 0) {
            holder.additionalRowBinding.ll.setBackgroundColor(context.getResources().getColor(R.color.gray2));
        }


    }
    private void setDays() {
        dayModelList = new ArrayList<>();
        dayModelList.add(new DayModel(context.getString(R.string.Saturday)));
        dayModelList.add(new DayModel(context.getString(R.string.sunday)));
        dayModelList.add(new DayModel(context.getString(R.string.monday)));
        dayModelList.add(new DayModel(context.getString(R.string.tuesday)));
        dayModelList.add(new DayModel(context.getString(R.string.wendesday)));
        dayModelList.add(new DayModel(context.getString(R.string.thursday)));
        dayModelList.add(new DayModel(context.getString(R.string.friday)));
        dayModelList2 = new ArrayList<>();
        dayModelList2.add("SATURDAY");
        dayModelList2.add("SUNDAY");
        dayModelList2.add("MONDAY");
        dayModelList2.add("TUESDAY");
        dayModelList2.add("WEDNESDAY");
        dayModelList2.add("THURSDAY");
        dayModelList2.add("FRIDAY");
    }

    @Override
    public int getItemCount() {
        return subServiceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private WashRowBinding additionalRowBinding;

        public MyHolder(@NonNull WashRowBinding additionalRowBinding) {
            super(additionalRowBinding.getRoot());
            this.additionalRowBinding = additionalRowBinding;
        }
    }

}
