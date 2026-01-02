package com.example.gestionbpedagogique;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gestionbpedagogique.database.entities.Module;

import java.util.List;

public class ModuleSpinnerAdapter extends ArrayAdapter<Module> {

    public ModuleSpinnerAdapter(Context context, int resource, List<Module> modules) {
        super(context, resource, modules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Module module = getItem(position);
        if (module != null) {
            view.setText(module.code + " - " + module.nom);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        Module module = getItem(position);
        if (module != null) {
            view.setText(module.code + " - " + module.nom);
        }
        return view;
    }
}