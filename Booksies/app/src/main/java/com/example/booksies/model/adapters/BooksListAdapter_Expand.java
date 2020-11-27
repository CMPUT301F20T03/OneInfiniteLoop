package com.example.booksies.model.adapters;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksies.R;
import com.example.booksies.controller.ViewProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.booksies.model.FirestoreHandler.acceptRequest;
import static com.example.booksies.model.FirestoreHandler.rejectRequest;


/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

class BooksListAdapter_Expand extends RecyclerView.Adapter<BooksListAdapter_Expand.MyViewHolder> {
    public ArrayList<String> requestList;
    public String bookID;
    //public static ArrayList<Boolean> expandable;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView request;
        public FloatingActionButton accept;
        public FloatingActionButton reject;

        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
            request = v.findViewById(R.id.request);
            accept = v.findViewById(R.id.accept);
            reject = v.findViewById(R.id.reject);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BooksListAdapter_Expand(ArrayList<String> requestList, String bookID) {
        this.requestList = requestList;
        this.bookID = bookID;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BooksListAdapter_Expand.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home_expand, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.request.setText(requestList.get(position).split("@gmail.com")[0]);
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity currentActivity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(currentActivity, ViewProfileActivity.class);
                intent.putExtra("username", requestList.get(position).split("@gmail.com")[0]);
                currentActivity.startActivity(intent);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(requestList.get(position),bookID);

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(requestList.get(position),bookID);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return requestList.size();
    }


}