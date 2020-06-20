package com.pramod.apartmentrental.User.AvailableApartments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pramod.apartmentrental.R;

import java.util.List;


public class User_home_Adapter extends RecyclerView.Adapter<User_home_view_holder> {


    private List<User_home_object> listingsList;
    private Context context;

    public User_home_Adapter(List<User_home_object> listingsList, Context context) {

        this.listingsList = listingsList;
        this.context = context;
    }


    @NonNull
    @Override
    public User_home_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View homeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_apartments, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        homeView.setLayoutParams(lp);
        return new User_home_view_holder((homeView));
    }

    @Override
    public void onBindViewHolder(@NonNull User_home_view_holder holder, int position) {

        holder.mListName.setText(listingsList.get(position).getListingName());
        holder.mListDescription.setText(listingsList.get(position).getListingDescription());
        holder.mListPrice.setText("Price: " +listingsList.get(position).getListingPrice() +" $");
        holder.mListId.setText(listingsList.get(position).getListingId());

        if(!listingsList.get(position).getListingImageUrl().equals("default")){
            Glide.with(context).load(listingsList.get(position).getListingImageUrl()).into(holder.mListImage);
        }

    }

    @Override
    public int getItemCount() {
        return listingsList.size();
    }


}

