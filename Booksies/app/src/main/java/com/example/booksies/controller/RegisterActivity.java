package com.example.booksies.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                final String email= rEmail.getText().toString();
                final String username = rUsername.getText().toString();
                final String pass = rPassword.getText().toString();
                final String phone = rPhone.getText().toString();
                if(email.contains("@")
                        && !(pass.length()<6)
                        && email.length() > 0
                        && phone.length() > 0)
                {
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Sign in success, update UI with the signed-in user's information
                                        //Add user to Users collection
                                        final HashMap<String, String> data = new HashMap<>();
                                        data.put("username", username);
                                        data.put("email", email);
                                        data.put("password", pass);
                                        data.put("phone", phone);
                                        collectionReference
                                                .document(user.getUid())
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("RegisterActivity", "User stored in database successfully");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("RegisterActivity", "User could not be added");
                                                    }
                                                });

                                        Log.d("RegisterActivity", "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Enter valid email and password must be atleast 6 characters",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);

    }
}
