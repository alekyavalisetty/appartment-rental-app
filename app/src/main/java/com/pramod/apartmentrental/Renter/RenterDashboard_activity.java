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

public class RenterDashboard_activity extends AppCompatActivity {

    private BottomNavigationView UserNavigation;
    private FirebaseAuth mFirebaseAuth;
    MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_dashboard_activity);

        UserNavigation = findViewById(R.id.bottom_nav_view_renter);
        materialToolbar = findViewById(R.id.toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(materialToolbar);
        UserNavigation.setOnNavigationItemSelectedListener(mOnNavigationClickListener);
        UserNavigation.setSelectedItemId(R.id.home);


    }

    private void signOut() {
        mFirebaseAuth.signOut();
        Intent loginIntent = new Intent(this, Login_activity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
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

    //Toolbar options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.toolbar_signout:
                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
