package com.restorani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.restorani.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public ActivityHomeBinding binding;
    private RestaurantsAdapter adapter;

    private DatabaseReference database;

    ArrayList<RestaurantModel> restaurants = new ArrayList<>();
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Restorani");

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(HomeActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        });

        database = FirebaseDatabase.getInstance().getReference();


        loadRestaurants();


        user = (UserModel) getIntent().getSerializableExtra("USER");

        if (user.getRole() != null && user.getRole().equals("admin")) {
            binding.add.setVisibility(View.VISIBLE);
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, AddRestaurantActivity.class);
                    startActivity(i);
                }
            });
        }


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.mainToolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        binding.drawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        binding.drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
                return false;
            }
        };
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        binding.mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    binding.drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }


    private void loadRestaurants() {
        database.child("public").child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurants = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    RestaurantModel restaurant = s.getValue(RestaurantModel.class);
                    restaurant.setId(s.getKey());
                    restaurants.add(restaurant);
                }
                setList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new RestaurantsAdapter(this, restaurants);
        binding.recyclerView.setAdapter(adapter);

    }


}