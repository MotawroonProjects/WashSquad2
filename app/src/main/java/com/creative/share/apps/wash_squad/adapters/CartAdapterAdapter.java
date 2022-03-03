package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.activities_fragments.activity_home.fragments.Fragment_Cart;
import com.creative.share.apps.wash_squad.databinding.CartRowBinding;
import com.creative.share.apps.wash_squad.models.ItemToUpload;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartAdapterAdapter extends RecyclerView.Adapter<CartAdapterAdapter.MyHolder> {

    private List<ItemToUpload>  itemToUploadList;
    private Context context;
    private String lang;
    private Fragment_Cart fragment_cart;
    private LinearLayoutManager manager;



    public CartAdapterAdapter(List<ItemToUpload> itemToUploadList, Context context,Fragment_Cart fragment_cart) {
        this.itemToUploadList = itemToUploadList;
        this.context = context;
        this.fragment_cart = fragment_cart;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartRowBinding cartRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.cart_row,parent,false);
        return new MyHolder(cartRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        ItemToUpload itemToUpload = itemToUploadList.get(position);
        holder.cartRowBinding.setLang(lang);
        holder.cartRowBinding.setItemModel(itemToUpload);

        AdditionalAdapter adapter = new AdditionalAdapter(itemToUpload.getSub_services(),context);
        manager = new LinearLayoutManager(context);
        holder.cartRowBinding.recView.setLayoutManager(manager);

        holder.cartRowBinding.recView.setAdapter(adapter);


        holder.cartRowBinding.btnSend.setOnClickListener(view -> {
            ItemToUpload itemToUpload1 = itemToUploadList.get(holder.getAdapterPosition());
            fragment_cart.setItemDataToUpload(itemToUpload1,holder.getAdapterPosition());
        });


    }

    @Override
    public int getItemCount() {
        return itemToUploadList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CartRowBinding cartRowBinding;
        public MyHolder(@NonNull CartRowBinding cartRowBinding) {
            super(cartRowBinding.getRoot());
            this.cartRowBinding = cartRowBinding;
        }
    }

}
