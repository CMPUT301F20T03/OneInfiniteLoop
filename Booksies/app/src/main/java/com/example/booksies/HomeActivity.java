package com.example.booksies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the home view
 */
public class HomeActivity extends AppCompatActivity {
    ArrayList<String> BooksData = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String[] books = {"War and Peace", "Les Miserable","SCIENCE","MATH", "Physics", "History"
                          ,"The three musketeers", "TWD","Calculus","Statistics"}; // test data for recycler view
        BooksData.addAll(Arrays.asList(books)); // add all books to ArrayList

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_navigation); // handles switch for bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:

                            case R.id.action_request:
                                break;
                            case R.id.action_add_book:
                                //replace another fragment
                                break;
                            case R.id.action_profile:
                                break;
                            case R.id.action_scanner:
                                break;
                        }
                        return true;
                    }
                });

        // setting up recycler view
        recyclerView =(RecyclerView) findViewById(R.id.book_list);
        assert recyclerView != null;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(BooksData);
        recyclerView.setAdapter(mAdapter);


    }


}
