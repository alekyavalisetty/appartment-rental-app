package com.pramod.apartmentrental.Admin.listings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.RenterModifyListing;

public class AdminListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView  mListName, mListDescription, mListPrice, mListId, mRenterId;
    public ImageView mListImage;

    public AdminListingViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mListName = itemView.findViewById(R.id.listNameApartment);
        mListDescription = itemView.findViewById(R.id.listDescriptionApartment);
        mListPrice = itemView.findViewById(R.id.listPriceApartment);
        mListImage = itemView.findViewById(R.id.listImageApartment);
        mListId = itemView.findViewById(R.id.listIdApartment);
        mRenterId = itemView.findViewById(R.id.listRenter);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), RenterModifyListing.class);
        Bundle b = new Bundle();
        b.putString("listID",mListId.getText().toString());
        b.putString("renterID",mRenterId.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
