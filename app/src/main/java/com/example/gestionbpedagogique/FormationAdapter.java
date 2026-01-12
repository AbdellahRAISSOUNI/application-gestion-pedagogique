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

public class FormationAdapter extends RecyclerView.Adapter<FormationAdapter.ViewHolder> {

    private List<FormationActivity.FormationItem> items;
    private long userId;
    private String userType;
    private FormationActivity activity;

    public FormationAdapter(List<FormationActivity.FormationItem> items, long userId, String userType, FormationActivity activity) {
        this.items = items;
        this.userId = userId;
        this.userType = userType;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_formation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FormationActivity.FormationItem item = items.get(position);
        holder.bind(item, userType, activity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<FormationActivity.FormationItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView typeText;
        private TextView cycleText;
        private TextView statusText;
        private TextView createdByText;
        private TextView createdDateText;
        private TextView validatedDateText;
        private com.google.android.material.card.MaterialCardView statusBadgeCard;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            typeText = itemView.findViewById(R.id.type_text);
            cycleText = itemView.findViewById(R.id.cycle_text);
            statusText = itemView.findViewById(R.id.status_text);
            createdByText = itemView.findViewById(R.id.created_by_text);
            createdDateText = itemView.findViewById(R.id.created_date_text);
            validatedDateText = itemView.findViewById(R.id.validated_date_text);
            // Find the parent MaterialCardView of statusText
            View parent = (View) statusText.getParent();
            while (parent != null && !(parent instanceof com.google.android.material.card.MaterialCardView)) {
                parent = (View) parent.getParent();
            }
            if (parent instanceof com.google.android.material.card.MaterialCardView) {
                statusBadgeCard = (com.google.android.material.card.MaterialCardView) parent;
            }
        }

        void bind(FormationActivity.FormationItem item, String userType, FormationActivity activity) {
            titleText.setText(item.title);
            
            // Type
            String typeStr = translateType(item.type);
            typeText.setText(typeStr);
            
            // Cycle
            String cycleStr = translateCycle(item.type, item.cycle);
            cycleText.setText(cycleStr);
            
            // Status with color
            String statusTextStr = translateStatus(item.status);
            statusText.setText(statusTextStr);
            
            // Update accent bar color based on status
            View accentBar = itemView.findViewById(R.id.accent_bar);
            if (accentBar != null) {
                int accentColor;
                switch (item.status) {
                    case "EN_ATTENTE":
                        accentColor = ContextCompat.getColor(itemView.getContext(), R.color.status_warning);
                        break;
                    case "APPROUVEE":
                        accentColor = ContextCompat.getColor(itemView.getContext(), R.color.status_success);
                        break;
                    case "REFUSEE":
                        accentColor = ContextCompat.getColor(itemView.getContext(), R.color.status_error);
                        break;
                    default:
                        accentColor = ContextCompat.getColor(itemView.getContext(), R.color.status_info);
                }
                accentBar.setBackgroundColor(accentColor);
            }
            
            // Update status badge background color
            int statusBgColor;
            switch (item.status) {
                case "EN_ATTENTE":
                    statusBgColor = ContextCompat.getColor(itemView.getContext(), R.color.status_warning);
                    break;
                case "APPROUVEE":
                    statusBgColor = ContextCompat.getColor(itemView.getContext(), R.color.status_success);
                    break;
                case "REFUSEE":
                    statusBgColor = ContextCompat.getColor(itemView.getContext(), R.color.status_error);
                    break;
                default:
                    statusBgColor = ContextCompat.getColor(itemView.getContext(), R.color.status_info);
            }
            if (statusBadgeCard != null) {
                statusBadgeCard.setCardBackgroundColor(statusBgColor);
            }
            statusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            
            // Created by
            createdByText.setText(item.createdByName);
            
            // Date de création
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            createdDateText.setText(dateFormat.format(new Date(item.createdDate)));
            
            // Date de validation
            if (item.validatedDate != null && item.validatedDate > 0) {
                validatedDateText.setText(dateFormat.format(new Date(item.validatedDate)));
                validatedDateText.setVisibility(View.VISIBLE);
            } else {
                validatedDateText.setVisibility(View.GONE);
            }
            
            // Make item clickable to edit (Admin can edit all)
            if ("ADMIN".equals(userType)) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, FormationEditActivity.class);
                    long userId = -1;
                    if (activity.getIntent() != null) {
                        userId = activity.getIntent().getLongExtra("USER_ID", -1);
                    }
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("FORMATION_ID", item.id);
                    activity.startActivity(intent);
                });
            } else {
                itemView.setOnClickListener(null);
            }
        }

        private String translateType(String type) {
            switch (type) {
                case "INITIALE":
                    return itemView.getContext().getString(R.string.formation_type_initiale);
                case "CONTINUE":
                    return itemView.getContext().getString(R.string.formation_type_continue);
                default:
                    return type;
            }
        }

        private String translateCycle(String type, String cycle) {
            if ("INITIALE".equals(type)) {
                switch (cycle) {
                    case "PREPARATOIRE":
                        return itemView.getContext().getString(R.string.formation_cycle_preparatoire);
                    case "INGENIEUR":
                        return itemView.getContext().getString(R.string.formation_cycle_ingenieur);
                    case "MASTER":
                        return itemView.getContext().getString(R.string.formation_cycle_master);
                    default:
                        return cycle;
                }
            } else if ("CONTINUE".equals(type)) {
                switch (cycle) {
                    case "DCA":
                        return itemView.getContext().getString(R.string.formation_cycle_dca);
                    case "DCESS":
                        return itemView.getContext().getString(R.string.formation_cycle_dcess);
                    default:
                        return cycle;
                }
            }
            return cycle;
        }

        private String translateStatus(String status) {
            switch (status) {
                case "EN_ATTENTE":
                    return itemView.getContext().getString(R.string.formation_status_en_attente);
                case "APPROUVEE":
                    return itemView.getContext().getString(R.string.formation_status_approuvee);
                case "REFUSEE":
                    return itemView.getContext().getString(R.string.formation_status_refusee);
                default:
                    return status;
            }
        }
    }
}
