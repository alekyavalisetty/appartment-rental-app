package com.pramod.apartmentrental.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.SelectionScreen_activity;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_account_fragment extends Fragment {

    LinearLayout signoutOwner, changeRole;
    TextView mAccountName;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String currentUId, userName;

    public User_account_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase Connections & id retrieval
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        signoutOwner = getView().findViewById(R.id.signout);
        changeRole = getView().findViewById(R.id.changeRole);

        mAccountName = getView().findViewById(R.id.account_name);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUId);

        //set Current user Name
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //get the child you want
                    if (map.get("name") != null) {
                        userName = map.get("name").toString();
                        mAccountName.setText(userName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changeRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectionScreen_activity.class);

                startActivity(intent);
                getActivity().finish();
            }
        });


        signoutOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //firebase signout
                mAuth.signOut();
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Login_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });




    }
}
