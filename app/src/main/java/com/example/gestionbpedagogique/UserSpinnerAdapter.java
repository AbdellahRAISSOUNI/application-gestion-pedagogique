package com.example.gestionbpedagogique;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gestionbpedagogique.database.entities.User;

import java.util.List;

public class UserSpinnerAdapter extends ArrayAdapter<User> {

    public UserSpinnerAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        User user = getItem(position);
        if (user != null) {
            view.setText(user.fullName);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        User user = getItem(position);
        if (user != null) {
            view.setText(user.fullName);
        }
        return view;
    }
}