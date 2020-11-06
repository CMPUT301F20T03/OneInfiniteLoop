package com.example.booksies.controller;

import android.app.Activity;
import android.content.Context;
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

import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ScanFragment extends Fragment {

    Button getBookDescription;
    Button borrowBookByScanning;
    Button returnBookByScanning;
    Button lendBookByScanning;
    Button acceptReturnByScanning;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    String currentPhotoPath = "";
    Uri mImageUri;
    CollectionReference collectionReference;
    String downloadableUrl;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_scan_books, container, false);
        Button info = (Button) view.findViewById(R.id.getBookDescription);
        context = view.getContext();
        //initialize Firestore and storage variables
//        db = FirebaseFirestore.getInstance();
//        collectionReference = db.collection("Books");
//        mAuth = FirebaseAuth.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReference();

//        Button to add photo leads to a dialog to choose how to upload image
//        getBookDescription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                scanOnClick(getActivity());
//            }
//        });

        info.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                scanOnClick(context);
            }
        });
        return view;




    }

    public void scanOnClick(Context context) {
        Intent intent = new Intent(context, ScanActivity.class);
        startActivity(intent);
    }

//    //Gets data from camera intent or from gallery and sets the image into imageView in AddBookFragment
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("RequestCode", Integer.toString(requestCode));
//        if (resultCode != Activity.RESULT_CANCELED) {
//            if(requestCode == ScanActivity.SCAN) {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    final String TAG = ScanFragment.class.getSimpleName();
//                    Log.d(TAG, "onSuccess: NICE");
//                }
//            }
//        }
//    }

}