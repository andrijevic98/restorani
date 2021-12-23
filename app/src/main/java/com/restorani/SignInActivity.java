package com.restorani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.restorani.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    public ActivitySignInBinding binding;

    private DatabaseReference database;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Logovanje");

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuthWithPassword();
            }
        });
        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void firebaseAuthWithPassword() {
        mAuth.signInWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUser();

                        } else {
                            Toast.makeText(SignInActivity.this, "Pokušajte ponovo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getUser(){
        database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    UserModel user = task.getResult().getValue(UserModel.class);
                    Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                    i.putExtra("USER", user);
                    startActivity(i);
                }
            }
        });
    }
}