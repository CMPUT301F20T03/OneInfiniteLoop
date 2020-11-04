package com.example.booksies;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String imageUrl = getIntent().getStringExtra("imageUrl");

        ImageView imageView = findViewById(R.id.imageView);
        Log.d("ImageUrl", imageUrl);
        Glide.with(ViewPhotoActivity.this)
                .load(imageUrl)
                .into(imageView);
    }

    //Go back to whatever opened this activity

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}