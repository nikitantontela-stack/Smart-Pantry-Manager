package com.nikita.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikita.smartpantrymanager.R;
import com.nikita.smartpantrymanager.data.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems = new ArrayList<>();
    private final OnItemActionListener listener;

    // Callback interface so MainActivity can react when the user taps edit or delete
    public interface OnItemActionListener {
        void onEditClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    public PantryAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void setPantryItems(List<PantryItem> items) {
        this.pantryItems = items;
        notifyDataSetChanged(); // tells the list to redraw itself with the new data
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        holder.textItemName.setText(item.getName());

        String quantityText = item.getQuantity() + " " + (item.getUnit() != null ? item.getUnit() : "");
        holder.textItemQuantity.setText(quantityText);

        holder.buttonEditItem.setOnClickListener(v -> listener.onEditClick(item));
        holder.buttonDeleteItem.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textItemQuantity;
        ImageButton buttonEditItem;
        ImageButton buttonDeleteItem;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
            buttonEditItem = itemView.findViewById(R.id.buttonEditItem);
            buttonDeleteItem = itemView.findViewById(R.id.buttonDeleteItem);
        }
    }
}
