package com.example.booksies.model.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.example.booksies.R;
import com.example.booksies.controller.MainActivity;
import com.example.booksies.model.adapters.RequestListAdapter;
import com.example.booksies.model.adapters.SearchAdapter;
import com.example.booksies.model.adapters.BooksListAdapter;
import com.example.booksies.model.books.Books;
import com.example.booksies.model.books.book_status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

/**
 * Firestore handler is a class designed to handle documents obtained from firebase.
 * The class models the documents into objects and arraylists to be used in various other
 * sections.
 */

public class FirestoreHandler {
    // declaring variables
    FirebaseFirestore db;
    ArrayList<Books> booksList = new ArrayList<Books>();
    ArrayList<Books> filteredList = new ArrayList<Books>();
    private ArrayList<Books> searchList = new ArrayList<Books>();

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private String filterString = "NO FILTER";
    private String sortString = "Title";
    private String query;
    static String userID;
    private Context context = null;
    private TextView noResults = null;

    /**
     * Constructor of FirestoreHandler.
     * @param recyclerView Object RecyclerView
     * @param layoutManager Object LayoutManager
     */
    public FirestoreHandler(RecyclerView recyclerView,  RecyclerView.LayoutManager layoutManager){
        this.recyclerView = recyclerView;

        this.layoutManager = layoutManager;

    }

    /**
     * Fetches all owner books from the database
     */
    public void listBooks(){
        db = FirebaseFirestore.getInstance();

        db.collection("Books").whereEqualTo("owner", getCurrentUserEmail())

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            Log.w("error", "Listen failed.", e);
                            return;
                        }
                        booksList.clear();

                        for (QueryDocumentSnapshot book : value) {
                            Books b = new Books(book.getString("isbn").toUpperCase(),
                                    book.getString("author").toUpperCase(),
                                    book.getString("title").toUpperCase());


                            if ((book.getString("status")).toUpperCase().equals("AVAILABLE")){
                                b.setStatus(book_status.AVAILABLE);
                            } else if ((book.getString("status").toUpperCase()).equals("REQUESTED")){
                                b.setStatus(book_status.REQUESTED);

                            } else if((book.getString("status")).toUpperCase().equals("ACCEPTED")){
                                b.setStatus(book_status.ACCEPTED);

                            } else if ((book.getString("status")).toUpperCase().equals("BORROWED")){
                                b.setStatus(book_status.BORROWED);

                            }

                            ArrayList<String> requestList = (ArrayList<String>)book.get("request");

                            if(requestList != null ){
                                if(requestList.size() != 0) {
                                    b.setBookRequests((ArrayList<String>) book.get("request"));
                                    db.collection("Books").document(book.getId()).update("status", "REQUESTED");
                                    b.setStatus(book_status.REQUESTED);
                                }

                                else
                                {
                                    db.collection("Books").document(book.getId()).update("status","AVAILABLE");
                                    b.setStatus(book_status.AVAILABLE);
                                    b.setBookRequests(new ArrayList<String>());
                                }

                            }
                            else
                            {
                                b.setBookRequests(new ArrayList<String>());

                            }

                            if(book.get("borrowerID") != null){
                                b.setBorrower((ArrayList<String>)book.get("borrowerID"));
                            }
                            else
                            {
                                b.setBorrower(new ArrayList<String>());
                            }

                            b.setImageUrl(book.getString("imageUrl"));
                            b.setOwner(book.getString("owner").split("@")[0]);

                            b.setDocID(book.getId());

                            booksList.add(b);

                        }

                        filter();
                        sort();


                    }

                });
    }

    public void clearSearchResults(){
        searchList.clear();
        mAdapter.notifyDataSetChanged();

    }




    /**
     * Performs search of the database using a keyword string
     * @param s keyword string
     */

    public void handleSearch(String s){

        db = FirebaseFirestore.getInstance();
        String owner = getCurrentUserEmail();
        query = s.toLowerCase();
        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();
        Task<QuerySnapshot> q1 = db.collection("Books")
                .whereIn("status", Arrays.asList("AVAILABLE", "REQUESTED"))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        searchList.clear();

                        for (QueryDocumentSnapshot book : task.getResult()) {
                            if (!book.getString("owner").equals(owner)){
                                Books b = new Books(book.getString("isbn").toUpperCase(),
                                        book.getString("author").toUpperCase(),
                                        book.getString("title").toUpperCase());
                                if ((book.getString("status")).toUpperCase().equals("AVAILABLE")){
                                    b.setStatus(book_status.AVAILABLE);
                                } else if ((book.getString("status").toUpperCase()).equals("REQUESTED")){
                                    b.setStatus(book_status.REQUESTED);

                                } else if((book.getString("status")).toUpperCase().equals("ACCEPTED")){
                                    b.setStatus(book_status.ACCEPTED);

                                } else if ((book.getString("status")).toUpperCase().equals("BORROWED")){
                                    b.setStatus(book_status.BORROWED);

                                }
                                b.setOwner(book.getString("owner").split("@")[0]);
                                b.setDocID(book.getId());
                                String textTitle = b.getTitle().toLowerCase();
                                String textISBN = b.getISBN().toLowerCase();
                                String textAuthor = b.getAuthor().toLowerCase();


                                if (textTitle.contains(query)) {
                                    searchList.add(b);
                                }
                                else if (textISBN.contains(query)) {
                                    searchList.add(b);
                                }
                                else if (textAuthor.contains(query)) {
                                    searchList.add(b);
                                }


                                mAdapter.notifyDataSetChanged();

                            }

                        }
                        try{
                            if(searchList.isEmpty()){
                                noResults.setVisibility(View.VISIBLE);
                            } else {
                                noResults.setVisibility(View.GONE);

                            }

                        }catch(Exception e){

                            toastMessage("An Issue occured");


                        }

                        if(searchList.isEmpty()){


                        } else {

                        }
                    } else {
                        Log.d("error", "Error getting documents: ", task.getException());
                    }

                }
            });


        mAdapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }
    private void toastMessage(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    public void setContext(Context context){
        this.context = context;
    }

    public void setNoResults(TextView view){
        this.noResults = view;
    }



    /**
     * Sets filter criteria string
     * @param f filter string
     */
    public void setFilterString(String f){
        this.filterString = f;

    }

    /**
     * gets the filter criteria string
     * @return the filter criteria string
     */
    public String getFilterString(){
        return this.filterString;

    }

    /**
     * Sets the sorting criteria string
     * @param s sort string
     */
    public void setSortString(String s){
        this.sortString = s;
    }

    /**
     * Gets the sorting criteria string
     * @return
     */
    public String getSortString(){
        return this.sortString;

    }

    /**
     * filters results
     */
    public void filter(){
        if(!filterString.equals("NO FILTER")){
            filteredList.clear();
            for (Books book:booksList){
                if ((book.getStatus().toString().toUpperCase()).equals(filterString)){
                    filteredList.add(book);
                }
            }
            mAdapter = new BooksListAdapter(filteredList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

        } else {
            mAdapter = new BooksListAdapter(booksList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

        }

    }

    /**
     * filters requested books by the borrower
     */

    public void reqfilter(){
        if(!filterString.equals("NO FILTER")){
            filteredList.clear();
            for (Books book:booksList){
                if ((book.getStatus().toString().toUpperCase()).equals(filterString)){
                    filteredList.add(book);
                }
            }
            mAdapter = new RequestListAdapter(filteredList);

        } else {
            mAdapter = new RequestListAdapter(booksList);

        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }

    /**
     * sorts list of books based on author, title or isbn
     */

    public void sort (){
        switch (sortString){
            case "Author":
                Comparator<Books> compareById = (Books o1, Books o2) -> o1.getAuthor().compareTo( o2.getAuthor() );

                Collections.sort(booksList, compareById);
                Collections.sort(filteredList, compareById);
                mAdapter.notifyDataSetChanged();
                break;
            case "Title":
                Comparator<Books> compareByTitle = (Books o1, Books o2) -> o1.getTitle().compareTo( o2.getTitle() );

                Collections.sort(booksList, compareByTitle);
                Collections.sort(filteredList, compareByTitle);
                mAdapter.notifyDataSetChanged();
                break;

            case "ISBN":
                Comparator<Books> compareByISBN = (Books o1, Books o2) -> o1.getISBN().compareTo(o2.getISBN());

                Collections.sort(booksList, compareByISBN);
                Collections.sort(filteredList, compareByISBN);
                mAdapter.notifyDataSetChanged();
                break;
        }


    }

    /**
     * Gets logged in user email address
     * @return string of email address
     */
    public static String getCurrentUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {

                String email = user.getEmail();
                return email;

            }
        }
        return "";

    }

    /**
     * Set's id of the current user
     */
    public static void setCurrentUserID()
    {
        String owner = getCurrentUserEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("email",owner)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot user : task.getResult()) {
                            userID = user.getId();
                        }
                    }
                });

    }

    /**
     * Makes request to book
     * @param bookID book document id
     */

    public static void addRequest(String bookID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").document(bookID).update("borrowerID",FieldValue.delete());
        String requests=getCurrentUserEmail();
        db.collection("Books").document(bookID).update("request", FieldValue.arrayUnion(requests));
        db.collection("Books").document(bookID).update("status","REQUESTED");

    }

    /**
     * handles accepting book requests
     * @param requestor the username of the requestor
     * @param bookID the book document id
     */
    public static void acceptRequest(String requestor, String bookID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").document(bookID).update("request",FieldValue.delete());
        db.collection("Books").document(bookID).update("borrowerID", FieldValue.arrayUnion(requestor));
        db.collection("Books").document(bookID).update("status","ACCEPTED");

    }

    /**
     * handles rejecting a book request
     * @param requestor username of the requester
     * @param bookID
     */
    public static void rejectRequest(String requestor, String bookID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").document(bookID).update("request",FieldValue.arrayRemove(requestor));

    }

    /**
     * List books requested
     */

    public void listReqBooks(){
        db = FirebaseFirestore.getInstance();

        db.collection("Books").whereNotEqualTo("owner", getCurrentUserEmail())
                .whereArrayContains("request",getCurrentUserEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            Log.w("error", "Listen failed.", e);
                            return;
                        }
                        booksList.clear();

                        for (QueryDocumentSnapshot book : value) {
                            Books b = new Books(book.getString("isbn").toUpperCase(),
                                    book.getString("author").toUpperCase(),
                                    book.getString("title").toUpperCase());
                            b.setOwner(book.getString("owner"));

                            if ((book.getString("status")).toUpperCase().equals("AVAILABLE")){
                                b.setStatus(book_status.AVAILABLE);
                            } else if ((book.getString("status").toUpperCase()).equals("REQUESTED")){
                                b.setStatus(book_status.REQUESTED);

                            } else if((book.getString("status")).toUpperCase().equals("ACCEPTED")){
                                b.setStatus(book_status.ACCEPTED);

                            } else if ((book.getString("status")).toUpperCase().equals("BORROWED")){
                                b.setStatus(book_status.BORROWED);

                            }


                            b.setImageUrl(book.getString("imageUrl"));
                            b.setOwner(book.getString("owner").split("@")[0]);
                            b.setDocID(book.getId());

                            booksList.add(b);

                        }
                        reqfilter();
                        sort();

                    }

                });
        db.collection("Books").whereNotEqualTo("owner", getCurrentUserEmail())
                .whereArrayContains("borrowerID",getCurrentUserEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            Log.w("error", "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot book : value) {
                            Books b = new Books(book.getString("isbn").toUpperCase(),
                                    book.getString("author").toUpperCase(),
                                    book.getString("title").toUpperCase());
                            b.setOwner(book.getString("owner"));

                            if ((book.getString("status")).toUpperCase().equals("AVAILABLE")){
                                b.setStatus(book_status.AVAILABLE);
                            } else if ((book.getString("status").toUpperCase()).equals("REQUESTED")){
                                b.setStatus(book_status.REQUESTED);

                            } else if((book.getString("status")).toUpperCase().equals("ACCEPTED")){
                                b.setStatus(book_status.ACCEPTED);

                            } else if ((book.getString("status")).toUpperCase().equals("BORROWED")){
                                b.setStatus(book_status.BORROWED);

                            }

                            b.setImageUrl(book.getString("imageUrl"));
                            b.setOwner(book.getString("owner").split("@")[0]);
                            b.setDocID(book.getId());
                            if(book.get("borrowerID") != null) {
                                booksList.add(b);

                            }

                        }
                        reqfilter();
                        sort();

                    }

                });
        


    }

    /**
     * handles adding a location of a book to firestore
     * @param bookId id of the book
     * @param lat latitude to set for the location
     * @param lon longitude to set for the location
     */
    public static void setPickupLocation(String bookId, double lat, double lon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, GeoPoint> data = new HashMap<>();
        data.put("location", new GeoPoint(lat, lon));
        db.collection("Books").document(bookId)
                .set(data, SetOptions.merge());
    }
}
