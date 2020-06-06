package com.pramod.apartmentrental.User.Favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_favourites_fragment extends Fragment {


    ImageView listImage;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mListingAdapter;
    private RecyclerView.LayoutManager mListingLayoutManager;
    private String currentUserID;


    public User_favourites_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mRecyclerView = getView().findViewById(R.id.recyclerViewFavourites);
        //allows to scroll freely through recycler view with no hickups
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mListingLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mListingLayoutManager);

        listImage = getView().findViewById(R.id.listImage);
        mListingAdapter = new User_favourites_Adapter(getDataSetListings(), getActivity());
        mRecyclerView.setAdapter(mListingAdapter);
        getListingID();

    }

    private void getListingID() {

        //String key= FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("favourites").push().getKey();

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID).child("favourites");
        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot showlist : dataSnapshot.getChildren()){
                        FetchListingInformation(showlist.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private ArrayList<User_favourites_Object> resultListings= new ArrayList<User_favourites_Object>();

    private void FetchListingInformation(final String key) {

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings").child(key);
        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {


                    String listingname = "";
                    String listingimageurl = "";
                    String listingdescription = "";
                    String listingprice = "";


                    if (dataSnapshot.child("listing_name").getValue() != null) {
                        listingname = dataSnapshot.child("listing_name").getValue().toString();
                    }

                    if (dataSnapshot.child("listing_description").getValue() != null) {
                        listingdescription = dataSnapshot.child("listing_description").getValue().toString();
                    }

                    if (dataSnapshot.child("listing_price").getValue() != null) {
                        listingprice = dataSnapshot.child("listing_price").getValue().toString();
                    }

                    if (!dataSnapshot.child("listing_image").getValue().equals("default")) {
                        listingimageurl = dataSnapshot.child("listing_image").getValue().toString();
                    }
                    else
                    {
                        listingimageurl = "default";
                    }

                    User_favourites_Object obj = new User_favourites_Object(key, listingname, listingdescription, listingprice, listingimageurl);
                    resultListings.add(obj);
                    mListingAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<User_favourites_Object> getDataSetListings() {
        return resultListings;
    }
}
