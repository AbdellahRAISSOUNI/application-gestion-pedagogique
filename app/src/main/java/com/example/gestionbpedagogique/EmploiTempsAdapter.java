package com.example.gestionbpedagogique;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmploiTempsAdapter extends RecyclerView.Adapter<EmploiTempsAdapter.ViewHolder> {

    private List<EmploiTempsActivity.EmploiTempsItem> items;
    private String userType;
    private long userId;
    private EmploiTempsActivity activity;

    public EmploiTempsAdapter(List<EmploiTempsActivity.EmploiTempsItem> items, String userType, long userId, EmploiTempsActivity activity) {
        this.items = items;
        this.userType = userType;
        this.userId = userId;
        this.activity = activity;
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
        holder.bind(item, userType, userId, activity);
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

        void bind(EmploiTempsActivity.EmploiTempsItem item, String userType, long userId, EmploiTempsActivity activity) {
            moduleNameText.setText(item.moduleName);
            moduleCodeText.setText(item.moduleCode);
            professeurNameText.setText(item.professeurName);
            jourText.setText(translateDay(item.jourSemaine));
            heureText.setText(item.heureDebut + " - " + item.heureFin);
            salleText.setText(item.salle);
            typeCoursText.setText(item.typeCours);
            
            // Make item clickable for admin to edit
            if ("ADMIN".equals(userType)) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, EmploiTempsEditActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("EMPLOI_TEMPS_ID", item.id);
                    activity.startActivity(intent);
                });
            } else {
                itemView.setOnClickListener(null);
            }
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