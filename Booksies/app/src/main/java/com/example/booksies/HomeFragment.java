package com.example.booksies;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private ColorDrawable swipeBackgroundDelete = new ColorDrawable(Color.parseColor("#FF0000"));
    private ColorDrawable swipeBackgroundEdit = new ColorDrawable(Color.parseColor("#30BEFF"));
    private Drawable deleteIcon;
    private Drawable editIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.book_list);
        assert recyclerView != null;
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        BooksList.add(new Books("123456","Leo Tolstoy","War and Peace"));
        BooksList.add(new Books("616165","Victor Hugo","Les Miserable"));
        BooksList.add(new Books("225522","Albert Eistein","SCIENCE"));
        BooksList.add(new Books("236266","Ramanujan","MATH"));
        BooksList.add(new Books("646223","Newton","Physics"));
        BooksList.add(new Books("314623","Iranian","History"));
        BooksList.add(new Books("616315","Indian","Computer"));
        mAdapter = new MyAdapter(BooksList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        deleteIcon = ContextCompat.getDrawable(this.getContext(), R.drawable.ic_delete_24px);
        editIcon = ContextCompat.getDrawable(this.getContext(), R.drawable.ic_edit_24px);

        return view;

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            BooksList.remove(viewHolder.getAdapterPosition());
//            mAdapter.notifyDataSetChanged();

        }

        // TODO: Need to clean this functionality up to look better
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            int rectWidth = 240;//itemView.getTop() - itemView.getBottom();
            int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight())/2;
            int editIconMargin = (itemView.getHeight() - editIcon.getIntrinsicHeight())/2;
            swipeBackgroundDelete.setBounds(itemView.getLeft(), itemView.getTop(), rectWidth, itemView.getBottom());
            swipeBackgroundEdit.setBounds(itemView.getLeft() + rectWidth, itemView.getTop(), rectWidth * 2, itemView.getBottom());

            deleteIcon.setBounds(itemView.getLeft() + deleteIconMargin, itemView.getTop() + deleteIconMargin, itemView.getLeft() +
                                        deleteIconMargin + deleteIcon.getIntrinsicWidth(), itemView.getBottom() - deleteIconMargin );
            editIcon.setBounds(itemView.getLeft() + rectWidth + editIconMargin, itemView.getTop() + editIconMargin, itemView.getLeft() + rectWidth +
                                        editIconMargin + deleteIcon.getIntrinsicWidth(), itemView.getBottom() - editIconMargin );

            swipeBackgroundDelete.draw(c);
            swipeBackgroundEdit.draw(c);
            c.save();
            c.clipRect(itemView.getLeft(), itemView.getTop(), rectWidth, itemView.getBottom());
            c.restore();
            deleteIcon.draw(c);
            editIcon.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}