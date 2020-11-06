/*
 * UserProfileFragment inflates activity_user_profile.xml
 *
 * Implements US 02.01.01 and US 04.03.01
 *
 * This activity is currently missing US 04.03.01
 *
 * Acknowledgments
 * https://firebase.google.com/docs/firestore/query-data/get-data#java_2
 * https://firebase.google.com/docs/auth/android/manage-users
 *
 */

package com.example.booksies.controller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.example.booksies.model.FirestoreHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;



import javax.annotation.Nullable;

public class UserProfileFragment extends Fragment {

    TextView username;
    TextView userPhone;
    TextView userEmail;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String uName;
    String uPhone;
    String uPass;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);

        username = view.findViewById(R.id.user_name);
        userPhone = view.findViewById(R.id.phone_number_display);
        userEmail = view.findViewById(R.id.email_display);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //initialize Firestore and storage variables
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(user.getUid());
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d("docExists", "DocumentSnapshot data: " + document.getData());
//                        uName = document.getString("username");
//                        uPhone = document.getString("phone");
//                        uPass = document.getString("password");
//                        username.setText(uName);
//                        userPhone.setText(uPhone);
//                        userEmail.setText(user.getEmail());
//                    } else {
//                        Log.d("noDoc", "No such document");
//                    }
//                } else {
//                    Log.d("fail", "get failed with ", task.getException());
//                }
//            }
//        });



        final Button editProfile = view.findViewById(R.id.edit_button);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditUserProfileActivity.class );
                intent.putExtra("user",uName);
                intent.putExtra("phone", uPhone);
                intent.putExtra("password", uPass);
                startActivity(intent);
            }
        });

        final DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Updating Data Failed", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    uName = snapshot.getString("username");
                    uPhone = snapshot.getString("phone");
                    uPass = snapshot.getString("password");
                    username.setText(uName);
                    userPhone.setText(uPhone);
                    userEmail.setText(user.getEmail());
                    Log.d("Updated data", "Current data: " + snapshot.getData());
                } else {
                    uName = snapshot.getString("username");
                    uPhone = snapshot.getString("phone");
                    uPass = snapshot.getString("password");
                    username.setText(uName);
                    userPhone.setText(uPhone);
                    userEmail.setText(user.getEmail());
                    Log.d("No new data", "Current data: null");
                }
            }
        });


        return view;
    }


}
