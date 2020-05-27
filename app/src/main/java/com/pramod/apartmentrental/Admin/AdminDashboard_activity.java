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

        setSupportActionBar(materialToolbar);


    }

    private void signOut() {
        mFirebaseAuth.signOut();
        Intent loginIntent = new Intent(this, Login_activity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

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
