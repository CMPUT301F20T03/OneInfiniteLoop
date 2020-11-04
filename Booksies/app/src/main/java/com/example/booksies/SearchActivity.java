package com.example.booksies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<Books> allBooks = new ArrayList<>();
    private ArrayList<Books> searchedBooks = new ArrayList<>();
    private AutoCompleteTextView searchBar;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        adapter = new MyAdapter(searchedBooks);

        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterSearchedBooks();
            }
        });
        filterSearchedBooks();

    }

    private void filterSearchedBooks() {
        String searchString = searchBar.getText().toString();
        List<String> searchStringList = Arrays.asList(searchString.split("\\s+"));     // be able to search by multiple whitespace separated keywords

        for (Books book : allBooks) {
            if (book.getStatus() == book_status.ACCEPTED || book.getStatus() == book_status.BORROWED) {
                continue;
            }
            for (String keyword : searchStringList) {
                if (book.getTitle().toLowerCase().contains(keyword) ||
                    book.getAuthor().toLowerCase().contains(keyword) ||
                    book.getComments().toLowerCase().contains(keyword)) {

                    searchedBooks.add(book);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}