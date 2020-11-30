package com.example.booksies.model.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.booksies.R;

import com.example.booksies.controller.ViewProfileActivity;
import com.example.booksies.controller.ViewMapsActivity;
import com.example.booksies.model.books.Books;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;

import static com.example.booksies.model.database.FirestoreHandler.getCurrentUserEmail;

//Acknowledgement: https://developer.android.com/guide/topics/ui/layout/recyclerview

/**
 * This class is a custom adapter for RecyclerView
 *
 */
public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.RequestListViewHolder> {
    public ArrayList<Books> bookList;
    Context context;

    /**
     * RequestListViewHolder extends to RecyclerView.ViewHolder
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public class RequestListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleView;
        public TextView authorView;
        public TextView isbnView;
        public TextView statusView;
        public TextView ownerView;
        public ImageView imageView;
        public FloatingActionButton map2;
        public FloatingActionButton delete_req;

        /**
         * Constructor for RequestListViewHolder and helps in finding views by id
         * for titleView, authorView, isbnView, statusView, ownerView
         * @param v: view to get context for and find related layout items
         */
        public RequestListViewHolder(View v) {
            super(v);
            context = v.getContext();
            titleView = v.findViewById(R.id.title_req);
            authorView = v.findViewById(R.id.author_req);
            isbnView = v.findViewById(R.id.isbn_req);
            statusView = v.findViewById(R.id.status_req);
            ownerView = v.findViewById(R.id.owner);
            imageView = (ImageView) v.findViewById(R.id.book_image);
            map2 = v.findViewById(R.id.map2);
            delete_req = v.findViewById(R.id.delete_req);
        }
    }

    /**
     * Helps to set bookList
     * @param bookList: bookList is an array list of books
     */
    public RequestListAdapter(ArrayList<Books> bookList) {
        this.bookList = bookList;
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent: instance of ViewGroup and used for finding parent
     * @param viewType: viewType
     * @return : vh
     */
    @Override
    public RequestListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_request, parent, false);

        RequestListViewHolder vh = new RequestListViewHolder(v);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder: instance of ViewGroup and used for finding parent
     * @param position: position to get book list
     */
    @Override
    public void onBindViewHolder(RequestListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleView.setText(bookList.get(position).getTitle());
        holder.authorView.setText(bookList.get(position).getAuthor());
        holder.isbnView.setText(bookList.get(position).getISBN());
        holder.statusView.setText(bookList.get(position).getStatus().toString().toLowerCase());
        holder.ownerView.setText(bookList.get(position).getOwner());

        // making owner name clickable
        holder.ownerView.setOnClickListener(new View.OnClickListener() {
            /**
             * When onClick for holder.ownerView is clicked
             * @param view: view
             */
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
        holder.map2.setOnClickListener(new View.OnClickListener() {
            /**
             * When onClick for holder.map2 is clicked
             * @param v: view to get context for
             */
            @Override
            public void onClick(View v) {
                AppCompatActivity currentActivity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(currentActivity, ViewMapsActivity.class);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Books").document(bookList.get(position).getDocID());

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    /**
                     * On task complete
                     * @param task: instance of Task<DocumentSnapshot> to get result
                     */
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            //If the book has a location already, then pass in the lat lng
                            if (documentSnapshot.getGeoPoint("location") != null) {
                                GeoPoint geopoint = documentSnapshot.getGeoPoint("location");
                                intent.putExtra("lat", geopoint.getLatitude());
                                intent.putExtra("lon", geopoint.getLongitude());
                                currentActivity.startActivity(intent);
                            }
                            else {
                                Toast.makeText(currentActivity,"Owner has not set location yet",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        if(!bookList.get(position).getStatus().toString().toUpperCase().equals("REQUESTED"))
        {
            holder.delete_req.setVisibility(View.GONE);
        }
        holder.delete_req.setOnClickListener(new View.OnClickListener() {
            /**
             * When holder.delete_req is clicked
             * @param v: Instance of View to get context for
             */
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Books").document(bookList.get(position).getDocID()).update("request",FieldValue.arrayRemove(getCurrentUserEmail()));
                Activity activity = (Activity) v.getContext();
                View action = activity.findViewById(R.id.action_request);
                action.performClick();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    /**
     * When holder.delete_req is clicked
     * @return : bookList.size()
     */
    public int getItemCount() {
        return bookList.size();
    }
}

