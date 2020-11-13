/*
* This activity gets the imageUrl from the extras in the intent and loads the image onto a full
* screen image view
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

public class ViewPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final String imageUrl = getIntent().getStringExtra("imageUrl");

        ImageView imageView = findViewById(R.id.imageView);
        Log.d("ImageUrl", imageUrl);
        Glide.with(ViewPhotoActivity.this)
                .load(imageUrl)
                .into(imageView);

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(ViewPhotoActivity.this, AddBookFragment.class);
                        intent.putExtra("imageUrl", "");
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
        finish();
        return true;
    }
}