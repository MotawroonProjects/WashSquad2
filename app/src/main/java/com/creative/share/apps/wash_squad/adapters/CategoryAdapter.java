package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_time.TimeActivity;
import com.creative.share.apps.wash_squad.databinding.CategoryRowBinding;
import com.creative.share.apps.wash_squad.databinding.TimeRowBinding;
import com.creative.share.apps.wash_squad.models.TimeDataModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    private List<Object> timeModelList;
    private Context context;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;
    public CategoryAdapter(List<Object> timeModelList, Context context) {
        this.timeModelList = timeModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding timeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.category_row,parent,false);
        return new MyHolder(timeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (oldHolder != null) {
                    if(oldPos!=-1){
                      }
                }
                currentPos = holder.getAdapterPosition();
              notifyDataSetChanged();

                oldHolder = holder;
                oldPos = currentPos;


            }
        });


if(currentPos==position){
    holder.timeRowBinding.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
    holder.timeRowBinding.tvTitle.setTextColor(context.getResources().getColor(R.color.white));
}
else{
    holder.timeRowBinding.card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
    holder.timeRowBinding.tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));

}








    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void clearSelection()
    {
        //selected_pos = -1;
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CategoryRowBinding timeRowBinding;
        public MyHolder(@NonNull CategoryRowBinding timeRowBinding) {
            super(timeRowBinding.getRoot());
            this.timeRowBinding = timeRowBinding;
        }
    }

}
