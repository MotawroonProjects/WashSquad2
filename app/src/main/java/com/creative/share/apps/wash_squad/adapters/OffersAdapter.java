package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.OfferRowBinding;
import com.creative.share.apps.wash_squad.models.OfferDataModel;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyHolder> {

    private List<OfferDataModel.OfferModel> offerModelList;
    private Context context;

    public OffersAdapter(List<OfferDataModel.OfferModel> offerModelList, Context context) {
        this.offerModelList = offerModelList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferRowBinding offerRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.offer_row,parent,false);
        return new MyHolder(offerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        OfferDataModel.OfferModel offerModel = offerModelList.get(position);
        holder.offerRowBinding.setOfferModel(offerModel);





    }

    @Override
    public int getItemCount() {
        return offerModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private OfferRowBinding offerRowBinding;
        public MyHolder(@NonNull OfferRowBinding offerRowBinding) {
            super(offerRowBinding.getRoot());
            this.offerRowBinding = offerRowBinding;
        }
    }

}
