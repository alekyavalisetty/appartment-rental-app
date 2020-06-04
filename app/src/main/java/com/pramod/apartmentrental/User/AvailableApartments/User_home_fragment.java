package com.pramod.apartmentrental.User.AvailableApartments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import java.util.ArrayList;
import java.util.List;

public class User_home_fragment extends Fragment {


    String  currentUserID;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter ListingCardItemAdapter;
    private RecyclerView.LayoutManager mListingLayoutManager;



    public User_home_fragment() {
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
        return inflater.inflate(R.layout.fragment_user_home_fragment, container, false);
    }
    private ArrayList<User_home_object> resultListings= new ArrayList<User_home_object>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mRecyclerView = view.findViewById(R.id.recyclerViewApartments);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mListingLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mListingLayoutManager);

        ListingCardItemAdapter = new User_home_Adapter(getListDataSetListings(), getActivity());
        mRecyclerView.setAdapter(ListingCardItemAdapter);

        getRenterApartmentDetails();
    }

    private void getRenterApartmentDetails() {

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

        ListingCardItemAdapter.notifyDataSetChanged();
    }

    private void GetApartmentDetails(final String key) {


        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings").child(key);
        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String listingname = "";
                    String listingimageurl = "";
                    String listingdescription = "";
                    String listingprice = "";
                    String listingrenter = dataSnapshot.child("listing_renter_id").getValue().toString();

                    if(!listingrenter.equals(currentUserID)) {
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

                        User_home_object obj = new User_home_object(key, listingname, listingdescription, listingprice, listingimageurl);
                        resultListings.add(obj);
                        ListingCardItemAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private List<User_home_object> getListDataSetListings() {
        return resultListings;
    }
}
