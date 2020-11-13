/*

 */

package com.example.booksies.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.booksies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditUserProfileActivity extends AppCompatActivity {

    String uName;
    String uPhone;
    FirebaseFirestore db;
    DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final EditText userPhone = findViewById(R.id.phone_number_edit);
        final EditText userName = findViewById(R.id.username_edit);
        Button ok = findViewById(R.id.ok_button);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        uName = getIntent().getStringExtra("user");
        uPhone = getIntent().getStringExtra("phone");
        final String uPass = getIntent().getStringExtra("password");
        userName.setText(uName);
        userPhone.setText(uPhone);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(user.getUid());
        final HashMap<String, String> data = new HashMap<>();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName = userName.getText().toString();
                uPhone = userPhone.getText().toString();
                data.put("username",uName);
                data.put("phone", uPhone);
                data.put("password", uPass);
                data.put("email", user.getEmail());
                documentReference
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("UpdateProfile", "Profile updated successfully");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("FailUpdateProfile", "Profile update unsuccessfully");
                            }
                        });

            }
        });
    }

    //Go back to whatever opened this activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
