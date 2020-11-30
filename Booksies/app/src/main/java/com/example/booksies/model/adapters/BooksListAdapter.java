package com.example.booksies.model.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.booksies.R;

import com.example.booksies.controller.EditBookActivity;
import com.example.booksies.model.books.Books;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;


/**
 * This class is a custom adapter for RecyclerView
 * designed to handle a list of book objects.
 * @author: Alireza Azimi (sazimi)
 * Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BooksListViewHolder> {
    public ArrayList<Books> bookList;
    //public static ArrayList<Boolean> expandable;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;

    FirebaseFirestore db;


    /**
     * Inetranl class designed to model the view holder
     * for recycler view elements
     */
    public class BooksListViewHolder extends RecyclerView.ViewHolder {
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

        /**
         * constructor of view holder class
         * @param v view object
         */

        public BooksListViewHolder(View v) {
            super(v);
            context = v.getContext();
            titleView = v.findViewById(R.id.book_name);
            authorView = v.findViewById(R.id.book_author);
            isbnView = v.findViewById(R.id.book_isbn);
            statusView = v.findViewById(R.id.book_status);
            expand = v.findViewById(R.id.expandable_layout);
            r_view = v.findViewById(R.id.expand_rlist);
            r_view.setLayoutManager(new LinearLayoutManager(v.getContext()));

            imageView = v.findViewById(R.id.book_image);
            linearLayout = v.findViewById(R.id.linear_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Books book = bookList.get(getAdapterPosition());
                    book.expand = !book.expand;

                    notifyItemChanged(getAdapterPosition());
                }
            });


            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Books book = bookList.get(getAdapterPosition());
                    db = FirebaseFirestore.getInstance();
                    db.collection("Books").document(book.getDocID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {

                                Bundle docID = new Bundle();
                                docID.putString("documentID", book.getDocID());
                                docID.putString("title", book.getTitle());
                                docID.putString("author", book.getAuthor());
                                docID.putString("isbn", book.getISBN());
                                docID.putString("comment", documentSnapshot.getString("comment"));
                                docID.putString("imageURL", book.getImageUrl());

                                Intent intent = new Intent(context.getApplicationContext(), EditBookActivity.class).putExtras(docID);
                                context.startActivity(intent);

                            } else {
                                Log.d("SOME TAG", "-----------------------------No such document");
                            }
                        }
                    });
                    return true;
                }
            });

        }
    }


    /**
     * Constructor of adapter
     * @param bookList list of book objects
     */
    public BooksListAdapter(ArrayList<Books> bookList) {
        this.bookList = bookList;

    }


    /**
     * Create view holder for list item
     * @param parent parent vieew
     * @param viewType type of view
     * @return View holder for list of books
     */
    @Override
    public BooksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home, parent, false);

        BooksListViewHolder vh = new BooksListViewHolder(v);
        return vh;
    }


    /**
     * Bind's the view holder
     * @param holder View holder of books list
     * @param position position of item
     */
    @Override
    public void onBindViewHolder(BooksListViewHolder holder, int position) {
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
        if(bookList.get(position).getStatus().toString().toUpperCase().equals("REQUESTED")) {
            holder.mAdapter = new BooksListAdapter_Expand(bookList.get(position).getBookRequests(), bookList.get(position).getDocID());
            holder.r_view.setAdapter(holder.mAdapter);
            holder.r_view.setItemAnimator(new DefaultItemAnimator());
            holder.r_view.setHasFixedSize(true);
        }
        else
        {
            holder.mAdapter = new Map_Adapter(bookList.get(position).getBorrower(),bookList.get(position).getDocID());
            holder.r_view.setAdapter(holder.mAdapter);
            holder.r_view.setItemAnimator(new DefaultItemAnimator());
            holder.r_view.setHasFixedSize(true);
        }

    }


    /**
     * get the number of items in recycler view
     * @return number of items in recycler view
     */
    @Override
    public int getItemCount() {
        return bookList.size();
    }
}

