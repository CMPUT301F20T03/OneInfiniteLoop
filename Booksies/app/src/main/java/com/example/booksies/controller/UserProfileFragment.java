/*
 * UserProfileFragment inflates activity_user_profile.xml
 *
 * Implements US 02.01.01 and US 04.03.01 and US 05.02.01
 *
 * view user profile and well as notifications
 *
 * Acknowledgments
 * https://firebase.google.com/docs/firestore/query-data/get-data#java_2
 * https://firebase.google.com/docs/auth/android/manage-users
 * https://firebase.google.com/docs/firestore/query-data/listen
 *
 * Author: Tony(xli)
 */

package com.example.booksies.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.example.booksies.model.notification.Notification;
import com.example.booksies.model.adapters.NotificationAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * This Class handles the User Profiles as well as Notifications
 */
public class UserProfileFragment extends Fragment {

    TextView username;
    TextView userPhone;
    TextView userEmail;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String uEmail;
    String uPhone;
    String uName;
    ListView notificationList;
    ArrayAdapter<Notification> notificationAdapter;
    ArrayList<Notification> notificationDataList;


    /**
     * Inflates user profile page
     * @param inflater: inflater
     * @param container: container
     * @param savedInstanceState: savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);

        username = view.findViewById(R.id.user_name);
        userPhone = view.findViewById(R.id.phone_number_display);
        userEmail = view.findViewById(R.id.email_display);
        notificationList = view.findViewById(R.id.notification_list);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //initialize Firestore and storage variables
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(user.getUid());


        final Button editProfile = view.findViewById(R.id.edit_button);
        editProfile.setOnClickListener(new View.OnClickListener() {
            /**
             * Run when editProfile button is clicked and start activity for intent
             * @param view: current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditUserProfileActivity.class );
                intent.putExtra("email",uEmail);
                intent.putExtra("phone", uPhone);
                startActivity(intent);
            }
        });

        final DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            /**
             * Run on Event
             * @param snapshot: instance of DocumentSnapshot to get email, phone and username
             * @param e: an exception which is an instance of FirebaseFirestoreException
             */
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Updating Data Failed", "Listen failed.", e);
                    return;
                }

                uEmail = snapshot.getString("email");
                uPhone = snapshot.getString("phone");
                uName = snapshot.getString("username");
                username.setText(uName);
                userPhone.setText(uPhone);
                userEmail.setText(uEmail);
            }
        });


        notificationDataList = new ArrayList<>();

        db.collection("Books")
                .whereEqualTo("owner", user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    /**
                     * Run on Event
                     * @param value: instance of QuerySnapshot
                     * @param error: an exception which is an instance of FirebaseFirestoreException
                     */
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value,
                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Reading Data Failed", "Listen failed.", error);
                            return;
                        }

                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            if (Objects.equals(doc.getString("status"), "REQUESTED")){
                                ArrayList<String> requests = (ArrayList<String>) doc.get("request");
                                String body = String.format("has requested %s", doc.getString("title"));
                                if (requests != null){
                                    for (int counter = 0; counter < requests.size(); counter++){
                                        Notification newRequestNotification = new Notification(requests.get(counter).split("@")[0], body);
                                        if (!notificationDataList.contains(newRequestNotification)){
                                            notificationDataList.add(0, newRequestNotification);
                                        }
                                    }
                                }

                                notificationAdapter = new NotificationAdapter(getContext(), notificationDataList);
                                notificationList.setAdapter(notificationAdapter);
                            }
                        }
                    }
                });


        db.collection("Books")
                .whereArrayContains("borrowerID", user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    /**
                     * Run on Event
                     * @param value: instance of QuerySnapshot
                     * @param error: an exception which is an instance of FirebaseFirestoreException
                     */
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value,
                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Reading Data Failed", "Listen failed.", error);
                            return;
                        }

                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            if (Objects.equals(doc.getString("status"),"ACCEPTED")){
                                ArrayList<String> borrow = (ArrayList<String>) doc.get("borrowerID");
                                String body = String.format("has accepted your requests for %s", doc.getString("title"));
                                if (borrow != null){
                                    for (int counter = 0; counter < borrow.size(); counter++){
                                        Notification newAcceptNotification = new Notification(doc.getString("owner").split("@")[0], body);
                                        if (!notificationDataList.contains(newAcceptNotification)){
                                            notificationDataList.add(0,newAcceptNotification);
                                        }
                                    }
                                }

                                notificationAdapter = new NotificationAdapter(getContext(), notificationDataList);
                                notificationList.setAdapter(notificationAdapter);
                            }
                        }
                    }
                });

        db.collection("Books")
                .whereArrayContains("request", user.getEmail() + ":" + user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    /**
                     * Run on Event
                     * @param value: instance of QuerySnapshot
                     * @param error: an exception which is an instance of FirebaseFirestoreException
                     */
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value,
                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Reading Data Failed", "Listen failed.", error);
                            return;
                        }

                        assert value != null;
                        for (QueryDocumentSnapshot doc : value){
                            String body = String.format("has denied your requests for %s", doc.getString("title"));
                        }
                    }
                });
        return view;
    }
}
