package com.example.booksies.model;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksies.R;
import com.example.booksies.controller.SetLocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;

import java.util.ArrayList;

import static com.example.booksies.model.FirestoreHandler.acceptRequest;
import static com.example.booksies.model.FirestoreHandler.rejectRequest;

/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

class Map_Adapter extends RecyclerView.Adapter<Map_Adapter.MyViewHolder> {
    public ArrayList<String> requestList;
    public String bookID;
    //public static ArrayList<Boolean> expandable;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView borrower;
        public FloatingActionButton map;

        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
            borrower = v.findViewById(R.id.borrower);
            map = v.findViewById(R.id.map);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Map_Adapter(ArrayList<String> requestList, String bookID) {
        this.requestList = requestList;
        this.bookID = bookID;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Map_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home_expand_accepted, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.borrower.setText(requestList.get(position).split(":")[0]);

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity currentActivity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(currentActivity, SetLocationActivity.class);
                intent.putExtra("bookId", bookID);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Books").document(bookID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            //If the book has a location already, then pass in the lat lng
                            if (documentSnapshot.getGeoPoint("location") != null) {
                                GeoPoint geopoint = documentSnapshot.getGeoPoint("location");
                                intent.putExtra("lat", geopoint.getLatitude());
                                intent.putExtra("lat", geopoint.getLongitude());
                            }
                        }
                    }
                });
                currentActivity.startActivity(intent);

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return requestList.size();
    }


}