package com.example.booksies.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * ScanFragment opens activity_scan_books layout which is linked to the Scanner Button in Navigation tab.
 * ScanFragment handles all the buttons related to this layout and it handles functions related
 * to retrieving ISBN code and using it to verify if an owner or borrower have successfully
 * exchanged books or not. ScanFragment extends to Fragment.
 */
public class ScanFragment extends Fragment {

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

//        db = FirebaseFirestore.getInstance();
//        collectionReference = db.collection("Books");
//        mAuth = FirebaseAuth.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReference();

        getBookDescription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scanOnClick(context);
            }
        });

        borrowBookByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scanOnClick(context);
            }
        });


        returnBookByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scanOnClick(context);
            }
        });

        lendBookByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scanOnClick(context);
            }
        });

        acceptReturnByScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
        Intent intent = new Intent(context, ScanActivity.class);
        startActivityForResult(intent, ScanActivity.SCAN);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Book Description");
                    builder.setMessage("ISBN is " + data.getStringExtra("ISBN"))
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
    }

}