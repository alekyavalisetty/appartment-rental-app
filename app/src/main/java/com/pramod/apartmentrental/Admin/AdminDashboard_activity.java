package com.pramod.apartmentrental.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminDashboard_activity extends AppCompatActivity {

    MaterialToolbar materialToolbar;
    private FirebaseAuth mFirebaseAuth;
    private BottomNavigationView AdminNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_activity);

        AdminNavigation = findViewById(R.id.bottom_nav_view_admin);
        materialToolbar = findViewById(R.id.toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        materialToolbar.setTitle("ADMIN DASHBOARD");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
