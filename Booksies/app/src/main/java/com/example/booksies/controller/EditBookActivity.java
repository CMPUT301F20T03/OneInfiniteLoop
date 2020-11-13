package com.example.booksies.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.booksies.R;
import com.example.booksies.model.Books;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.Inflater;

public class EditBookActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_UPLOAD = 2;

    View mView;
    Button acceptButton;
    Button cancelButton;
    Button deleteButton;
    ImageButton addPhotoButton;
    ImageView cameraImageView;
    EditText titleEditText;
    EditText authorEditText;
    EditText isbnEditText;
    EditText commentsEditText;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    String currentPhotoPath = "";
    Uri mImageUri;
    CollectionReference collectionReference;
    String downloadableUrl;
    DocumentReference documentReference;
    Intent mIntent;
    String documentID;

    String title;
    String author;
    String isbn;
    String comments;
    String imageURL;



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntent = getIntent();
        documentID = mIntent.getStringExtra("documentID");

        setContentView(R.layout.activity_edit_book);
        acceptButton = findViewById(R.id.accept_button);
        cancelButton = findViewById(R.id.cancel_button);
        deleteButton = findViewById(R.id.delete_button);
        addPhotoButton = findViewById(R.id.addImageButton);
        cameraImageView = findViewById(R.id.cameraImageView);
        titleEditText = findViewById(R.id.titleEditText_edit);
        authorEditText = findViewById(R.id.authorEditText_edit);
        isbnEditText = findViewById(R.id.ISBNEditText_edit);
        commentsEditText = findViewById(R.id.commentEditText_edit);

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Books").document(documentID);

        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        isbn = getIntent().getStringExtra("isbn");
        comments = getIntent().getStringExtra("comments");
        imageURL = getIntent().getStringExtra("imageURL");

        final String original_title = title;
        final String original_author = author;
        final String original_isbn = isbn;
        final String original_comments = comments;
        final String original_imageURL = imageURL;

        titleEditText.setText(title);
        authorEditText.setText(author);
        isbnEditText.setText(isbn);
        commentsEditText.setText(comments);
//        cameraImageView.

        Log.d("EDIT LOG: ", "---------- " + title + " " + author + " " + isbn + " " + comments +  " " + imageURL + " ---------- ");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBookActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleEditText.getText().toString();
                author = authorEditText.getText().toString();
                isbn = isbnEditText.getText().toString();
                comments = commentsEditText.getText().toString();

                documentReference.update("title", title);
                documentReference.update("author", author);
                documentReference.update("isbn", isbn);
                documentReference.update("comment", comments);

                Intent intent = new Intent(EditBookActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.delete();

                Intent intent = new Intent(EditBookActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });


    }
}
