package com.example.booksies.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksies.model.database.FirestoreHandler;
import com.example.booksies.R;

/**
 * This class is a recycler view that shows the requests on a book
 */
public class RequestListFragment extends Fragment {

    private  RecyclerView recyclerViewReq;
    private RecyclerView.LayoutManager layoutManager;
    FirestoreHandler f;
    View view;

    /**
     * Set content view and context and set onClick Listeners for various buttons in
     * fragment_requestlist layout.
     * @param inflater: inflater
     * @param container: container
     * @param savedInstanceState: saved instance state
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_requestlist, container, false);
        recyclerViewReq =(RecyclerView) view.findViewById(R.id.req_list);

        assert recyclerViewReq != null;
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewReq.setLayoutManager(layoutManager);
        return view;
    }

    /**
     * Run on Activity created
     * @param savedInstanceState: saved instance state
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        f = new FirestoreHandler(recyclerViewReq,  layoutManager);
        f.listReqBooks();

        Spinner spinnerFilter = (Spinner) view.findViewById(R.id.reqfilter);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(getActivity(),
                R.array.req_filter_items, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFilter.setAdapter(adapterFilter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Run when item is selected
             * @param parentView: parent view of current view
             * @param selectedItemView: selected item view
             * @param position: position of item
             * @param id: id of item
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                f.setFilterString(parentView.getItemAtPosition(position).toString().toUpperCase());
                f.reqfilter();
            }

            /**
             * Run when nothing is selected
             * @param parentView: parent view of current view
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
             * Run when item is selected
             * @param parentView: parent view of current view
             * @param selectedItemView: selected item view
             * @param position: position of item
             * @param id: id of item
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                f.setSortString(parentView.getItemAtPosition(position).toString());
                f.sort();
            }

            /**
             * Run when nothing is selected
             * @param parentView: parent view of current view
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });
    }
}