package com.example.booksies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksies.R;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {
    ArrayList<String> BooksData = new ArrayList<String>();
    ArrayList<Books> BooksList = new ArrayList<Books>();
    private  RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        String[] books = {"War and Peace", "Les Miserable","SCIENCE","MATH", "Physics", "History"
                ,"The three musketeers", "TWD","Calculus","Statistics"}; // test data for recycler view
        BooksData.addAll(Arrays.asList(books)); // add all books to ArrayList
        recyclerView =(RecyclerView) view.findViewById(R.id.book_list);
        assert recyclerView != null;
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        BooksList.add(new Books("War and Peace","","",false));
        BooksList.add(new Books("Les Miserable","","",false));
        BooksList.add(new Books("SCIENCE","","",false));
        BooksList.add(new Books("MATH","","",false));
        BooksList.add(new Books("Physics","","",false));
        BooksList.add(new Books("History","","",false));
        BooksList.add(new Books("History","","",false));
        mAdapter = new MyAdapter(BooksList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        return view;

    }

}