package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.fragment_offers.Fragment_Offers;
import com.creative.share.apps.wash_squad.databinding.OfferRowBinding;
import com.creative.share.apps.wash_squad.models.OfferDataModel;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyHolder> {

    private List<OfferDataModel.OfferModel> offerModelList;
    private Context context;
    private Fragment fragment;

    public OffersAdapter(List<OfferDataModel.OfferModel> offerModelList, Context context,Fragment fragment) {
        this.offerModelList = offerModelList;
        this.context = context;
        this.fragment=fragment;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferRowBinding offerRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.offer_row, parent, false);
        return new MyHolder(offerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        OfferDataModel.OfferModel offerModel = offerModelList.get(position);
        holder.offerRowBinding.setOfferModel(offerModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof Fragment_Offers){
                    Fragment_Offers fragment_offers=(Fragment_Offers)fragment;
                fragment_offers.show(offerModelList.get(holder.getLayoutPosition()));}
            }
        });


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
