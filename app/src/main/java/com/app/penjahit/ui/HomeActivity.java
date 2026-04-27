package com.app.penjahit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.app.penjahit.R;
import com.app.penjahit.adapter.BannerAdapter;
import com.app.penjahit.adapter.FilterAdapter;
import com.app.penjahit.adapter.ProductAdapter;
import com.app.penjahit.databinding.ActivityHomeBinding;
import com.app.penjahit.model.BannerModel;
import com.app.penjahit.model.ProductModel;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends BaseAppCompat {
    private ActivityHomeBinding binding;
    private List<ProductModel> fullProductList = new ArrayList<>();
    private List<ProductModel> filteredProductList = new ArrayList<>();
    private ProductAdapter adapter;
    private String selectedCategory = "Semua";
    private String selectedGender = "Semua";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpBanner();
        setUpRv();
        setUpFilter();
        setUpSideMenu();
        setupToolbar();
        fetchProductsFromFirestore();
        binding.searchBar.setOnClickListener(view -> goToPage(SearchActivity.class));
    }
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
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

    private void setUpBanner() {
        ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        List<BannerModel> bannerList = new ArrayList<>();
        bannerList.add(new BannerModel(R.drawable.background_image_jahitin, "Diskon untuk semua bahan!"));
        bannerList.add(new BannerModel(R.drawable.background_image_jahitin_2, "Cek jasa jahit kilat hari ini"));
        bannerList.add(new BannerModel(R.drawable.background_image_jahitin, "Koleksi baju pria & wanita terbaru"));

        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);
        new Handler().postDelayed(new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (bannerViewPager.getAdapter() != null) {
                    currentPage = (currentPage + 1) % bannerViewPager.getAdapter().getItemCount();
                    bannerViewPager.setCurrentItem(currentPage, true);
                    bannerViewPager.postDelayed(this, 4000);
                }
            }
        }, 4000);
    }

    private void setUpRv() {
        binding.rvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvProduct.setHasFixedSize(true);
        adapter = new ProductAdapter(this, filteredProductList, product -> {
            Intent intent = new Intent(HomeActivity.this, DetailProductActivity.class);
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
                    Log.e("HomeActivity", "Gagal mengambil produk: " + e.getMessage());
                });
    }

    private void applyFilters() {
        filteredProductList.clear();
        for (ProductModel product : fullProductList) {
            boolean matchCategory = selectedCategory.equals("Semua") || product.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchGender = selectedGender.equals("Semua") || product.getGender().equalsIgnoreCase(selectedGender);
            if (matchCategory && matchGender) {
                filteredProductList.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setUpSideMenu() {
        binding.ivMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_profile) {
                goToPage(ProfileActivity.class);
            } else if (id == R.id.nav_sell) {
                goToPage(UploadProductActivity.class);
            } else if (id == R.id.nav_list_product) {
                goToPage(ListProductActivity.class);
            } else if (id == R.id.nav_logout) {
                SessionManager.clearData(this);
                goToPageAndClearPrevious(IntroductionActivity.class);
            }else if (id == R.id.nav_list_cart) {
                goToPage(CartActivity.class);
            }

            binding.drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProductsFromFirestore();
    }
}