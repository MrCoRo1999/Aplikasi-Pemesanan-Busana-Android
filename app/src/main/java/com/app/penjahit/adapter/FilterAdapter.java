package com.app.penjahit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.penjahit.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private final List<String> items;
    private int selectedPosition = 0;
    private final Context context;
    private OnFilterSelected onFilterSelected;

    public FilterAdapter(Context context, List<String> items, OnFilterSelected onFilterSelected) {
        this.items = items;
        this.context = context;
        this.onFilterSelected = onFilterSelected;
    }
    public interface OnFilterSelected {
        void onSelected(String value);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.tvFilter.setText(item);

        if (position == selectedPosition) {
            holder.tvFilter.setBackgroundResource(R.drawable.bg_category_selected);
            holder.tvFilter.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.tvFilter.setBackgroundResource(R.drawable.bg_category_unselected);
            holder.tvFilter.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(prev);
            notifyItemChanged(selectedPosition);
            onFilterSelected.onSelected(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFilter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFilter = itemView.findViewById(R.id.tvFilter);
        }
    }
}
