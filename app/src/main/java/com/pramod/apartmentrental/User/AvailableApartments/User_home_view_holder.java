package com.pramod.apartmentrental.User.AvailableApartments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.R;

public class User_home_view_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView  mListName, mListDescription, mListPrice;
    public ImageView mListImage;

    public User_home_view_holder(@NonNull View itemView) {
        super(itemView);

        mListName = itemView.findViewById(R.id.listNameApartment);
        mListDescription = itemView.findViewById(R.id.listDescriptionApartment);
        mListPrice = itemView.findViewById(R.id.listPriceApartment);
        mListImage = itemView.findViewById(R.id.listImageApartment);
    }


    @Override
    public void onClick(View view) {

    }
}
