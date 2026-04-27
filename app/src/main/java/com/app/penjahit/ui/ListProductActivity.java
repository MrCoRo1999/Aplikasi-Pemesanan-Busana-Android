package com.app.penjahit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.penjahit.R;
import com.app.penjahit.adapter.ProductListAdapter;
import com.app.penjahit.databinding.ActivityUploadedProductListBinding;
import com.app.penjahit.model.ProductModel;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends BaseAppCompat {

    private ActivityUploadedProductListBinding binding;
    private ProductListAdapter productAdapter;
    private List<ProductModel> productList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private static final String COLLECTION_PRODUCTS = "products";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadedProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        fetchProductFromFirestore();
        setupToolbar();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {

        productAdapter = new ProductListAdapter(this, productList, new ProductListAdapter.OnProductEditListener() {
            @Override
            public void onEditClicked(ProductModel product) {
                Intent intent = new Intent(ListProductActivity.this, UploadProductActivity.class);
                intent.putExtra(UploadProductActivity.EXTRA_PRODUCT, product);
                startActivity(intent);
            }

            @Override
            public void onDeleteClicked(ProductModel product) {
                showLoading("Menghapus produk...");

                firestore.collection(COLLECTION_PRODUCTS)
                        .document(product.getId())
                        .delete()
                        .addOnSuccessListener(unused -> {
                            // Hapus dari list lokal
                            int index = productList.indexOf(product);
                            if (index != -1) {
                                productList.remove(index);
                                productAdapter.notifyItemRemoved(index);
                            }
                            hideLoading();
                            showToast("Produk berhasil dihapus.");
                        })
                        .addOnFailureListener(e -> {
                            hideLoading();
                            showToast("Gagal menghapus produk: " + e.getMessage());
                        });
            }
        });

        binding.rvUploadedProduct.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUploadedProduct.setAdapter(productAdapter);


    }
    private void fetchProductFromFirestore() {
        showLoading("Memuat produk...");

        firestore.collection(COLLECTION_PRODUCTS)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .whereEqualTo("userId", SessionManager.getId(this))
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    productList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        ProductModel product = doc.toObject(ProductModel.class);
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                    hideLoading();
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    Log.d("failure ","--> "+e.getMessage());
                    showToast("Gagal mengambil produk: " + e.getMessage());
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProductFromFirestore();

    }
}