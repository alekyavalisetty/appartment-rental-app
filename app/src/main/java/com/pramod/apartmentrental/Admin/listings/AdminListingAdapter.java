package com.pramod.apartmentrental.Admin.listings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pramod.apartmentrental.R;
import java.util.List;

public class AdminListingAdapter extends RecyclerView.Adapter<AdminListingViewHolder> {

    private List<AdminListingsObject> listingsList;
    private Context context;

    public AdminListingAdapter(List<AdminListingsObject> listingsList, Context context) {
        this.listingsList = listingsList;
        this.context = context;
    }


    @NonNull
    @Override
    public AdminListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View homeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_apartments, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        homeView.setLayoutParams(lp);
        return new AdminListingViewHolder((homeView));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListingViewHolder holder, int position) {

        holder.mListName.setText(listingsList.get(position).getListingName());
        holder.mListDescription.setText(listingsList.get(position).getListingDescription());
        holder.mListPrice.setText("Price: " +listingsList.get(position).getListingPrice() +" $");
        holder.mListId.setText(listingsList.get(position).getListingId());
        holder.mRenterId.setText(listingsList.get(position).getListingRenter());

        if(!listingsList.get(position).getListingImageUrl().equals("default")){
            Glide.with(context).load(listingsList.get(position).getListingImageUrl()).into(holder.mListImage);
        }


    }

    @Override
    public int getItemCount() {
        return listingsList.size();
    }
}
