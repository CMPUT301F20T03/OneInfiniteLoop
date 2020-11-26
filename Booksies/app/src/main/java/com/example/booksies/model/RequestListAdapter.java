package com.example.booksies.model;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.example.booksies.R;

import com.example.booksies.controller.ViewProfileActivity;
import com.google.firebase.storage.FirebaseStorage;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.MyViewHolder> {
    public ArrayList<Books> bookList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;
    String url;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleView;
        public TextView authorView;
        public TextView isbnView;
        public TextView statusView;
        public TextView ownerView;
        public ImageView imageView;


        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            titleView = v.findViewById(R.id.title_req);
            authorView = v.findViewById(R.id.author_req);
            isbnView = v.findViewById(R.id.isbn_req);
            statusView = v.findViewById(R.id.status_req);
            ownerView = v.findViewById(R.id.owner);
            imageView = (ImageView) v.findViewById(R.id.book_image);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RequestListAdapter(ArrayList<Books> bookList) {
        this.bookList = bookList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RequestListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_request, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleView.setText(bookList.get(position).getTitle());
        holder.authorView.setText(bookList.get(position).getAuthor());
        holder.isbnView.setText(bookList.get(position).getISBN());
        holder.statusView.setText(bookList.get(position).getStatus().toString().toLowerCase());
        holder.ownerView.setText(bookList.get(position).getOwner());

        holder.ownerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("username", bookList.get(position).getOwner());
                context.startActivity(intent);
            }
        });


        Glide.with(context)
                .load(bookList.get(position).getImageUrl())
                .into(holder.imageView);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bookList.size();
    }
}

