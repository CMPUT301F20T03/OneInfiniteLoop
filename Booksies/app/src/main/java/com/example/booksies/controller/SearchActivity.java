package com.example.booksies.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.booksies.model.database.FirestoreHandler;
import com.example.booksies.R;

/**
 * This Class handles searchView and is responsible for querying and returning requested strings
 */
public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView searchRecyclerView;
    TextView noResult;
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
        noResult = (TextView) findViewById(R.id.no_result);
        noResult.setVisibility(View.GONE);
        assert searchRecyclerView != null;
        layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchView.setOnClickListener(new View.OnClickListener() {

            /**
             * Executed on click
             */
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
            FirestoreHandler search = new FirestoreHandler(searchRecyclerView, layoutManager);

            /**
             * Creates new FirestoreHandler to manage search
             * @param s: String to be searched
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                search.handleSearch(s);
                return false;
            }

            /**
             * Runs when query text is changed and handles search for it
             * @param s: String to be searched
             */
            @Override
            public boolean onQueryTextChange(String s) {
                if(!s.equals("")){
                    search.setContext(SearchActivity.this);
                    search.setNoResults(noResult);
                    search.handleSearch(s);
                }
                else search.clearSearchResults();

                return false;
            }
        });
    }
}