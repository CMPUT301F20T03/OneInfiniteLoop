package com.example.booksies.model;


import android.content.Context;
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

import com.google.firebase.storage.FirebaseStorage;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public ArrayList<Books> bookList;
    //public static ArrayList<Boolean> expandable;
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
        public ImageView imageView;
        public RelativeLayout expand;
        public LinearLayout linearLayout;
        public RecyclerView r_view;
        public RecyclerView.Adapter mAdapter;


        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            titleView = v.findViewById(R.id.book_name);
            authorView = v.findViewById(R.id.book_author);
            isbnView = v.findViewById(R.id.book_isbn);
            statusView = v.findViewById(R.id.book_status);
            expand = v.findViewById(R.id.expandable_layout);
            r_view = (RecyclerView) v.findViewById(R.id.expand_rlist);
            r_view.setLayoutManager(new LinearLayoutManager(v.getContext()));

            imageView = (ImageView) v.findViewById(R.id.book_image);
            linearLayout = v.findViewById(R.id.linear_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Books book = bookList.get(getAdapterPosition());
                    book.expand = !book.expand;

                    notifyItemChanged(getAdapterPosition());


                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Books> bookList) {
        this.bookList = bookList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home, parent, false);

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


        Glide.with(context)
                .load(bookList.get(position).getImageUrl())
                .into(holder.imageView);



        if(!bookList.get(position).expand)
        holder.expand.setVisibility(View.GONE);
        else
        {
            holder.expand.setVisibility(View.VISIBLE);
        }

        holder.mAdapter = new MyAdapter_Expand(bookList.get(position).getBookRequests(),bookList.get(position).getDocID());
        holder.r_view.setAdapter(holder.mAdapter);
        holder.r_view.setItemAnimator(new DefaultItemAnimator());
        holder.r_view.setHasFixedSize(true);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bookList.size();
    }
}

