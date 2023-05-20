package com.example.atry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Activity selectedActivity = null;
                switch (item.getItemId()){
                    case R.id.home:
                        selectedActivity = new MainActivity(); break;
                    case R.id.group:
                        selectedActivity = new GroupActivity(); break;
                    case R.id.profile:
                        selectedActivity = new ProfileActivity(); break;
                }

                Intent intent = new Intent(ProfileActivity.this, selectedActivity.getClass());
                startActivity(intent);
                return true;
            }
        });
    }
}