package com.example.booksies.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.example.booksies.model.adapters.NotificationAdapter;
import com.example.booksies.model.notification.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This Class handles the Home view
 */
public class NavigationActivity extends AppCompatActivity {

    FirebaseFirestore db;
    DocumentReference docRef;
    ArrayList<Notification> notificationDataList;


    /**
     * Responsible for creating activity when first launched
     * @param savedInstanceState: savedInstanceState is a reference to a Bundle object passed into the onCreate method
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Log.d("EDITBOOK", "----------------------------------------EDIT BOOK----------------------------------------");
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_navigation); // handles switch for bottom navigation

        Fragment frag;
        if(getIntent().getStringExtra("request")!= null)
        {
            frag = new RequestListFragment();

        }
        else
        {
            frag =new HomeFragment();

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).addToBackStack(null).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    /**
                     * Responsible for creating new fragments and setting new layouts when a
                     * specific menu item is selected
                     * @param item: A MenuItem which is part of the NavigationActivity at the bottom of the ui
                     */
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                HomeFragment frag = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).addToBackStack(null).commit();
                                break;
                            case R.id.action_request:
                                RequestListFragment reqFrag = new RequestListFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container,reqFrag)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.action_add_book:
                                AddBookFragment addBookFrag = new AddBookFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, addBookFrag)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.action_profile:
                                UserProfileFragment userProfileFragment = new UserProfileFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, userProfileFragment)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.action_scanner:
                                ScanFragment scanFragment = new ScanFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, scanFragment)
                                        .addToBackStack(null).commit();
                                break;
                        }
                        return true;
                    }
                });

        notificationDataList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
//        db.collection("Books")
//                .whereEqualTo("owner", user.getEmail())
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    @Override
//                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value,
//                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w("Reading Data Failed", "Listen failed.", error);
//                            return;
//                        }
//
//                        assert value != null;
//                        for (QueryDocumentSnapshot doc : value) {
//                            if (Objects.equals(doc.getString("status"), "REQUESTED")){
//                                ArrayList<String> requests = (ArrayList<String>) doc.get("request");
//                                String body = String.format("has requested %s", doc.getString("title"));
//                                for (int counter = 0; counter < requests.size(); counter++){
//                                    Notification newRequestNotification = new Notification(requests.get(counter).split("@")[0], body);
//                                    if (!notificationDataList.contains(newRequestNotification)){
//                                        notificationDataList.add(0, newRequestNotification);
//                                        notification(requests.get(counter).split("@")[0], body);
//                                    }
//                                }
//                                //notificationAdapter = new NotificationAdapter(getContext(), notificationDataList);
//
//                                //notificationList.setAdapter(notificationAdapter);
//                            }
//                        }
//                    }
//                });

        db.collection("Books")
                .whereEqualTo("owner", user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Snapshot", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("Notification", "New city: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    ArrayList<String> requests = (ArrayList<String>) dc.getDocument().get("request");
                                    String title = requests.get(requests.size()-1).split("@")[0];
                                    String body = String.format("has requested %s", dc.getDocument().getString("title"));
                                    notificationDataList.add(0, new Notification(title, body));
                                    notification(title, body);
                                    Log.d("Notification", "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d("Notification", "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });








    }

    /**
     * onCreateOptionsMenu is useful for creating a new Menu.
     * @param menu: A Menu object that is used to create Navigation Activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * If Menu Item and in this case action_logout is selected, the instance of FirebaseAuth is
     * signed out and this logs out the current user and a new user can thus login.
     * @param item: A MenuItem at the top of user interface
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.action_logout:   //this item has your app icon
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:

                break;


        }
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification(String title, String body){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        NotificationChannel channel=
                new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentText(title)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.baseline_account_circle_24)
                .setContentText(body);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
    }



}
