package com.example.booksies.model.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
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
 *
 */

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.MyViewHolder> {
    public ArrayList<Books> bookList;
    //public static ArrayList<Boolean> expandable;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;
    String url;

    // for swiping
    private ColorDrawable swipeBackgroundDelete = new ColorDrawable(Color.parseColor("#FF0000"));
    private ColorDrawable swipeBackgroundEdit = new ColorDrawable(Color.parseColor("#30BEFF"));
    private Drawable deleteIcon;
    private Drawable editIcon;
    FirebaseFirestore db;
//    private OnItemSwipeListener listener;


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

//            linearLayout = v.findViewById(R.id.linear_layout);
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

//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION && listener != null) {
//                        listener.onItemSwipe(getSnapshots().getSnapshot(position), position);
//                    }
//                }
//            });
        }
    }

//    public interface OnItemSwipeListener {
//        void onItemSwipe(DocumentSnapshot documentSnapshot, int position);
//    }
//
//    public void setOnItemSwipeListener(OnItemSwipeListener listener) {
//        this.listener = listener;
//    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BooksListAdapter(ArrayList<Books> bookList) {
        this.bookList = bookList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public BooksListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

//     Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            BooksList.remove(viewHolder.getAdapterPosition());
//            mAdapter.notifyDataSetChanged();

            int position = viewHolder.getAdapterPosition();
            db = FirebaseFirestore.getInstance();
            String id = db.collection("Books").getId();
            Toast.makeText(context.getApplicationContext(), "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();

        }

        // TODO: Need to clean this functionality up to look better
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            int rectWidth = 240;//itemView.getTop() - itemView.getBottom();
            int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight())/2;
            int editIconMargin = (itemView.getHeight() - editIcon.getIntrinsicHeight())/2;
            swipeBackgroundDelete.setBounds(itemView.getLeft(), itemView.getTop(), rectWidth, itemView.getBottom());
            swipeBackgroundEdit.setBounds(itemView.getLeft() + rectWidth, itemView.getTop(), rectWidth * 2, itemView.getBottom());

            deleteIcon.setBounds(itemView.getLeft() + deleteIconMargin, itemView.getTop() + deleteIconMargin, itemView.getLeft() +
                    deleteIconMargin + deleteIcon.getIntrinsicWidth(), itemView.getBottom() - deleteIconMargin );
            editIcon.setBounds(itemView.getLeft() + rectWidth + editIconMargin, itemView.getTop() + editIconMargin, itemView.getLeft() + rectWidth +
                    editIconMargin + deleteIcon.getIntrinsicWidth(), itemView.getBottom() - editIconMargin );

            swipeBackgroundDelete.draw(c);
            swipeBackgroundEdit.draw(c);
            c.save();
            c.clipRect(itemView.getLeft(), itemView.getTop(), rectWidth, itemView.getBottom());
            c.restore();
            deleteIcon.draw(c);
            editIcon.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}

