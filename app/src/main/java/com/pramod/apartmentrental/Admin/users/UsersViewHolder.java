package com.pramod.apartmentrental.Admin.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.RenterModifyListing;
import com.pramod.apartmentrental.User.ContactRenter;
import com.pramod.apartmentrental.UserProfileSettings;

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mUserName, mUserPhone, mUserEmail, mUserId;
    public ImageView mUserImage;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mUserName = itemView.findViewById(R.id.userName);
        mUserPhone = itemView.findViewById(R.id.userPhone);
        mUserEmail = itemView.findViewById(R.id.userEmail);
        mUserId = itemView.findViewById(R.id.userId);
        mUserImage = itemView.findViewById(R.id.userImage);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), UserProfileSettings.class);
        Bundle b = new Bundle();
        b.putString("renter",mUserId.getText().toString());
        b.putString("role","admin");
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
