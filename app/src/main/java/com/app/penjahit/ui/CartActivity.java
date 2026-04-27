package com.app.penjahit.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.penjahit.adapter.CartListAdapter;
import com.app.penjahit.adapter.ProductListAdapter;
import com.app.penjahit.databinding.ActivityCartBinding;
import com.app.penjahit.model.ProductModel;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseAppCompat {

    private ActivityCartBinding binding;
    private CartListAdapter cartAdapter;
    private List<ProductModel> cartItems = new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        setupToolbar();
        setupRecyclerView();
        fetchCartItems();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        cartAdapter = new CartListAdapter(this, cartItems, new CartListAdapter.OnProductEditListener() {
            @Override
            public void onClicked(ProductModel product) {
                Intent intent = new Intent(CartActivity.this, DetailProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);

            }

            @Override
            public void onDeleteClicked(ProductModel product) {
                removeFromCart(product);
            }
        });

        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCartItems.setAdapter(cartAdapter);
    }

    private void fetchCartItems() {
        showLoading("Memuat keranjang...");
        String userId = SessionManager.getId(this);

        firestore.collection("cart")
                .document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    cartItems.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        ProductModel product = doc.toObject(ProductModel.class);
                        product.setId(doc.getId());
                        cartItems.add(product);
                    }
                    cartAdapter.notifyDataSetChanged();
                    hideLoading();
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    showToast("Gagal mengambil keranjang: " + e.getMessage());
                });
    }

    private void removeFromCart(ProductModel product) {
        String userId = SessionManager.getId(this);
        firestore.collection("cart")
                .document(userId)
                .collection("items")
                .document(product.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    cartItems.remove(product);
                    cartAdapter.notifyDataSetChanged();
                    showToast("Produk dihapus dari keranjang.");
                })
                .addOnFailureListener(e -> {
                    showToast("Gagal menghapus produk: " + e.getMessage());
                });
    }
}
