/*
* implements US 08.02.01 and US 08.03.01
 */


package com.example.booksies.controller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.booksies.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This class allows you to view a full sized image of a photo taken and allows you to delete it
 */
public class ViewPhotoActivity extends AppCompatActivity {
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Gets imageUrl either from addBookFragment or EditBookActivity
        imageUrl = getIntent().getStringExtra("imageUrl");
        ImageView imageView = findViewById(R.id.imageView);

        //Load image onto imageview using the imageUrl passed in
        Glide.with(ViewPhotoActivity.this)
                .load(imageUrl)
                .into(imageView);

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //let calling activity know that image was deleted
                        Intent intent = new Intent(ViewPhotoActivity.this, AddBookFragment.class);
                        intent.putExtra("imageUrl", "deleted");
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        break;
                }
                setResult(RESULT_OK);
            }
        };

        //Click listener for delete button that opens up a alert dialog
        FloatingActionButton deleteButton = findViewById(R.id.deleteFloatingActionButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewPhotoActivity.this);
                builder
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }
        });
    }

    //Go back to whatever opened this activity
    @Override
    public boolean onSupportNavigateUp() {
        final String previousActivity = getIntent().getStringExtra("previousActivity");
        Intent intent;
        if (previousActivity.equals("AddBookFragment")) {
            intent = new Intent(ViewPhotoActivity.this, AddBookFragment.class);
        }
        else {
            intent = new Intent(ViewPhotoActivity.this, EditBookActivity.class);
        }
        intent.putExtra("imageUrl", imageUrl);
        finish();
        return true;
    }
}