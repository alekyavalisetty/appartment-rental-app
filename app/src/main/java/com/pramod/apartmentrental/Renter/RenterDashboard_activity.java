package com.pramod.apartmentrental.Renter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.home_listings.Renter_home_fragment;
import com.pramod.apartmentrental.Renter.my_listings.Renter_my_listings;
import com.pramod.apartmentrental.User.User_account_fragment;

public class RenterDashboard_activity extends AppCompatActivity {

    private BottomNavigationView UserNavigation;
    private FirebaseAuth mFirebaseAuth;
    MaterialToolbar materialToolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationClickListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){
                case R.id.home:
                    getFragment(new Renter_home_fragment());
                    return true;
                case R.id.my_listing:
                    getFragment(new Renter_my_listings());
                    return true;
                case R.id.add_listing:
                    getFragment(new Renter_add_apartment_fragment());
                    return true;
                case R.id.account_Renter:
                    getFragment(new User_account_fragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_dashboard_activity);

        UserNavigation = findViewById(R.id.bottom_nav_view_renter);
        materialToolbar = findViewById(R.id.toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        materialToolbar.setTitle("RENTER DASHBOARD");
        UserNavigation.setOnNavigationItemSelectedListener(mOnNavigationClickListener);
        UserNavigation.setSelectedItemId(R.id.account_Renter);


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
