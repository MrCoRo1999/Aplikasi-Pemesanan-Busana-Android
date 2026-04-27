package com.app.penjahit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.penjahit.R;
import com.app.penjahit.databinding.ItemUploadedProductBinding;
import com.app.penjahit.model.ProductModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final Context context;
    private final List<ProductModel> productList;
    private final OnProductEditListener listener;

    public ProductListAdapter(Context context, List<ProductModel> productList, OnProductEditListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public interface OnProductEditListener {
        void onEditClicked(ProductModel product);
        void onDeleteClicked(ProductModel product);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemUploadedProductBinding binding = ItemUploadedProductBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvCategory.setText(product.getCategory());
        holder.binding.tvGender.setText(product.getGender());
        holder.binding.tvDescription.setText(product.getDescription());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.background_image_jahitin_2)
                .into(holder.binding.imgProduct);

        holder.binding.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClicked(product);
            }
        });

        holder.binding.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClicked(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemUploadedProductBinding binding;

        public ViewHolder(@NonNull ItemUploadedProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
