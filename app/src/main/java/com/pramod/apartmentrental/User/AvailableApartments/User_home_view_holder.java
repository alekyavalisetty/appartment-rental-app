package com.pramod.apartmentrental.User.AvailableApartments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.ApartmentDetails;

public class User_home_view_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView  mListName, mListDescription, mListPrice, mListId;
    public ImageView mListImage;

    public User_home_view_holder(@NonNull View itemView) {
        super(itemView);

        mListName = itemView.findViewById(R.id.listNameApartment);
        mListDescription = itemView.findViewById(R.id.listDescriptionApartment);
        mListPrice = itemView.findViewById(R.id.listPriceApartment);
        mListImage = itemView.findViewById(R.id.listImageApartment);
        mListId = itemView.findViewById(R.id.listId);
    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(view.getContext(), ApartmentDetails.class);
        Bundle b = new Bundle();

        b.putString("listID",mListId.getText().toString());

        intent.putExtras(b);

        view.getContext().startActivity(intent);

    }
}
