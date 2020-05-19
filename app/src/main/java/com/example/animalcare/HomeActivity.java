package com.example.animalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(HomeActivity.this);
        menuInflater.inflate(R.menu.menu_items,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.ItemLogout){

            firebaseAuth.signOut();
            Toast.makeText(this, "LogOut Successfull", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            finish();
            startActivity(i);
            return true;
        }
        else if(item.getItemId() == R.id.ItemProfile){

            //firebaseAuth.signOut();
            //Toast.makeText(this, "LogOut Successfull", Toast.LENGTH_SHORT).show();
            Intent j = new Intent(HomeActivity.this, profile.class);
            //finish();
            startActivity(j);
            return true;
        }

        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(false);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new AnimalFragmentAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);



    }
}
