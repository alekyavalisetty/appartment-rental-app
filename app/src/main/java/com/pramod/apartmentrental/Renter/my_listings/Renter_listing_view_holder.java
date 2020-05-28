package com.pramod.apartmentrental.Renter.my_listings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.pramod.apartmentrental.R;

public class Renter_listing_view_holder extends ViewHolder implements OnClickListener{


    public TextView mListId, mListName, mListDescription, mListPrice;
    public ImageView mListImage;
    Context context;
    private FirebaseAuth mAuth;
    private String currentUserID,listingID;



    public Renter_listing_view_holder(@NonNull View itemView) {
        super(itemView);


        itemView.setOnClickListener(this);
        mListId = itemView.findViewById(R.id.listId);
        mListName = itemView.findViewById(R.id.listName);
        mListDescription = itemView.findViewById(R.id.listDescription);
        mListPrice = itemView.findViewById(R.id.listPrice);
        mListImage = itemView.findViewById(R.id.listImage);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        listingID = mListId.getText().toString();


        mListImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onClick(View view) {

     /* Intent intent = new Intent(view.getContext(), ModifyListingDetails.class);
        Bundle b = new Bundle();
        b.putString("listID",mListId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);*/
    }




}
