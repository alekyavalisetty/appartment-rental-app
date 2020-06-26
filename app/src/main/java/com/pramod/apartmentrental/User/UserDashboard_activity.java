package com.pramod.apartmentrental.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pramod.apartmentrental.Messages.Messages_fragment;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.AvailableApartments.User_home_fragment;
import com.pramod.apartmentrental.User.Favourites.User_favourites_fragment;

public class UserDashboard_activity extends AppCompatActivity {

    private BottomNavigationView UserNavigation;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference mMessagesDatabase, mUserDatabase;
    String currentId;
    int count =0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationClickListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){
                case R.id.home:
                    getFragment(new User_home_fragment());
                    return true;
                case R.id.maps:
                    getFragment(new User_maps_fragment());
                    return true;
                case R.id.account_user:
                    getFragment(new User_account_fragment());
                    return true;
                case R.id.favourites:
                    getFragment(new User_favourites_fragment());
                    return true;
                case R.id.my_messages:
                    getFragment(new Messages_fragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_activity);

        UserNavigation = findViewById(R.id.bottom_nav_view_user);
        mFirebaseAuth = FirebaseAuth.getInstance();

        UserNavigation.setOnNavigationItemSelectedListener(mOnNavigationClickListener);
        //Initialise user home fragment
        UserNavigation.setSelectedItemId(R.id.maps);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentId = mFirebaseAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentId);

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    count = (int)dataSnapshot.child("contacts").getChildrenCount();
                    BadgeDrawable badge = UserNavigation.getOrCreateBadge(R.id.my_messages);
                    badge.setNumber(count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
