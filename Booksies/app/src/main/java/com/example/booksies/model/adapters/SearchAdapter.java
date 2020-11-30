package com.example.booksies.model.adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksies.R;

import com.example.booksies.controller.MainActivity;
import com.example.booksies.controller.NavigationActivity;
import com.example.booksies.controller.SearchActivity;
import com.example.booksies.controller.ViewProfileActivity;
import com.example.booksies.model.books.Books;


import java.util.ArrayList;

import static com.example.booksies.model.database.FirestoreHandler.addRequest;


/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    public ArrayList<Books> bookList;
    Context context;


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

        public LinearLayout linearLayout;
        public RecyclerView.Adapter mAdapter;
        public Button req_button;


        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            titleView = v.findViewById(R.id.book_name);
            authorView = v.findViewById(R.id.book_author);
            isbnView = v.findViewById(R.id.book_isbn);
            statusView = v.findViewById(R.id.book_status);
            ownerView = v.findViewById(R.id.owner_user_name);
            linearLayout = v.findViewById(R.id.search_layout);
            req_button = v.findViewById(R.id.req_button);




        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAdapter(ArrayList<Books> bookList) {
        this.bookList = bookList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_content, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.titleView.setText(bookList.get(position).getTitle().toUpperCase());
        holder.authorView.setText(bookList.get(position).getAuthor().toUpperCase());
        holder.isbnView.setText(bookList.get(position).getISBN().toUpperCase());
        holder.statusView.setText("AVAILABLE");
        holder.ownerView.setText(bookList.get(position).getOwner());
        holder.ownerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity currentActivity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(currentActivity, ViewProfileActivity.class);
                intent.putExtra("username", bookList.get(position).getOwner());
                currentActivity.startActivity(intent);
            }
        });

        holder.req_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest(bookList.get(position).getDocID());
                String bookName = bookList.get(position).getTitle();
                bookName+=" has been REQUESTED";
                Toast.makeText(v.getContext(),bookName, Toast.LENGTH_SHORT).show();

                AppCompatActivity currentActivity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(currentActivity, NavigationActivity.class);
                intent.putExtra("request", "r");
                currentActivity.startActivity(intent);

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bookList.size();
    }
}

