package com.app.penjahit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.app.penjahit.R;
import com.app.penjahit.adapter.BannerAdapter;
import com.app.penjahit.adapter.FilterAdapter;
import com.app.penjahit.adapter.ProductAdapter;
import com.app.penjahit.databinding.ActivitySearchBinding;
import com.app.penjahit.model.BannerModel;
import com.app.penjahit.model.ProductModel;
import com.app.penjahit.util.BaseAppCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends BaseAppCompat {
    private ActivitySearchBinding binding;
    private List<ProductModel> fullProductList = new ArrayList<>();
    private List<ProductModel> filteredProductList = new ArrayList<>();
    private ProductAdapter adapter;
    private String selectedCategory = "Semua";
    private String selectedGender = "Semua";
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpRv();
        setUpFilter();
        fetchProductsFromFirestore();
        setupToolbar();
        setupSearchListener();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupSearchListener() {
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().trim();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setUpFilter() {
        List<String> kategoriList = Arrays.asList("Semua", "Bahan Dasar", "Pakaian", "Jasa");
        List<String> genderList = Arrays.asList("Semua", "Pria", "Wanita", "Anak-Anak", "Uni-Sex");

        FilterAdapter kategoriAdapter = new FilterAdapter(this, kategoriList, selected -> {
            selectedCategory = selected;
            applyFilters();
        });
        FilterAdapter genderAdapter = new FilterAdapter(this, genderList, selected -> {
            selectedGender = selected;
            applyFilters();
        });

        binding.rvKategori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvKategori.setAdapter(kategoriAdapter);

        binding.rvSex.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvSex.setAdapter(genderAdapter);
    }

    private void setUpRv() {
        binding.rvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvProduct.setHasFixedSize(true);
        adapter = new ProductAdapter(this, filteredProductList, product -> {
            Intent intent = new Intent(SearchActivity.this, DetailProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);

        });
        binding.rvProduct.setAdapter(adapter);
        binding.rvProduct.addItemDecoration(new GridSpacingItemDecoration(24));
    }

    private void fetchProductsFromFirestore() {
        FirebaseFirestore.getInstance().collection("products")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    fullProductList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        ProductModel product = doc.toObject(ProductModel.class);
                        fullProductList.add(product);
                    }
                    applyFilters();
                })
                .addOnFailureListener(e -> {
                    Log.e("SearchActivity", "Gagal mengambil produk: " + e.getMessage());
                });
    }

    private void applyFilters() {
        filteredProductList.clear();
        for (ProductModel product : fullProductList) {
            boolean matchCategory = selectedCategory.equals("Semua") || product.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchGender = selectedGender.equals("Semua") || product.getGender().equalsIgnoreCase(selectedGender);
            boolean matchSearch = searchQuery.isEmpty() || product.getName().toLowerCase().contains(searchQuery.toLowerCase());
            if (matchCategory && matchGender && matchSearch) {
                filteredProductList.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
