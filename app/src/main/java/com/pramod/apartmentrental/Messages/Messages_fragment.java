package com.pramod.apartmentrental.Messages;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Admin.users.Users;
import com.pramod.apartmentrental.R;

import java.util.ArrayList;
import java.util.List;

public class Messages_fragment extends Fragment {

    private RecyclerView mMessageRecyclerView;
    private RecyclerView.Adapter mMessageAdapter;
    private RecyclerView.LayoutManager mMessageLayoutManager;
    private String currentUserId;
    private ArrayList<MessageUserObject> resultUserListings= new ArrayList<MessageUserObject>();



    public Messages_fragment() {
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
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mMessageRecyclerView = getView().findViewById(R.id.recyclerViewUsers);

        //allows to scroll freely through recycler view with no hickups
        mMessageRecyclerView.setNestedScrollingEnabled(false);
        mMessageRecyclerView.setHasFixedSize(true);

        mMessageLayoutManager = new LinearLayoutManager(getActivity());
        mMessageRecyclerView.setLayoutManager(mMessageLayoutManager);

        mMessageAdapter = new MessagesAdapter(getUserMessagesList(), getActivity());
        mMessageRecyclerView.setAdapter(mMessageAdapter);

        getUserMessagesDetails();
    }

    private void getUserMessagesDetails() {

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("contacts");
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot contactList : dataSnapshot.getChildren()){
                        getUserInfo(contactList.getKey());
                    }
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
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if(!key.equals(userId)){

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

                        MessageUserObject obj = new MessageUserObject(key, userName, userEmail, userPhone, userPhoto);
                        resultUserListings.add(obj);
                        mMessageAdapter.notifyDataSetChanged();
                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private ArrayList<MessageUserObject> getUserMessagesList() {
        return resultUserListings;
    }

}