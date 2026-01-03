package com.example.gestionbpedagogique;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CahierChargesAdapter extends RecyclerView.Adapter<CahierChargesAdapter.ViewHolder> {

    private List<CahierChargesActivity.CahierChargesItem> items;
    private long userId;
    private String userType;
    private CahierChargesActivity activity;

    public CahierChargesAdapter(List<CahierChargesActivity.CahierChargesItem> items, long userId, String userType, CahierChargesActivity activity) {
        this.items = items;
        this.userId = userId;
        this.userType = userType;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cahier_charges, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CahierChargesActivity.CahierChargesItem item = items.get(position);
        holder.bind(item, userType, activity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<CahierChargesActivity.CahierChargesItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titreText;
        private TextView typeText;
        private TextView auteurText;
        private TextView formationText;
        private TextView statutText;
        private TextView dateCreationText;
        private TextView dateValidationText;

        ViewHolder(View itemView) {
            super(itemView);
            titreText = itemView.findViewById(R.id.titre_text);
            typeText = itemView.findViewById(R.id.type_text);
            auteurText = itemView.findViewById(R.id.auteur_text);
            formationText = itemView.findViewById(R.id.formation_text);
            statutText = itemView.findViewById(R.id.statut_text);
            dateCreationText = itemView.findViewById(R.id.date_creation_text);
            dateValidationText = itemView.findViewById(R.id.date_validation_text);
        }

        void bind(CahierChargesActivity.CahierChargesItem item, String userType, CahierChargesActivity activity) {
            titreText.setText(item.titre);
            
            // Type
            String typeStr = translateType(item.type);
            typeText.setText(typeStr);
            
            // Auteur
            auteurText.setText(item.auteurName);
            
            // Formation
            if (item.formationTitle != null && !item.formationTitle.isEmpty()) {
                formationText.setText(item.formationTitle);
                formationText.setVisibility(View.VISIBLE);
            } else {
                formationText.setVisibility(View.GONE);
            }
            
            // Statut with color
            String statutTextStr = translateStatut(item.statut);
            statutText.setText(statutTextStr);
            
            int statutColor;
            switch (item.statut) {
                case "BROUILLON":
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.text_secondary);
                    break;
                case "ENVOYE":
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.primary_blue);
                    break;
                case "APPROUVE":
                    statutColor = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_dark);
                    break;
                case "REFUSE":
                    statutColor = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark);
                    break;
                default:
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.text_primary);
            }
            statutText.setTextColor(statutColor);
            
            // Date de création
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            dateCreationText.setText(dateFormat.format(new Date(item.dateCreation)));
            
            // Date de validation
            if (item.dateValidation != null) {
                dateValidationText.setText(dateFormat.format(new Date(item.dateValidation)));
                dateValidationText.setVisibility(View.VISIBLE);
            } else {
                dateValidationText.setVisibility(View.GONE);
            }
            
            // Make item clickable to edit/view
            // Admin can view all and approve/refuse
            // Professeur Assistant can view and edit their own
            boolean canEdit = "ADMIN".equals(userType) || 
                             ("PROFESSEUR_ASSISTANT".equals(userType) && item.statut.equals("BROUILLON"));
            
            if (canEdit) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, CahierChargesEditActivity.class);
                    long userId = -1;
                    if (activity.getIntent() != null) {
                        userId = activity.getIntent().getLongExtra("USER_ID", -1);
                    }
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("CAHIER_CHARGES_ID", item.id);
                    activity.startActivity(intent);
                });
            } else {
                itemView.setOnClickListener(null);
            }
        }

        private String translateType(String type) {
            switch (type) {
                case "FORMATION_INITIALE":
                    return itemView.getContext().getString(R.string.cahier_type_formation_initiale);
                case "FORMATION_CONTINUE":
                    return itemView.getContext().getString(R.string.cahier_type_formation_continue);
                default:
                    return type;
            }
        }

        private String translateStatut(String statut) {
            switch (statut) {
                case "BROUILLON":
                    return itemView.getContext().getString(R.string.cahier_statut_brouillon);
                case "ENVOYE":
                    return itemView.getContext().getString(R.string.cahier_statut_envoye);
                case "APPROUVE":
                    return itemView.getContext().getString(R.string.cahier_statut_approuve);
                case "REFUSE":
                    return itemView.getContext().getString(R.string.cahier_statut_refuse);
                default:
                    return statut;
            }
        }
    }
}
