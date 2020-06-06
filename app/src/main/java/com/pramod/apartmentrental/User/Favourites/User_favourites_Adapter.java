package com.pramod.apartmentrental.User.Favourites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.ApartmentDetails;

import java.util.List;

public class User_favourites_Adapter extends RecyclerView.Adapter<User_favourites_Viewholder>  {


    private List<User_favourites_Object> listingsList;
    private Context context;

    //passes information between listingActivity
    public User_favourites_Adapter(List<User_favourites_Object> listingsList, Context context){
        this.listingsList = listingsList;
        this.context = context;
    }


    @NonNull
    @Override
    public User_favourites_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_favourites_listing, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        User_favourites_Viewholder rcv = new User_favourites_Viewholder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull User_favourites_Viewholder holder, int position) {
        holder.mListId.setText(listingsList.get(position).getListingId());
        holder.mListName.setText(listingsList.get(position).getListingName());
        holder.mListDescription.setText(listingsList.get(position).getListingDescription());
        holder.mListPrice.setText("Price: " +listingsList.get(position).getListingPrice());

        if(!listingsList.get(position).getListingImageUrl().equals("default")){
            Glide.with(context).load(listingsList.get(position).getListingImageUrl()).into(holder.mListImage);
        }
    }

    @Override
    public int getItemCount() {
        return listingsList.size();
    }
}
