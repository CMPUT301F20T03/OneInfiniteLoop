package com.example.booksies.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksies.model.Books;
import com.example.booksies.model.FirestoreHandler;
import com.example.booksies.model.MyAdapter;
import com.example.booksies.R;

/**
 * This Class has a Recycler View and is responsible for showing all the books that are available,
 * requested, accepted and borrowed
 */
public class HomeFragment extends Fragment {

    private  RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirestoreHandler f;
    SearchView searchView;
    View view;


    /**
     * Responsible for creating view when first launched
     * @param inflater: inflater is responsible for converting layout to view objects
     * @param container: It is a ViewGroup object
     * @param savedInstanceState: savedInstanceState is a reference to a Bundle object passed into the onCreate method
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.book_list);

        assert recyclerView != null;
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        searchView = (SearchView) view.findViewById(R.id.search_bar);


        return view;

    }

    /**
     * It is useful for modifying UI elements.
     * @param savedInstanceState: savedInstanceState is a reference to a Bundle object passed into the onCreate method
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        f = new FirestoreHandler(recyclerView,  layoutManager);
        f.listBooks();

        Spinner spinnerFilter = (Spinner) view.findViewById(R.id.filter);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_items, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFilter.setAdapter(adapterFilter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                f.setFilterString(parentView.getItemAtPosition(position).toString().toUpperCase());
                f.filter();;

            }

            /**
             * Executed when nothing is selected
             * @param parentView: It is the parent view
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });


        Spinner spinnerSort = (Spinner) view.findViewById(R.id.sort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_items, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSort.setAdapter(adapterSort);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * It is run when an Item is selected in HomeFragment.
             * @param parentView: It is the parent view
             * @param selectedItemView: It is the selected Item view
             * @param position: It is useful for getting item at a certain position
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                f.setSortString(parentView.getItemAtPosition(position).toString());
                f.sort();

            }

            /**
             * Executed when nothing is selected
             * @param parentView: It is the parent view
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        Intent intent = new Intent(getActivity(), SearchActivity.class);
        searchView.setOnClickListener(new View.OnClickListener() {
            /**
             * Executed on click
             * @param v: It is a View object
             */
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                searchView.setIconifiedByDefault(false);
                searchView.clearFocus();

                startActivity(intent);


            }
        });



    }


}