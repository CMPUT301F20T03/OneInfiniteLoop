package com.example.booksies.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.booksies.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * This Class allows the user to edit or delete a book from the database
 */
public class EditBookActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_UPLOAD = 2;
    static final int REQUEST_VIEW_IMAGE = 3;

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
    StorageReference storage;
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
    String imageUrl;
    String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/booksies-6aa46.appspot.com/o/images%2Fopen-book-silhouette.jpg?alt=media&token=34b3c0e2-0efc-4a25-aed5-86d9d2f0e230";

    StorageReference photoToDeleteReference;
    String photoToDelete;

    /**
     * creates activity
     * @param savedInstanceState: saved instance state
     */
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

        storage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Books").document(documentID);

        title = mIntent.getStringExtra("title");
        author = mIntent.getStringExtra("author");
        isbn = mIntent.getStringExtra("isbn");
        comments = mIntent.getStringExtra("comment");
        imageUrl = mIntent.getStringExtra("imageURL");

        titleEditText.setText(title);
        authorEditText.setText(author);
        isbnEditText.setText(isbn);
        commentsEditText.setText(comments);

        // are you sure you want to delete the book
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //let calling activity know that image was deleted
                        Intent intent = new Intent(EditBookActivity.this, HomeFragment.class);
                        setResult(RESULT_OK, intent);
                        documentReference.delete();
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        break;
                }
                setResult(RESULT_OK);
            }
        };

        //if there was a photo attached
        if(!imageUrl.equals(defaultImageUrl)) {
            mImageUri = Uri.parse(imageUrl);
            addPhotoButton.setVisibility(View.GONE);
            Glide.with(EditBookActivity.this)
                    .load(imageUrl)
                    .into(cameraImageView);
            cameraImageView.setVisibility(View.VISIBLE);
        }
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(EditBookActivity.this);
            }
        });

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewImageIntent = new Intent(EditBookActivity.this, ViewPhotoActivity.class);
                viewImageIntent.putExtra("imageUrl", mImageUri.toString());
                viewImageIntent.putExtra("previousActivity", "EditBookActivity");
                startActivityForResult(viewImageIntent, REQUEST_VIEW_IMAGE);
            }
        });

        // cancels the editing
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // accepts the editing
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // editable fields
                title = titleEditText.getText().toString();
                author = authorEditText.getText().toString();
                isbn = isbnEditText.getText().toString();
                comments = commentsEditText.getText().toString();
                // if none of the 3 mandatory fields are left blank
                if (!(title.isEmpty() || author.isEmpty() || isbn.isEmpty())) {
                    documentReference.update("title", title);
                    documentReference.update("author", author);
                    documentReference.update("isbn", isbn);
                    documentReference.update("comment", comments);

                    final HashMap<String, String> data = new HashMap<>();
                    //if new picture attached
                    if (mImageUri != null && mImageUri != Uri.parse(imageUrl)) {
                        storage = storage.child("images/"
                                + mImageUri.getLastPathSegment());
                        UploadTask uploadTask = storage.putFile(mImageUri);
                        //Waits for image to be uploaded to storage before adding book to Firestore
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadableUrl = uri.toString();
                                        documentReference.update("imageUrl", downloadableUrl);
                                    }
                                });
                            }
                        });
                    }

                    //no picture attached make imageUrl the default book image from our Storage
                    else {
                        documentReference.update("imageUrl",
                                "https://firebasestorage.googleapis.com/v0/b/booksies-6aa46.appspot.com/o/images%2Fopen-book-silhouette.jpg?alt=media&token=34b3c0e2-0efc-4a25-aed5-86d9d2f0e230");
                    }

                    if(photoToDeleteReference!= null) {
                        photoToDeleteReference.delete();
                    }
                    finish();
                }

                // if missing fields
                else {
                    String string = "";
                    if (title.isEmpty()) {
                        string += "Title, ";
                    }
                    if (author.isEmpty()){
                        string += "Author, ";
                    }
                    if (isbn.isEmpty()) {
                        string += "ISBN ";
                    }
                    string += "cannot be empty";
                    Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditBookActivity.this);
                builder
                        .setMessage("Are you sure you want to delete " + title + "?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }


        });
    }

    /**
     * selects an image
     * @param context: context
     */
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Upload From Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add a photo");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (ActivityCompat.checkSelfPermission(EditBookActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast toast = Toast.makeText(EditBookActivity.this,
                                "Allow camera permissions in app settings to use camera", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(EditBookActivity.this,
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                }

                else if (options[item].equals("Upload From Gallery")) {
                    if (ActivityCompat.checkSelfPermission(EditBookActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast toast = Toast.makeText(EditBookActivity.this,
                                "Allow storage permissions in app settings to use gallery", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, REQUEST_IMAGE_UPLOAD);
                    }
                }

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
      * Function for creating file when taking a picture with camera
      * Also sets the mImageUri to uri of current picture
      * Returns the File object of image
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = EditBookActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();
        mImageUri = Uri.fromFile(new File(currentPhotoPath));
        return image;
    }

    //Gets data from camera intent or from gallery and sets the image into imageView in EditBookActivity

    /**
     *
     * @param requestCode : requested code
     * @param resultCode : the resulted code
     * @param data : the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraImageView = findViewById(R.id.cameraImageView);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    cameraImageView.setImageURI(mImageUri);
                    addPhotoButton.setVisibility(View.GONE);
                    cameraImageView.setVisibility(View.VISIBLE);
                }
            } else if (requestCode == REQUEST_IMAGE_UPLOAD) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri imageUri = data.getData();
                    mImageUri = imageUri;
                    cameraImageView.setImageURI(imageUri);
                    addPhotoButton.setVisibility(View.GONE);
                    cameraImageView.setVisibility(View.VISIBLE);
                }
            } else {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String imageUrl = data.getStringExtra("imageUrl");
                    if (imageUrl.equals("deleted")) {
                        photoToDeleteReference = storage.child(mImageUri.getLastPathSegment());
                        mImageUri = null;
                        addPhotoButton.setVisibility(View.VISIBLE);
                        cameraImageView.setImageURI(null);
                        cameraImageView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}
