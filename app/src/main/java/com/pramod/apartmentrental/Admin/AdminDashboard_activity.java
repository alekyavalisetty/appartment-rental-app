package com.pramod.apartmentrental.Admin;

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
import com.pramod.apartmentrental.Admin.listings.AdminListings;
import com.pramod.apartmentrental.Admin.users.AdminUsersList;
import com.pramod.apartmentrental.Login_activity;
import com.pramod.apartmentrental.R;
import com.pramod.apartmentrental.Renter.Renter_add_apartment_fragment;
import com.pramod.apartmentrental.Renter.home_listings.Renter_home_fragment;
import com.pramod.apartmentrental.Renter.my_listings.Renter_my_listings;
import com.pramod.apartmentrental.User.AvailableApartments.User_home_fragment;
import com.pramod.apartmentrental.User.Favourites.User_favourites_fragment;
import com.pramod.apartmentrental.User.User_account_fragment;
import com.pramod.apartmentrental.User.User_maps_fragment;

public class AdminDashboard_activity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private BottomNavigationView AdminNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationClickListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){
                case R.id.account_admin:
                    getFragment(new User_account_fragment());
                    return true;
                case R.id.admin_listings:
                    getFragment(new AdminListings());
                    return true;
                case R.id.admin_users:
                    getFragment(new AdminUsersList());
                    return true;


            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_activity);

        AdminNavigation = findViewById(R.id.bottom_nav_view_admin);
        AdminNavigation.setOnNavigationItemSelectedListener(mOnNavigationClickListener);
        AdminNavigation.setSelectedItemId(R.id.admin_listings);

    }



    private void getFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }



}
