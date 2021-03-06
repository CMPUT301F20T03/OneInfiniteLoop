/*
 * ViewProfileFragment inflates activity_view_profile.xml
 *
 * Implements US 02.03.01
 *
 * view user profile when you click on a username with in the app
 *
 * Acknowledgments
 * https://firebase.google.com/docs/firestore/query-data/get-data#java_2
 * https://firebase.google.com/docs/auth/android/manage-users
 * https://firebase.google.com/docs/firestore/query-data/listen
 *
 * Author: Tony(xli)
 */
package com.example.booksies.controller;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

/**
 * This Class handles the Viewing User Profiles
 */
public class ViewProfileActivity extends AppCompatActivity {
    String userEmail;
    String username;
    String phone;
    FirebaseFirestore db;
    Task<QuerySnapshot> docRef;

    /**
     * Inflates view user profile page
     * @param savedInstanceState : savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_profile);
        final TextView usernameView = findViewById(R.id.view_user_name);
        final TextView phoneView = findViewById(R.id.view_phone_number_display);
        final TextView emailView = findViewById(R.id.view_email_display);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");
        usernameView.setText(username);

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("lowusername", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**
                     * On task complete
                     * @param task : Instance of Task<QuerySnapshot>
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            assert querySnapshot != null;
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                            userEmail = document.getString("email");
                            phone = document.getString("phone");
                            emailView.setText(userEmail);
                            phoneView.setText(phone);
                        } else {
                            Log.d("Query Snapshot", "Error getting documents: ", task.getException());
                        }
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


