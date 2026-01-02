package com.example.gestionbpedagogique;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmploiTempsAdapter extends RecyclerView.Adapter<EmploiTempsAdapter.ViewHolder> {

    private List<EmploiTempsActivity.EmploiTempsItem> items;

    public EmploiTempsAdapter(List<EmploiTempsActivity.EmploiTempsItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emploi_temps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmploiTempsActivity.EmploiTempsItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<EmploiTempsActivity.EmploiTempsItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView moduleNameText;
        private TextView moduleCodeText;
        private TextView professeurNameText;
        private TextView jourText;
        private TextView heureText;
        private TextView salleText;
        private TextView typeCoursText;

        ViewHolder(View itemView) {
            super(itemView);
            moduleNameText = itemView.findViewById(R.id.module_name_text);
            moduleCodeText = itemView.findViewById(R.id.module_code_text);
            professeurNameText = itemView.findViewById(R.id.professeur_name_text);
            jourText = itemView.findViewById(R.id.jour_text);
            heureText = itemView.findViewById(R.id.heure_text);
            salleText = itemView.findViewById(R.id.salle_text);
            typeCoursText = itemView.findViewById(R.id.type_cours_text);
        }

        void bind(EmploiTempsActivity.EmploiTempsItem item) {
            moduleNameText.setText(item.moduleName);
            moduleCodeText.setText(item.moduleCode);
            professeurNameText.setText(item.professeurName);
            jourText.setText(translateDay(item.jourSemaine));
            heureText.setText(item.heureDebut + " - " + item.heureFin);
            salleText.setText(item.salle);
            typeCoursText.setText(item.typeCours);
        }

        private String translateDay(String day) {
            switch (day.toUpperCase()) {
                case "LUNDI": return "Lundi";
                case "MARDI": return "Mardi";
                case "MERCREDI": return "Mercredi";
                case "JEUDI": return "Jeudi";
                case "VENDREDI": return "Vendredi";
                case "SAMEDI": return "Samedi";
                default: return day;
            }
        }
    }
}