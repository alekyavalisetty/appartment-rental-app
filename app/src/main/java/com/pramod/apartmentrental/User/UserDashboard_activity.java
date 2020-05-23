package com.pramod.apartmentrental.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pramod.apartmentrental.R;

public class UserDashboard_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard_activity);

        BottomNavigationView UserNavigation = findViewById(R.id.bottom_nav_view_user);

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
