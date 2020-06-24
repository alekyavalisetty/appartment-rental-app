package com.pramod.apartmentrental.Admin.listings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.my_listings.RenterApartmentObject;
import com.pramod.apartmentrental.Renter.my_listings.Renter_listing_Adapter;
import com.pramod.apartmentrental.User.AvailableApartments.User_home_Adapter;
import com.pramod.apartmentrental.User.AvailableApartments.User_home_object;

import java.util.ArrayList;
import java.util.List;


public class AdminListings extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mListingAdapter;
    private RecyclerView.LayoutManager mListingLayoutManager;
    private String listingUserId;
    Bundle bundle;
    private ArrayList<AdminListingsObject> resultListings= new ArrayList<AdminListingsObject>();
    private Boolean fromProfile = false;


    public AdminListings() {
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
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            listingUserId = bundle.getString("renterId");
            fromProfile = bundle.getBoolean("fromProfile");
        }


        return inflater.inflate(R.layout.fragment_admin_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerViewAdmin);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mListingLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mListingLayoutManager);

        mListingAdapter = new AdminListingAdapter(getListDataSetListings(), getActivity());
        mRecyclerView.setAdapter(mListingAdapter);

        getApartmentDetails();


    }



    private void getApartmentDetails() {
        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings");
        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot showlist : dataSnapshot.getChildren()){

                        GetApartmentDetails(showlist.getKey());
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



    private void GetApartmentDetails(final String key) {

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings").child(key);
        if(fromProfile)
        {

            listDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {

                        String listingname = "";
                        String listingimageurl = "";
                        String listingdescription = "";
                        String listingprice = "";
                        String listingrenter = dataSnapshot.child("listing_renter_id").getValue().toString();

                        if(listingrenter.equals(listingUserId))
                        {
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
                            } else {
                                listingimageurl = "default";
                            }

                            AdminListingsObject obj = new AdminListingsObject(key, listingname, listingdescription, listingprice, listingimageurl,listingrenter);
                            resultListings.add(obj);
                            mListingAdapter.notifyDataSetChanged();
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        else
        {
            listDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {

                        String listingname = "";
                        String listingimageurl = "";
                        String listingdescription = "";
                        String listingprice = "";
                        String listingrenter = dataSnapshot.child("listing_renter_id").getValue().toString();

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
                        } else {
                            listingimageurl = "default";
                        }

                        AdminListingsObject obj = new AdminListingsObject(key, listingname, listingdescription, listingprice, listingimageurl,listingrenter);
                        resultListings.add(obj);
                        mListingAdapter.notifyDataSetChanged();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    private List<AdminListingsObject> getListDataSetListings() {
        return resultListings;
    }
}