package com.example.booksies;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nullable;

public class UserProfileActivity extends AppCompatActivity {

    ImageView userImage;
    TextView username;
    TextView userEmail;
    TextView userPhone;
    Button editProfile;


    @Override
    protected void onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_user_profile);

        username = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.email_display);
        userPhone = findViewById(R.id.phone_number_display);
        editProfile = findViewById(R.id.edit_button);

        Intent intent = getIntent();
        final User owner = intent.getParcelableExtra("owner");
        username.setText(owner.getUsername());
        userEmail.setText(owner.getEmail());
        userPhone.setText(owner.getPhone());






    }



}
