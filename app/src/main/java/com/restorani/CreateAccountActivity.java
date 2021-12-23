package com.restorani;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restorani.databinding.ActivityCreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {

    public ActivityCreateAccountBinding binding;


    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Kreiranje naloga");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFirebaseAccount();
            }
        });

        database = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
    }

    private void createFirebaseAccount() {
        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference newRequestRef = database.child("users").child(task.getResult().getUser().getUid());
                            newRequestRef.setValue(new UserModel(binding.firstName.getText().toString(), binding.lastName.getText().toString(), task.getResult().getUser().getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(CreateAccountActivity.this, HomeActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Došlo je do greške. Proverite podatke i pokušajte ponovo.", Toast.LENGTH_LONG).show();
                        }
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