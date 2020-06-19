package com.pramod.apartmentrental.Admin.users;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pramod.apartmentrental.Admin.listings.AdminListingsObject;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder>{

    private List<Users> userList;
    private Context context;

    public UsersAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
