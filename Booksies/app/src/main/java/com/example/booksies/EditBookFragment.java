package com.example.booksies;

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
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
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

public class EditBookFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_UPLOAD = 2;

    View mView;
    Button addButton;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.activity_add_book, container, false);

        addButton = mView.findViewById(R.id.addButton);
        cancelButton = mView.findViewById(R.id.cancelButton);
        deleteButton = mView.findViewById(R.id.deleteButton);
        addPhotoButton = mView.findViewById(R.id.addImageButton);
        cameraImageView = mView.findViewById(R.id.cameraImageView);
        titleEditText = mView.findViewById(R.id.titleEditText);
        authorEditText = mView.findViewById(R.id.authorEditText);
        isbnEditText = mView.findViewById(R.id.ISBNEditText);
        commentsEditText = mView.findViewById(R.id.commentEditText);

        //initialize Firestore and storage variables
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Books");
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Button to add photo leads to a dialog to choose how to upload image
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(getActivity());
            }
        });

        //add button gets all attributes of book and adds to Cloud Firestore
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookToFirestore();
            }
        });

        //cancel button goes back to activity or fragment that called it
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager().getBackStackEntryCount() == 0) {
                    getActivity().finish();
                }
                else {
                    getFragmentManager().popBackStack();
                }
            }
        });
        return mView;
    }

    //Gets data from camera intent or from gallery and sets the image into imageView in AddBookFragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RequestCode", Integer.toString(requestCode));
        cameraImageView = mView.findViewById(R.id.cameraImageView);
        if (resultCode != Activity.RESULT_CANCELED) {
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    addPhotoButton.setVisibility(View.INVISIBLE);
                    cameraImageView.setVisibility(View.VISIBLE);
                    cameraImageView.setImageBitmap(imageBitmap);
                    Log.d("photopath", currentPhotoPath);
                }
            }
            else {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri imageUri = data.getData();
                    addPhotoButton.setVisibility(View.INVISIBLE);
                    cameraImageView.setVisibility(View.VISIBLE);
                    cameraImageView.setImageURI(imageUri);
                    mImageUri = imageUri;
                    Log.d("photopath", mImageUri.toString());
                }
            }
        }
    }



    //Dialog for choosing between upload or take photo
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Upload From Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add a photo");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                            PackageManager.FEATURE_CAMERA)) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                }

                else if (options[item].equals("Upload From Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, REQUEST_IMAGE_UPLOAD);

                }

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     *Function for creating file when taking a picture with camera
     * Also sets the currentPhotoPath variable to the path of file
     * @return the image file
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Function for adding a book to Firestore
     * Puts into a document called Books with fields:
     * title, author, isbn, comment, owner, status, and imageUrl
     */
    private void addBookToFirestore() {
        //final String currentUserId = mAuth.getCurrentUser().getUid();
        //This is Temporary
        final String currentUserId = "JackyH56";

        final String titleStr = titleEditText.getText().toString();
        final String authorStr = authorEditText.getText().toString();
        final String isbnStr = isbnEditText.getText().toString();
        String commentStr = commentsEditText.getText().toString();
        final HashMap<String, String> data = new HashMap<>();
        if (titleStr.length() > 0 && authorStr.length() > 0 && isbnStr.length() > 0) {
            data.put("title", titleStr);
            data.put("author", authorStr);
            data.put("isbn", isbnStr);
            data.put("status", "AVAILABLE");
            data.put("comment", commentStr);
            data.put("owner", currentUserId);
        } else {
            Toast toast = Toast.makeText(getActivity(), "Adding a book requires\n Title, Author and ISBN", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        //if picture attached
        if (currentPhotoPath.length() != 0 || mImageUri != null) {
            if (currentPhotoPath.length() > 0) {
                mImageUri = Uri.fromFile(new File(currentPhotoPath));
            }
            final StorageReference storage = storageReference.child("images/" + mImageUri.getLastPathSegment());
            UploadTask uploadTask = storage.putFile(mImageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadableUrl = uri.toString();
                            data.put("imageUrl", downloadableUrl);
                            collectionReference
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("BookAdd", "Book added successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("BookAdd", "Failed to add book");
                                        }
                                    });
                        }
                    });
                }
            });
        }

        //no picture attached make imageUrl the default book image from our Storage
        else {
            data.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/booksies-6aa46.appspot.com/o/images%2Fopen-book-silhouette.jpg?alt=media&token=34b3c0e2-0efc-4a25-aed5-86d9d2f0e230");
            collectionReference
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("BookAdd", "Book added successfully");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("BookAdd", "Failed to add book");

                        }
                    });
        }
        //Go back to whatever called this fragment
        getFragmentManager().popBackStack();
    }


    public void updateBook(Books newBook) {
        collectionReference.document();
    }

}
