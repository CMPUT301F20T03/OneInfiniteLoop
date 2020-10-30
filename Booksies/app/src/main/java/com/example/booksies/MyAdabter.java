package com.example.booksies;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is a custom adapter for RecyclerView
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public ArrayList<Books> mDataset;
    //public static ArrayList<Boolean> expandable;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleView;
        public TextView authorView;
        public TextView isbnView;
        public RelativeLayout expand;
        public LinearLayout linearLayout;
        public RecyclerView r_view;
        public RecyclerView.Adapter mAdapter;


        public MyViewHolder(View v) {
            super(v);
            titleView = v.findViewById(R.id.book_name);
            authorView = v.findViewById(R.id.book_author);
            isbnView = v.findViewById(R.id.book_isbn);
            expand = v.findViewById(R.id.expandable_layout);
            r_view = (RecyclerView) v.findViewById(R.id.expand_rlist);
            r_view.setLayoutManager(new LinearLayoutManager(v.getContext()));
            mAdapter = new MyAdapter_Expand(mDataset);
            r_view.setAdapter(mAdapter);
            r_view.setItemAnimator(new DefaultItemAnimator());
            r_view.setHasFixedSize(true);
            linearLayout = v.findViewById(R.id.linear_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Books book = mDataset.get(getAdapterPosition());
                    book.expand = !book.expand;

                    notifyItemChanged(getAdapterPosition());


                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Books> myDataset) {
        mDataset = myDataset;
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
        holder.titleView.setText(mDataset.get(position).getTitle());
        holder.authorView.setText(mDataset.get(position).getAuthor());
        holder.isbnView.setText(mDataset.get(position).getISBN());

        if(!mDataset.get(position).expand)
        holder.expand.setVisibility(View.GONE);
        else
        {
            holder.expand.setVisibility(View.VISIBLE);
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}