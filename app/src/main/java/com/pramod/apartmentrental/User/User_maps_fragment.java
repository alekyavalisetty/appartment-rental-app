package com.pramod.apartmentrental.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.R;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_maps_fragment extends Fragment implements OnMapReadyCallback {

    GoogleMap UserGoogleMap;
    MapView mMapView;
    String listingID;
    View mView;
    String currentUserID;
    Double latitude, longitude;
    private MarkerOptions markerOptions = new MarkerOptions();
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    TextView myLocation;

    public User_maps_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_maps_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mMapView = view.findViewById(R.id.googleMap);

        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myLocation = getActivity().findViewById(R.id.getlocation);


        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);

                } else {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    UserGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    UserGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("My Current Location"));
                    CameraPosition MyLocation = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(18).bearing(0).tilt(45).build();

                    UserGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(MyLocation));

                }
            }
        });

    }



    private void getMapParameters(final GoogleMap googleMap) {

        DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings");

        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot showlist : dataSnapshot.getChildren()){

                        GetListInformation(showlist.getKey(),googleMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetListInformation(String key, final GoogleMap googleMap) {
         DatabaseReference listDb = FirebaseDatabase.getInstance().getReference().child("listings").child(key);
        listDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String name= null, snippet = null;

                if (dataSnapshot.child("listing_id").getValue() != null) {
                    listingID = dataSnapshot.child("listing_id").getValue().toString();
                }

                String listingrenter = dataSnapshot.child("listing_renter_id").getValue().toString();

                if(!listingrenter.equals(currentUserID)) {
                    if (dataSnapshot.child("listing_latitude").getValue() != null) {
                        latitude = Double.parseDouble(dataSnapshot.child("listing_latitude").getValue().toString());
                    }

                    if (dataSnapshot.child("listing_longitude").getValue() != null) {
                        longitude = Double.parseDouble(dataSnapshot.child("listing_longitude").getValue().toString());
                    }

                    if (dataSnapshot.child("listing_name").getValue() != null) {
                        name = dataSnapshot.child("listing_name").getValue().toString();
                    }
                    if (dataSnapshot.child("listing_description").getValue() != null) {
                        snippet = dataSnapshot.child("listing_description").getValue().toString();
                    }

                    latLngs.add(new LatLng(latitude,longitude));

                    for(LatLng point : latLngs){
                        markerOptions.position(point);
                        markerOptions.title(name);
                        markerOptions.snippet(snippet);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                        googleMap.addMarker(markerOptions);

                    }

                    UserGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(12).bearing(1).tilt(45).build();
                    UserGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                /**/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UserGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Intent intent = new Intent(getActivity(), ApartmentDetails.class);

                Bundle b = new Bundle();
                b.putString("listID",listingID);
                intent.putExtras(b);

                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UserGoogleMap = googleMap;
        getMapParameters(googleMap);


    }

}

