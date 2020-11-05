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
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nullable;

public class UserProfileFragment extends Fragment {

    ImageView userImage;
    TextView username;
    TextView userEmail;
    TextView userPhone;
    Button editProfile;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);

        username = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.email_display);
        userPhone = view.findViewById(R.id.phone_number_display);
        editProfile = view.findViewById(R.id.edit_button);

        //Temp
        username.setText(user.getUsername());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());

        return view;

    }



}
