package com.example.booksies.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.booksies.R;
import com.example.booksies.controller.UserProfileFragment;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private ArrayList<Notification> notifications;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, 0, notifications);
        this.notifications = notifications;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_notifications, parent, false);
        }

        Notification notification = notifications.get(position);

        TextView username = view.findViewById(R.id.notification_title);
        TextView body = view.findViewById(R.id.notification_body);

        username.setText(notification.getTitle());
        body.setText(notification.getBody());

        return view;
    }


}
