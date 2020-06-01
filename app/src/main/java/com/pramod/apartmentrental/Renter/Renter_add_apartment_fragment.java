package com.pramod.apartmentrental.Renter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class Renter_add_apartment_fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private DatabaseReference mListingDatabase,mUserDatabase;
    EditText listingName, listingDescription,  listingPrice, listingLocation1,listingLocation;
    private Double latitude= null, longitude= null;

    private Uri resultUri;
    private ImageView listingImage;
    String imageUrl;
    private String currentUId, aptName, aptLocation, aptDescription, aptPrice, key, aptLocation1 = " ";
    ImageButton getLocation;
    Button postButton;


    private String city, address;

    public Renter_add_apartment_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_add_apartment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        Places.initialize(getContext(), "AIzaSyDM14aZbu5rVipuAmAZ5ZI24VZ6HWTh0BI");
        PlacesClient placesClient = Places.createClient(getContext());




        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getActivity(), Login_activity.class);
                    startActivity(intent);
                }
            }
        };

        mListingDatabase = FirebaseDatabase.getInstance().getReference().child("listings");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        listingName = view.findViewById(R.id.listing_name);
        listingDescription = view.findViewById(R.id.listing_description);
        listingLocation1 = view.findViewById(R.id.listing_location1);

        listingLocation = view.findViewById(R.id.listing_location);
        listingPrice = view.findViewById(R.id.listing_price);
        listingImage = view.findViewById(R.id.listing_image);
        getLocation = view.findViewById(R.id.button_get_location);
        postButton = getActivity().findViewById(R.id.button_post_ad);


        listingLocation.setFocusable(false);
        listingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(getContext());

                startActivityForResult(intent, 100);

            }
        });

        //to select image from galary.
        listingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //tells the app to go outside the app and not from the app
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        //Get current location
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);

                } else {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());

                    List<Address> addresses;

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    try {

                        addresses = gcd.getFromLocation(latitude,longitude ,1);

                        if (addresses.size() >0) {
                            address = addresses.get(0).getAddressLine(0);
                            city = addresses.get(0).getLocality();

                            listingLocation.setText(address);
                        }

                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Location not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aptName = listingName.getText().toString();
                aptDescription = listingDescription.getText().toString();
                aptLocation = listingLocation.getText().toString();
                aptLocation1 = listingLocation1.getText().toString();
                aptPrice = listingPrice.getText().toString();

                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocationName(aptLocation, 1);

                    if (addresses.size() > 0) {
                        latitude = (addresses.get(0).getLatitude());
                        longitude = (addresses.get(0).getLongitude());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Create a new map to post details
                Map listingInfo = new HashMap();
                if (TextUtils.isEmpty(aptName))
                {
                    listingName.setError("Enter Apartment name");
                }
                else if (TextUtils.isEmpty(aptDescription))
                {
                    listingDescription.setError("Enter apartment description");
                }
                else if (TextUtils.isEmpty(aptLocation))
                {
                    listingLocation.setError("Please enter the address");
                }
                else if(TextUtils.isEmpty(aptPrice))
                {
                    listingPrice.setError("Please enter the rent amount");
                }
                else{

                    //for list id
                    key = FirebaseDatabase.getInstance().getReference().child("listings").push().getKey();
                    listingInfo.put("listing_id", key);
                    listingInfo.put("listing_name",aptName);
                    listingInfo.put("listing_description",aptDescription);
                    listingInfo.put("listing_location",aptLocation);
                    listingInfo.put("listing_location1",aptLocation1);
                    listingInfo.put("listing_price",aptPrice);
                    listingInfo.put("listing_renter_id",currentUId);
                    listingInfo.put("listing_city", city);
                    listingInfo.put("listing_latitude",latitude);
                    listingInfo.put("listing_longitude", longitude);



                    //Insert current listing id to the current user ID
                    mUserDatabase.child(currentUId).child("listings").child(key).child("listID").setValue(key);
                    mListingDatabase.child(key).updateChildren(listingInfo);

                    if (resultUri != null) {

                        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("listing_image").child(key);

                        Bitmap bitmap = null;

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), resultUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //tomake the image small size
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                        byte[] data = baos.toByteArray();

                        //uploading the image
                        UploadTask uploadTask = filepath.putBytes(data);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                getActivity().finish();
                            }
                        });

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                imageUrl = uri.toString();

                                                Map listingInfo = new HashMap();
                                                listingInfo.put("listing_image", imageUrl);
                                                mListingDatabase.child(key).updateChildren(listingInfo);
                                            }
                                        });

                                    }
                                }

                                currentRefreshFragment(Renter_add_apartment_fragment.this);

                            }
                        });

                    } else {

                        imageUrl = "default";

                        listingInfo.put("listing_image", imageUrl);
                        mListingDatabase.child(key).updateChildren(listingInfo);
                    }
                }
                ShowDialog showDialog = new ShowDialog();
                showDialog.show(getActivity().getSupportFragmentManager(), "Posting Dialog");

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the intent from activity code is 1
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            final Uri imageUri = data.getData();
            resultUri = imageUri;
            listingImage.setImageURI(resultUri);

        }

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);

            //Set Address in edit text
            listingLocation.setText(place.getAddress());

        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void currentRefreshFragment(Fragment fragment){

        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();

    }
}
