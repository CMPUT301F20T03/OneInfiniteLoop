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

import static com.example.booksies.model.database.FirestoreHandler.acceptRequest;
import static com.example.booksies.model.database.FirestoreHandler.rejectRequest;


/**
 * This class is a custom adapter for RecyclerView
 * designed to handle a list of requests for books.
 * @author: Archit Siby (siby)
 * Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

class BooksListAdapter_Expand extends RecyclerView.Adapter<BooksListAdapter_Expand.BooksListViewHolder> {
    public ArrayList<String> requestList;
    public String bookID;
    //public static ArrayList<Boolean> expandable;


    /**
     * Custom view holder class
     */
    public class BooksListViewHolder extends RecyclerView.ViewHolder {
        public TextView request;
        public FloatingActionButton accept;
        public FloatingActionButton reject;

        // each data item is just a string in this case
        public BooksListViewHolder(View v) {
            super(v);
            request = v.findViewById(R.id.request);
            accept = v.findViewById(R.id.accept);
            reject = v.findViewById(R.id.reject);


        }
    }


    /**
     * Constructor of expandable adapter class
     * @param requestList list of book requests
     * @param bookID doument id of book
     */
    public BooksListAdapter_Expand(ArrayList<String> requestList, String bookID) {
        this.requestList = requestList;
        this.bookID = bookID;
    }


    /**
     * Creates the view holder
     * @param parent parent view
     * @param viewType type of view
     * @return ViewHolder of book item
     */
    @Override
    public BooksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home_expand, parent, false);

        BooksListViewHolder vh = new BooksListViewHolder(v);
        return vh;
    }

    /**
     * Bind's the view holder
     * @param holder custom view holder
     * @param position position of item
     */
    @Override
    public void onBindViewHolder(BooksListViewHolder holder, int position) {
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

    /**
     * get's the number of items in adapter
     * @return the number of items in recycler view
     */
    @Override
    public int getItemCount() {
        return requestList.size();
    }


}