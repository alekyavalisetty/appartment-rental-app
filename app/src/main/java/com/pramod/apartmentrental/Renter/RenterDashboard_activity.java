package com.pramod.apartmentrental.Renter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.User.User_account_fragment;
import com.pramod.apartmentrental.User.User_favourites_fragment;
import com.pramod.apartmentrental.User.User_home_fragment;
import com.pramod.apartmentrental.User.User_maps_fragment;

public class RenterDashboard_activity extends AppCompatActivity {

    private BottomNavigationView UserNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_dashboard_activity);

        UserNavigation = findViewById(R.id.bottom_nav_view_renter);

        UserNavigation.setOnNavigationItemSelectedListener(mOnNavigationClickListener);
        //Initialise user home fragment
        UserNavigation.setSelectedItemId(R.id.home);
    }

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
                    getFragment(new Renter_account_fragment());
                    return true;
            }
            return false;
        }
    };

    private void getFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
