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

public class ReunionAdapter extends RecyclerView.Adapter<ReunionAdapter.ViewHolder> {

    private List<ReunionActivity.ReunionItem> items;
    private long userId;
    private String userType;
    private ReunionActivity activity;

    public ReunionAdapter(List<ReunionActivity.ReunionItem> items, long userId, String userType, ReunionActivity activity) {
        this.items = items;
        this.userId = userId;
        this.userType = userType;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reunion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReunionActivity.ReunionItem item = items.get(position);
        holder.bind(item, userType, activity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<ReunionActivity.ReunionItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titreText;
        private TextView dateHeureText;
        private TextView organisateurText;
        private TextView statutText;
        private TextView participantsText;
        private TextView ordreDuJourText;

        ViewHolder(View itemView) {
            super(itemView);
            titreText = itemView.findViewById(R.id.titre_text);
            dateHeureText = itemView.findViewById(R.id.date_heure_text);
            organisateurText = itemView.findViewById(R.id.organisateur_text);
            statutText = itemView.findViewById(R.id.statut_text);
            participantsText = itemView.findViewById(R.id.participants_text);
            ordreDuJourText = itemView.findViewById(R.id.ordre_du_jour_text);
        }

        void bind(ReunionActivity.ReunionItem item, String userType, ReunionActivity activity) {
            titreText.setText(item.titre);
            
            // Format date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);
            dateHeureText.setText(dateFormat.format(new Date(item.dateHeure)));
            
            organisateurText.setText(item.organisateurName);
            ordreDuJourText.setText(item.ordreDuJour);
            
            // Set statut with color
            String statutTextStr = translateStatut(item.statut);
            statutText.setText(statutTextStr);
            
            int statutColor;
            switch (item.statut) {
                case "PLANIFIEE":
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.primary_blue);
                    break;
                case "EN_COURS":
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.accent_orange);
                    break;
                case "TERMINEE":
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.text_secondary);
                    break;
                default:
                    statutColor = ContextCompat.getColor(itemView.getContext(), R.color.text_primary);
            }
            statutText.setTextColor(statutColor);
            
            // Participants
            if (item.participantNames != null && !item.participantNames.isEmpty()) {
                String participantsStr = String.format(
                    activity.getString(R.string.participants_count),
                    item.participantNames.size()
                );
                participantsText.setText(participantsStr);
                participantsText.setVisibility(View.VISIBLE);
            } else {
                participantsText.setVisibility(View.GONE);
            }
            
            // Make item clickable to edit (only for Admin)
            if ("ADMIN".equals(userType)) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, ReunionEditActivity.class);
                    // Get USER_ID from the activity's intent
                    long userId = -1;
                    if (activity.getIntent() != null) {
                        userId = activity.getIntent().getLongExtra("USER_ID", -1);
                    }
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("REUNION_ID", item.id);
                    activity.startActivity(intent);
                });
            } else {
                // Professors can only view, not edit
                itemView.setOnClickListener(null);
            }
        }

        private String translateStatut(String statut) {
            switch (statut) {
                case "PLANIFIEE":
                    return itemView.getContext().getString(R.string.reunion_statut_planifiee);
                case "EN_COURS":
                    return itemView.getContext().getString(R.string.reunion_statut_en_cours);
                case "TERMINEE":
                    return itemView.getContext().getString(R.string.reunion_statut_terminee);
                default:
                    return statut;
            }
        }
    }
}

