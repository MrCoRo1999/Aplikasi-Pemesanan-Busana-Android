package com.app.penjahit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.penjahit.R;
import com.app.penjahit.databinding.ItemCartProductBinding;
import com.app.penjahit.databinding.ItemUploadedProductBinding;
import com.app.penjahit.model.ProductModel;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private final Context context;
    private final List<ProductModel> productList;
    private final OnProductEditListener listener;

    public CartListAdapter(Context context, List<ProductModel> productList, OnProductEditListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public interface OnProductEditListener {
        void onClicked(ProductModel product);
        void onDeleteClicked(ProductModel product);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCartProductBinding binding = ItemCartProductBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvCategory.setText(product.getCategory());
        holder.binding.tvQtyPrice.setText(convertToCurrency(Long.parseLong(product.getPrice())));

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.background_image_jahitin_2)
                .into(holder.binding.imgProduct);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClicked(product);

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
        ItemCartProductBinding binding;

        public ViewHolder(@NonNull ItemCartProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public String convertToCurrency(long price){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(price);
    }
}
