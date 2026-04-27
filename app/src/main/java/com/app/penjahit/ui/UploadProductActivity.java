package com.app.penjahit.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.app.penjahit.databinding.ActivityUploadProductBinding;
import com.app.penjahit.model.ProductModel;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.PermissionUtility;
import com.app.penjahit.util.SessionManager;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class UploadProductActivity extends BaseAppCompat {

    public static final String EXTRA_PRODUCT = "extra_product";
    private ProductModel editingProduct = null;

    private ActivityUploadProductBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1001;
    private Uri selectedImageUri = null;
    private StorageReference storageReference;
    private PermissionUtility permissionUtility;
    private PermissionUtility permissionUtility13;

    private final ArrayList<String> PERMISSIONS = new ArrayList<>(
            Arrays.asList(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
    );
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private final ArrayList<String> PERMISSIONS_13 = new ArrayList<>(
            Arrays.asList(
                    Manifest.permission.READ_MEDIA_IMAGES
            )
    );

    @Override
    public void onGetData(Intent intent, int requestCode) {
        if(requestCode==PICK_IMAGE_REQUEST){
            selectedImageUri=intent.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        selectedImageUri
                );
                binding.llPickImage.setVisibility(View.GONE);
                binding.imgPreview.setVisibility(View.VISIBLE);
                binding.imgPreview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        permissionUtility = new PermissionUtility(this, PERMISSIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionUtility13 = new PermissionUtility(this, PERMISSIONS_13);
        }

        storageReference = FirebaseStorage.getInstance().getReference();
        setupToolbar();
        setupSpinner();

        editingProduct = (ProductModel) getIntent().getSerializableExtra(EXTRA_PRODUCT);
        if (editingProduct != null) populateFormForEdit();

        binding.llPickImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionAos13();
            } else {
                permissionAos();
            }
        });
        binding.imgPreview.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionAos13();
            } else {
                permissionAos();
            }
        });

        binding.btnUpload.setOnClickListener(v -> uploadProduct());
    }


    private void setupSpinner() {
        String[] categories = {"Pilih Kategori", "Bahan Dasar", "Pakaian", "Jasa"};
        String[] genders = {"Pilih Jenis Kelamin", "Pria", "Wanita", "Anak Kecil", "Unisex"};

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, categories);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, genders);

        binding.spCategory.setAdapter(categoryAdapter);
        binding.spGender.setAdapter(genderAdapter);
    }

    private void populateFormForEdit() {
        binding.etProductName.setText(editingProduct.getName());
        binding.etProductPrice.setText(editingProduct.getPrice());
        binding.etDescription.setText(editingProduct.getDescription());
        binding.spCategory.setSelection(getSpinnerIndex(binding.spCategory, editingProduct.getCategory()));
        binding.spGender.setSelection(getSpinnerIndex(binding.spGender, editingProduct.getGender()));

        Glide.with(this).load(editingProduct.getImageUrl()).into(binding.imgPreview);
        binding.imgPreview.setVisibility(View.VISIBLE);
        binding.llPickImage.setVisibility(View.GONE);
        binding.btnUpload.setText("Update Produk");
    }

    private int getSpinnerIndex(android.widget.Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void permissionAos13() {
        if (permissionUtility13.arePermissionsEnabled()) {
            selectImage();
        } else {
            permissionUtility13.requestMultiplePermissions();
        }
    }

    private void permissionAos() {
        if (permissionUtility.arePermissionsEnabled()) {
            selectImage();
        } else {
            permissionUtility.requestMultiplePermissions();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar Produk"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!permissionUtility13.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                permissionUtility13.requestMultiplePermissions();
                return;
            }
        } else {
            if (!permissionUtility.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                permissionUtility.requestMultiplePermissions();
                return;
            }
        }
        selectImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.imgPreview.setImageURI(selectedImageUri);
            binding.imgPreview.setVisibility(View.VISIBLE);
        }
    }

    private void uploadProduct() {
        String productName = binding.etProductName.getText().toString().trim();
        String productPrice = binding.etProductPrice.getText().toString().trim();
        String category = binding.spCategory.getSelectedItem().toString();
        String gender = binding.spGender.getSelectedItem().toString();
        String description = binding.etDescription.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty() || description.isEmpty()) {
            showToast("Harap lengkapi semua kolom."); return;
        }
        if (category.equals("Pilih Kategori") || gender.equals("Pilih Jenis Kelamin")) {
            showToast("Harap pilih kategori dan jenis kelamin."); return;
        }

        if (editingProduct != null) {
            if (selectedImageUri != null) uploadImageToFirebaseAndUpdate();
            else updateProduct(editingProduct.getImageUrl());
        } else {
            if (selectedImageUri == null) {
                showToast("Pilih gambar produk terlebih dahulu.");
                return;
            }
            uploadImageToFirebase(productName);
        }
    }

    private void uploadImageToFirebase(String productName) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        String fileName = "products/" + System.currentTimeMillis();
        StorageReference ref = storageReference.child(fileName);

        ref.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    progressDialog.dismiss();
                    String imageUrl = uri.toString();
                    uploadProductDataToFirestore(imageUrl);
                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showToast("Upload gagal: " + e.getMessage());
                });
    }

    private void resetForm() {
        binding.etProductName.setText("");
        binding.etProductPrice.setText("");
        binding.etDescription.setText("");
        binding.spCategory.setSelection(0);
        binding.spGender.setSelection(0);
        binding.imgPreview.setImageDrawable(null);
        binding.imgPreview.setVisibility(View.GONE);
        selectedImageUri = null;
    }
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    private void uploadProductDataToFirestore(String imageUrl) {
        showLoading("Upload Produk");
        ProductModel product = new ProductModel();
        String docId = FirebaseFirestore.getInstance().collection("products").document().getId();

        product.setId(docId);
        product.setUserId(SessionManager.getId(this));
        product.setName(binding.etProductName.getText().toString().trim());
        product.setPrice(binding.etProductPrice.getText().toString().trim());
        product.setCategory(binding.spCategory.getSelectedItem().toString());
        product.setGender(binding.spGender.getSelectedItem().toString());
        product.setDescription(binding.etDescription.getText().toString().trim());
        product.setImageUrl(imageUrl);
        product.setContactPersion(SessionManager.getNoHp(this));
        product.setTimestamp(System.currentTimeMillis());

        FirebaseFirestore.getInstance().collection("products")
                .document(docId)
                .set(product)
                .addOnSuccessListener(unused -> {
                    showToast("Produk berhasil ditambahkan!");
                    hideLoading();
                    finish();
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    showToast("Gagal menyimpan produk: " + e.getMessage());
                });
    }

    private void uploadImageToFirebaseAndUpdate() {
        showLoading("Mengunggah gambar baru...");
        String fileName = "products/" + System.currentTimeMillis();
        StorageReference ref = storageReference.child(fileName);

        ref.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    hideLoading();
                    updateProduct(uri.toString());
                }))
                .addOnFailureListener(e -> {
                    hideLoading();
                    showToast("Upload gagal: " + e.getMessage());
                });
    }

    private void updateProduct(String imageUrl) {
        showLoading("Mengupdate produk...");

        editingProduct.setName(binding.etProductName.getText().toString().trim());
        editingProduct.setPrice(binding.etProductPrice.getText().toString().trim());
        editingProduct.setCategory(binding.spCategory.getSelectedItem().toString());
        editingProduct.setGender(binding.spGender.getSelectedItem().toString());
        editingProduct.setDescription(binding.etDescription.getText().toString().trim());
        editingProduct.setImageUrl(imageUrl);
        editingProduct.setTimestamp(System.currentTimeMillis());

        FirebaseFirestore.getInstance().collection("products")
                .document(editingProduct.getId())
                .set(editingProduct)
                .addOnSuccessListener(unused -> {
                    hideLoading();
                    showToast("Produk berhasil diperbarui!");
                    finish();
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    showToast("Gagal update: " + e.getMessage());
                });
    }
}
