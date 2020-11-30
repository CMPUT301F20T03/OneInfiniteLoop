/*
* EditUserProfileActivity inflates activity_edit_profile.xml
*
* Implements US 02.02.01
*
* Author: Tony(xli)
*
 */

package com.example.booksies.controller;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This Class handles the Editing User Profiles
 */
public class EditUserProfileActivity extends AppCompatActivity {

    String uEmail;
    String uPhone;
    FirebaseFirestore db;
    DocumentReference documentReference;


    /**
     * Inflates edit profile page
     * @param savedInstanceState: savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final EditText userPhone = findViewById(R.id.phone_number_edit);
        final EditText userEmail = findViewById(R.id.user_email_edit);
        Button ok = findViewById(R.id.ok_button);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        uEmail = getIntent().getStringExtra("email");
        uPhone = getIntent().getStringExtra("phone");
        userEmail.setText(uEmail);
        userPhone.setText(uPhone);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(user.getUid());
        final HashMap<String, String> data = new HashMap<>();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uEmail = userEmail.getText().toString();
                uPhone = userPhone.getText().toString();
                documentReference.update("email", uEmail);
                documentReference.update("phone",uPhone);
                finish();
            }
        });
    }


    /**
     * Go back to the previous activity
     * @return boolean
     */
    //Go back to whatever opened this activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
