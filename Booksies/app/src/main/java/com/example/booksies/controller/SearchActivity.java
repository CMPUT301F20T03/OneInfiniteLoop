package com.example.booksies.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;

import com.example.booksies.model.Books;
import com.example.booksies.model.FirestoreHandler;
import com.example.booksies.model.MyAdapter;
import com.example.booksies.R;
import com.example.booksies.model.book_status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Class handles searchView and is responsible for querying and returning requested strings
 */
public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView searchRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Responsible for creating activity when first launched
     * @param savedInstanceState: savedInstanceState is a reference to a Bundle object passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.onActionViewExpanded();
        searchRecyclerView =(RecyclerView) findViewById(R.id.search_list);
        assert searchRecyclerView != null;
        layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            /**
             * Executed on close
             */

            @Override
            public boolean onClose() {

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * Creates new FirestoreHandler to manage search
             * @param s: String to be searched
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                FirestoreHandler search = new FirestoreHandler(searchRecyclerView, layoutManager);
                search.handleSearch(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



    }

}