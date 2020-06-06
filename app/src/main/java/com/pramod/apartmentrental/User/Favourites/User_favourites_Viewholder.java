package com.pramod.apartmentrental.User.Favourites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.ApartmentDetails;

public class User_favourites_Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mListId, mListName, mListDescription, mListPrice;
    public ImageView mListImage;

    public User_favourites_Viewholder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        mListId = itemView.findViewById(R.id.listId);
        mListName = itemView.findViewById(R.id.listName);
        mListDescription = itemView.findViewById(R.id.listDescription);
        mListPrice = itemView.findViewById(R.id.listPrice);
        mListImage = itemView.findViewById(R.id.listImage);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ApartmentDetails.class);
        Bundle b = new Bundle();
        b.putString("listID",mListId.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
