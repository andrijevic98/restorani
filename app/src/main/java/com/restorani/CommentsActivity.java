package com.restorani;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restorani.databinding.ActivityCommentsBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    public ActivityCommentsBinding binding;
    private CommentsAdapter adapter;

    private DatabaseReference database;
    private RestaurantModel restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Komentari");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        restaurant = (RestaurantModel) getIntent().getSerializableExtra("RESTAURANT");
        setList();

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCommentDialog.newInstance(new AddCommentDialog.DialogAction() {
                    @Override
                    public void onAdded() {
                        loadRestaurant();
                    }
                }, restaurant).show(getSupportFragmentManager(), AddCommentDialog.TAG);
            }
        });

        database = FirebaseDatabase.getInstance().getReference();


    }

    private void loadRestaurant() {
        database.child("public").child("restaurants").child(restaurant.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    restaurant = task.getResult().getValue(RestaurantModel.class);
                    setList();
                } else {

                }
            }
        });
    }

    private void setList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new CommentsAdapter(this, restaurant);
        binding.recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}