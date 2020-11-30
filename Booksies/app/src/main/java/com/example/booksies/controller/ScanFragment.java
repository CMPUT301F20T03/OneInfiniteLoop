package com.example.booksies.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.example.booksies.model.books.Books;
import com.example.booksies.model.books.book_status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.booksies.model.database.FirestoreHandler;
import com.google.android.gms.tasks.OnCompleteListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * ScanFragment opens activity_scan_books layout which is linked to the Scanner Button in Navigation tab.
 * ScanFragment handles all the buttons related to this layout and it handles functions related
 * to retrieving ISBN code and using it to verify if an owner or borrower have successfully
 * exchanged books or not. ScanFragment extends to Fragment.
 */
public class ScanFragment extends Fragment {

    String buttonClickVal = "none";
    Button getBookDescription;
    Button borrowBookByScanning;
    Button returnBookByScanning;
    Button lendBookByScanning;
    Button acceptReturnByScanning;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    CollectionReference collectionReference;
    Context context;
    Boolean bookFoundInDB = false;
    FirestoreHandler f;

    /**
     * Set content view and context and set on Click Listeners for various buttons in
     * activty_scan_books layout.
     * @param savedInstanceState: saved instance state
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_scan_books, container, false);
        getBookDescription = (Button) view.findViewById(R.id.getBookDescription);
        borrowBookByScanning = (Button) view.findViewById(R.id.borrowBookByScanning);
        returnBookByScanning = (Button) view.findViewById(R.id.returnBookByScanning);
        lendBookByScanning = (Button) view.findViewById(R.id.lendBookByScanning);
        acceptReturnByScanning = (Button) view.findViewById(R.id.acceptReturnByScanning);
        context = view.getContext();

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Books");
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        getBookDescription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonClickVal = "getBookDescription";
                scanOnClick(context);
            }
        });

        borrowBookByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // borrower
                buttonClickVal = "borrowBook";
                scanOnClick(context);
            }
        });


        returnBookByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // borrower
                buttonClickVal = "returnBook";
                scanOnClick(context);
            }
        });

        lendBookByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // owner
                buttonClickVal = "lendBook";
                scanOnClick(context);
            }
        });

        acceptReturnByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // owner
                buttonClickVal = "acceptReturn";
                scanOnClick(context);
            }
        });

        return view;
    }

    /**
     * Create an intent and also use ScanActivity to scan ISBN code and retrive ISBN code
     * information.
     * @param context: context
     */
    public void scanOnClick(Context context) {
        Intent intent = new Intent(context, Scanner.class);
        startActivityForResult(intent, ScanActivity.SCAN);
    }

    private void toastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Run when Scanner Activity returns ISBN code after scanning
     * @param requestCode: requested Code to know about the request made
     * @param resultCode: result code which shows if a result was OK or NOT or CANCELLED
     * @param data: intent given
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if(requestCode == ScanActivity.SCAN) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    db.collection("Books")
                            .whereEqualTo("isbn", data.getStringExtra("ISBN"))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot book : task.getResult()) {
                                            bookFoundInDB = true;
                                            String documentID = book.getReference().getId();
                                            Books b = new Books(book.getString("isbn").toUpperCase(),
                                                    book.getString("author").toUpperCase(),
                                                    book.getString("title").toUpperCase());
                                            b.setOwner(book.getString("owner"));
                                            Boolean validateVal;
                                            //if get book description and book is in database return book description
                                            if (buttonClickVal.equals("getBookDescription")) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Book Description");
                                                builder.setMessage("Title: " + book.getString("title").toUpperCase()
                                                        + "\nAuthor: " + book.getString("author").toUpperCase()
                                                        + "\nISBN: " + book.getString("isbn").toUpperCase())
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                            else {
                                                if (book.getBoolean("validate") != null) {
                                                    validateVal = book.getBoolean("validate");
                                                }
                                                else {
                                                    validateVal = false;
                                                }

                                                if (!book.getString("status").toUpperCase().equals("ACCEPTED")
                                                        && !book.getString("status").toUpperCase().equals("BORROWED")) {
                                                    toastMessage("Book needs to be accepted or borrowed to be able to scan");
                                                }
                                                else {
                                                    String currentAccount = user.getEmail();

                                                    String borrower = ((ArrayList<String>) book.get("borrowerID")).get(0);
                                                    //if lending
                                                    if (book.getString("status").equals("ACCEPTED")
                                                            && buttonClickVal.equals("lendBook")
                                                            && book.get("owner").equals(currentAccount)
                                                            && validateVal.equals(false)) {
                                                        db.collection("Books").document(documentID).update("validate", true);
                                                        buttonClickVal = "none";
                                                        toastMessage(currentAccount.split("@")[0] + " " + "has verified that he / she is lending");
                                                    }
                                                    //if borrowing
                                                    else if ((book.getString("status").toUpperCase()).equals("ACCEPTED")
                                                            & buttonClickVal.equals("borrowBook") & validateVal.equals(true)
                                                            & currentAccount.equals(borrower)) {
                                                        b.setStatus(book_status.BORROWED);
                                                        db.collection("Books").document(documentID).update("status", "BORROWED");
                                                        db.collection("Books").document(documentID).update("validate", false);
                                                        buttonClickVal = "none";
                                                        toastMessage(currentAccount.split("@")[0] + " " + " has verified that he / she has borrowed");
                                                    }
                                                    //if returning
                                                    else if ((book.getString("status")).toUpperCase().equals("BORROWED")
                                                            & buttonClickVal.equals("returnBook") & validateVal.equals(false)
                                                            & currentAccount.equals(borrower)) {
                                                        db.collection("Books").document(documentID).update("validate", true);
                                                        buttonClickVal = "none";
                                                        toastMessage(currentAccount.split("@")[0] + " " + " has verified that he / she is returning book");
                                                    }
                                                    //if accepting return
                                                    else if ((book.getString("status")).toUpperCase().equals("BORROWED")
                                                            & buttonClickVal.equals("acceptReturn") & validateVal.equals(true)
                                                            & book.get("owner").equals(currentAccount)) {
                                                        b.setStatus(book_status.AVAILABLE);
                                                        db.collection("Books").document(documentID).update("status", "AVAILABLE");
                                                        db.collection("Books").document(documentID).update("validate", false);
                                                        db.collection("Books").document(documentID).update("borrowerID", FieldValue.delete());
                                                        db.collection("Books").document(documentID).update("location", FieldValue.delete());
                                                        buttonClickVal = "none";
                                                        toastMessage(currentAccount.split("@")[0] + " " + " has verified that he / she has received book");
                                                    }
                                                    else {
                                                        toastMessage("Something went wrong\nLend > Borrow > Return > Accept");
                                                    }
                                                }
                                            }
                                        }
                                        //If get book description and book is not in database just show isbn
                                            if(buttonClickVal.equals("getBookDescription") &&
                                                !bookFoundInDB) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("Book Description");
                                            builder.setMessage(data.getStringExtra("ISBN"))
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    }
                                }
                            });
                    }
                }
            }
        }
    }