package com.restorani;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restorani.databinding.ActivityAddRestaurantBinding;
import com.restorani.databinding.ActivityCommentsBinding;

public class AddRestaurantActivity extends AppCompatActivity {

    public ActivityAddRestaurantBinding binding;

    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_restaurant);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Dodaj restoran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance().getReference();

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRestaurantModel restaurant = new AddRestaurantModel();
                restaurant.setAddress(binding.address.getText().toString());
                restaurant.setName(binding.name.getText().toString());
                restaurant.setImage(binding.image.getText().toString());
                restaurant.setPhone(binding.phone.getText().toString());
                restaurant.setComments(null);
                DatabaseReference newRequestRef = database.child("public").child("restaurants").push();
                newRequestRef.setValue(restaurant).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                        }
                    }
                });
            }
        });


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