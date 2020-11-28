package com.example.booksies.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.booksies.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This Class handles the Home view
 */
public class NavigationActivity extends AppCompatActivity {


    /**
     * Responsible for creating activity when first launched
     * @param savedInstanceState: savedInstanceState is a reference to a Bundle object passed into the onCreate method
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Log.d("EDITBOOK", "----------------------------------------EDIT BOOK----------------------------------------");
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_navigation); // handles switch for bottom navigation

        Fragment frag;
        if(getIntent().getStringExtra("request")!= null)
        {
            frag = new RequestListFragment();

        }
        else
        {
            frag =new HomeFragment();

        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).addToBackStack(null).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    /**
                     * Responsible for creating new fragments and setting new layouts when a
                     * specific menu item is selected
                     * @param item: A MenuItem which is part of the NavigationActivity at the bottom of the ui
                     */
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                HomeFragment frag = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).addToBackStack(null).commit();
                                break;
                            case R.id.action_request:
                                RequestListFragment reqFrag = new RequestListFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container,reqFrag)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.action_add_book:
                                AddBookFragment addBookFrag = new AddBookFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, addBookFrag)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.action_profile:
                                UserProfileFragment userProfileFragment = new UserProfileFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, userProfileFragment)
                                        .addToBackStack(null).commit();
                                break;
                            case R.id.action_scanner:
                                ScanFragment scanFragment = new ScanFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, scanFragment)
                                        .addToBackStack(null).commit();
                                break;
                        }
                        return true;
                    }
                });





    }

    /**
     * onCreateOptionsMenu is useful for creating a new Menu.
     * @param menu: A Menu object that is used to create Navigation Activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * If Menu Item and in this case action_logout is selected, the instance of FirebaseAuth is
     * signed out and this logs out the current user and a new user can thus login.
     * @param item: A MenuItem at the top of user interface
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.action_logout:   //this item has your app icon
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:

                break;


        }
        return true;
    }



}
