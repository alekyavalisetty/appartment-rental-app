package com.pramod.apartmentrental.Renter.my_listings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pramod.apartmentrental.R;

import java.util.List;

public class Renter_listing_Adapter extends RecyclerView.Adapter<Renter_listing_view_holder> {

    private List<RenterApartmentObject> apartmentList;
    private Context context;

    private FirebaseAuth mAuth;
    private String currentUserID;

    private DatabaseReference listingDb = FirebaseDatabase.getInstance().getReference().child("listing");
    private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("users");

    //passes information between listingActivity
    public Renter_listing_Adapter(List<RenterApartmentObject> listingsList, Context context){

        this.apartmentList = listingsList;
        this.context = context;

    }

    @NonNull
    @Override
    public Renter_listing_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renter_listing, null, false);

        RecyclerView.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutView.setLayoutParams(lp);

        Renter_listing_view_holder rcv = new Renter_listing_view_holder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull Renter_listing_view_holder holder, int position) {

        holder.mListId.setText(apartmentList.get(position).getListingId());
        holder.mListName.setText(apartmentList.get(position).getListingName());
        holder.mListDescription.setText(apartmentList.get(position).getListingDescription());
        holder.mListPrice.setText("Price: " + apartmentList.get(position).getListingPrice());

        if(!apartmentList.get(position).getListingImageUrl().equals("default")){
            Glide.with(context).load(apartmentList.get(position).getListingImageUrl()).into(holder.mListImage);
        }



    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }


}
