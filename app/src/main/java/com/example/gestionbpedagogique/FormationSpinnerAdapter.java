package com.example.gestionbpedagogique;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gestionbpedagogique.database.entities.Formation;

import java.util.List;

public class FormationSpinnerAdapter extends ArrayAdapter<Formation> {

    public FormationSpinnerAdapter(Context context, List<Formation> formations) {
        super(context, android.R.layout.simple_spinner_item, formations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Formation formation = getItem(position);
        if (formation != null) {
            view.setText(formation.title);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        Formation formation = getItem(position);
        if (formation != null) {
            view.setText(formation.title);
        }
        return view;
    }
}
