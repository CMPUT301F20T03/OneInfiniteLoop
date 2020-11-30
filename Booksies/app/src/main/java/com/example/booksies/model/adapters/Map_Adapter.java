package com.example.booksies.model.adapters;


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
import com.example.booksies.controller.ViewProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

public class Map_Adapter extends RecyclerView.Adapter<Map_Adapter.MapAdapterViewHolder> {
    public ArrayList<String> requestList;
    public String bookID;
    //public static ArrayList<Boolean> expandable;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MapAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView borrower;
        public FloatingActionButton map;

        // each data item is just a string in this case
        public MapAdapterViewHolder(View v) {
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
    public MapAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home_expand_accepted, parent, false);

        MapAdapterViewHolder vh = new MapAdapterViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MapAdapterViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.borrower.setText(requestList.get(position).split("@gmail.com")[0]);
        holder.borrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity currentActivity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(currentActivity, ViewProfileActivity.class);
                intent.putExtra("username", requestList.get(position).split("@gmail.com")[0]);
                currentActivity.startActivity(intent);
            }
        });

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
                                intent.putExtra("lon", geopoint.getLongitude());
                            }
                            currentActivity.startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return requestList.size();
    }


}