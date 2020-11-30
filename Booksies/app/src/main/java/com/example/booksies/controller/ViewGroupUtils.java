package com.example.booksies.controller;

import android.view.View;
import android.view.ViewGroup;

/**
 * This Class handles utility methods for views like getting parent and removing and replacing views
 */
public class ViewGroupUtils {

    /**
     * Get Parent of view
     * @param view: view
     */
    public static ViewGroup getParent(View view) {
        return (ViewGroup)view.getParent();
    }

    /**
     * Remove view
     * @param view: view
     */
    public static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if(parent != null) {
            parent.removeView(view);
        }
    }

    /**
     * Replaces current view with new view
     * @param currentView: current view
     * @param newView: new view
     */
    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if(parent == null) {
            return;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        removeView(newView);
        parent.addView(newView, index);
    }
}