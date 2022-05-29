package com.creative.share.apps.wash_squad.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Market;
import com.creative.share.apps.wash_squad.activities_fragments.activity_time.TimeActivity;
import com.creative.share.apps.wash_squad.databinding.CategoryRowBinding;
import com.creative.share.apps.wash_squad.databinding.TimeRowBinding;
import com.creative.share.apps.wash_squad.models.CategoryModel;
import com.creative.share.apps.wash_squad.models.TimeDataModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    private List<CategoryModel> list;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private int currentPos = 1;
    private int oldPos = currentPos;
    private MyHolder oldHolder;
    private String lang;

    public CategoryAdapter(List<CategoryModel> list, Context context,Fragment fragment,String lang) {
        this.list = list;
        this.context = context;
        this.fragment=fragment;
        this.lang=lang;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding timeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.category_row, parent, false);
        return new MyHolder(timeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.timeRowBinding.setModel(list.get(position));
        myHolder.timeRowBinding.setLang(lang);
        if (oldHolder==null){
            oldHolder=myHolder;
            CategoryModel model=list.get(position);
            model.setSelected(true);
            list.set(position,model);
            myHolder.timeRowBinding.setModel(model);
            oldPos=position;
            if (fragment instanceof Fragment_Market){
                Fragment_Market fragmentMarket =(Fragment_Market) fragment;
                fragmentMarket.setItemCategory(model,position);

            }

        }
        holder.itemView.setOnClickListener(view -> {
            if (oldHolder!=null){
                CategoryModel oldCategoryModel=list.get(oldPos);
                oldCategoryModel.setSelected(false);
                list.set(oldPos,oldCategoryModel);

                MyHolder oHolder=(MyHolder) oldHolder;
                oHolder.timeRowBinding.setModel(oldCategoryModel);
            }
            currentPos=myHolder.getAdapterPosition();
            CategoryModel categoryModel=list.get(currentPos);
            categoryModel.setSelected(true);
            list.set(currentPos,categoryModel);
            myHolder.timeRowBinding.setModel(categoryModel);

            oldHolder=myHolder;
            oldPos=currentPos;


            if (fragment instanceof Fragment_Market){
                Fragment_Market fragment_market=(Fragment_Market) fragment;
                fragment_market.setItemCategory(categoryModel,currentPos);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void clearSelection() {
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

    public void updateList(List<CategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
