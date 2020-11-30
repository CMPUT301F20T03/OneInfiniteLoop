package com.example.booksies.model.database;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static com.example.booksies.model.database.FirestoreHandler.getCurrentUserEmail;

public interface CRUD {
    /**
     * Fetches all owner books from the database
     */
    void listBooks();

    /**
     * Performs search of the database using a keyword string
     * @param s keyword string
     */
    void handleSearch(String s);

    /**
     * List books requested
     */
    void listReqBooks();

    /**
     * Makes request to book
     * @param bookID book document id
     */
    static void addRequest(String bookID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").document(bookID).update("borrowerID", FieldValue.delete());
        String requests= getCurrentUserEmail();
        db.collection("Books").document(bookID).update("request", FieldValue.arrayUnion(requests));
        db.collection("Books").document(bookID).update("status","REQUESTED");

    }

    /**
     * handles accepting book requests
     * @param requestor the username of the requestor
     * @param bookID the book document id
     */
    static void acceptRequest(String requestor, String bookID){
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
    static void rejectRequest(String requestor, String bookID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").document(bookID).update("request",FieldValue.arrayRemove(requestor));

    }

    /**
     * handles adding a location of a book to firestore
     * @param bookId id of the book
     * @param lat latitude to set for the location
     * @param lon longitude to set for the location
     */
    static void setPickupLocation(String bookId, double lat, double lon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, GeoPoint> data = new HashMap<>();
        data.put("location", new GeoPoint(lat, lon));
        db.collection("Books").document(bookId)
                .set(data, SetOptions.merge());
    }


}
