package com.app.penjahit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.app.penjahit.R;
import com.app.penjahit.util.SessionManager;
import com.bumptech.glide.Glide;
import com.app.penjahit.databinding.ActivityDetailProductBinding;
import com.app.penjahit.model.ProductModel;
import com.app.penjahit.util.BaseAppCompat;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailProductActivity extends BaseAppCompat {
    private ActivityDetailProductBinding binding;
    private ProductModel product;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();

        setupToolbar();
        getProductDataFromIntent();
        if (product != null) {
            showProductData();
        }
        setupOrderButton();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getProductDataFromIntent() {
        product = (ProductModel) getIntent().getSerializableExtra("product");
        if (product == null) {
            showToast("Produk tidak ditemukan.");
            finish();
        }
    }

    private void showProductData() {
        binding.tvProductName.setText(product.getName());
        binding.tvProductPrice.setText(convertToCurrency(Long.parseLong(product.getPrice())));
        binding.tvProductCategory.setText("Kategori: " + product.getCategory());
        binding.tvProductGender.setText("Gender: " + product.getGender());
        binding.tvProductDescription.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.background_image_jahitin_2)
                .into(binding.imageProduct);
    }

    private void setupOrderButton() {
        binding.btnOrder.setOnClickListener(v -> {
            if (product.getContactPersion() != null && !product.getContactPersion().isEmpty()) {
                String message = "Halo saya melihat product anda di Fazermood. Apakah "+ product.getName() + " Masih tersedia?";
                String url = "https://wa.me/+62" + product.getContactPersion() + "?text=" + Uri.encode(message);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                showToast("Nomor kontak tidak tersedia.");
            }
        });
        binding.btnCart.setOnClickListener(view -> addToCart());
    }

    private void addToCart() {
        String userId = SessionManager.getId(this); // ambil ID user yang login

        if (userId == null || userId.isEmpty()) {
            showToast("Gagal menambahkan ke keranjang: ID pengguna tidak ditemukan.");
            return;
        }

        firestore.collection("cart")
                .document(userId)
                .collection("items")
                .document(product.getId()) // pakai ID produk sebagai ID dokumen
                .set(product) // menyimpan objek ProductModel
                .addOnSuccessListener(unused -> showToast("Berhasil dimasukkan ke keranjang."))
                .addOnFailureListener(e -> showToast("Gagal menambahkan ke keranjang: " + e.getMessage()));
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
