package com.pramod.apartmentrental.Admin.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Admin.listings.AdminListingsObject;
import com.pramod.apartmentrental.R;

import java.util.ArrayList;


public class AdminUsersList extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mUserListingAdapter;
    private RecyclerView.LayoutManager mUserListingLayoutManager;
    private String userId;
    private ArrayList<Users> resultUserListings= new ArrayList<Users>();


    public AdminUsersList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_users_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerViewUser);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mUserListingLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mUserListingLayoutManager);

        mUserListingAdapter = new UsersAdapter(getUserListDataSetListings(), getActivity());
        mRecyclerView.setAdapter(mUserListingAdapter);

        getUserDetails();
    }

    private ArrayList<Users> getUserListDataSetListings() {
        return resultUserListings;
    }

    private void getUserDetails() {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users");
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot showlist : dataSnapshot.getChildren()){
                        getUserInfo(showlist.getKey());
                    }

                }else
                {
                    Toast.makeText(getContext(), "It's Empty! Check back Later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo(final String key) {
        DatabaseReference listUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(key);
        listUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String userName = "";
                    String userImage = "";
                    String userPhoto = "";
                    String userPhone = "";
                    String userEmail = "";
                    String userId = key;

                    if (dataSnapshot.child("name").getValue() != null) {
                        userName = dataSnapshot.child("name").getValue().toString();
                    }

                    if (dataSnapshot.child("email").getValue() != null) {
                        userEmail = dataSnapshot.child("email").getValue().toString();
                    }

                    if (dataSnapshot.child("phone").getValue() != null) {
                        userPhone = dataSnapshot.child("phone").getValue().toString();
                    }

                    if (!dataSnapshot.child("photo").getValue().equals("")) {
                        userPhoto = dataSnapshot.child("photo").getValue().toString();
                    } else {
                        userPhoto = "";
                    }

                    Users obj = new Users(key, userName, userEmail, userPhone, userPhoto);
                    resultUserListings.add(obj);
                    mUserListingAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}