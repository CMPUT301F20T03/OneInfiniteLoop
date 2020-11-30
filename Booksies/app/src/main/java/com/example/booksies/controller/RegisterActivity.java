/*
* This activity allows a user to create an account for the app
* implements US 02.01.01
* Author Archit / Jacky(jzhuang)
 */

package com.example.booksies.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This class handles the creating of a user account
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText rEmail,rPassword,rUsername,rPhone;
    private Button btnRegister;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize UI refs

        rEmail = (EditText) findViewById(R.id.emailEditText);
        rUsername = (EditText) findViewById(R.id.usernameEditText);
        rPassword = (EditText) findViewById(R.id.passwordEditText);
        rPhone = (EditText) findViewById(R.id.phoneEditText);
        btnRegister = (Button) findViewById(R.id.register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //initialize Firestore and storage variables
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()) {
                    final String email = rEmail.getText().toString();
                    final String username = rUsername.getText().toString();
                    final String lowerUsername = username.toLowerCase();
                    final String pass = rPassword.getText().toString();
                    final String phone = rPhone.getText().toString();
                    //Makes sure fields are filled in
                    if (email.contains("@")
                            && !(pass.length() < 6)
                            && email.length() > 0
                            && phone.length() > 0) {
                        mAuth.createUserWithEmailAndPassword(username.concat("@gmail.com"), pass)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            // Sign in success, update UI with the signed-in user's information
                                            //Add user to Users collection
                                            final HashMap<String, String> data = new HashMap<>();
                                            data.put("username", username.split("@gmail.com")[0]);
                                            data.put("lowusername", lowerUsername);
                                            data.put("email", email);
                                            data.put("phone", phone);
                                            collectionReference
                                                    .document(user.getUid())
                                                    .set(data);
                                            Toast.makeText(RegisterActivity.this, "Authentication successful.",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(user);
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Username taken.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(RegisterActivity.this, "All fields must be filled in.\nPassword must be at least 6 characters",
                                Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Internet connection required",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Goes to login activity
    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);

    }
    /**
    * Checks if user is connected to internet
     * @return true if connected
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
