package com.app.penjahit.ui;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.penjahit.R;
import com.app.penjahit.databinding.ActivityEditProfileBinding;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends BaseAppCompat {

    private ActivityEditProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private static final String TABLE_CUSTOMER = "customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        loadUserData();

        binding.btnSave.setOnClickListener(view -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            showLoading("");
            updateProfile(name, email, phone);
        });
    }

    private void loadUserData() {
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        firestore.collection(TABLE_CUSTOMER).document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        binding.etName.setText(documentSnapshot.getString("name"));
                        binding.etPhone.setText(documentSnapshot.getString("phone"));
                    }
                    binding.etEmail.setText(currentUser.getEmail());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void updateProfile(String name, String newEmail, String phone) {
        if (currentUser == null) return;

        String currentEmail = currentUser.getEmail();
        String uid = currentUser.getUid();

        if (!newEmail.equals(currentEmail)) {
            showPasswordBottomSheet(currentEmail, name, newEmail, phone);
        } else {
            saveToFirestore(uid, name, currentEmail, phone); // email tidak berubah
            hideLoading();
        }
    }

    private void showPasswordBottomSheet(String currentEmail, String name, String newEmail, String phone) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_password, null);
        dialog.setContentView(sheetView);

        EditText etPassword = sheetView.findViewById(R.id.etPassword);
        View btnSubmit = sheetView.findViewById(R.id.btnSubmitPassword);

        btnSubmit.setOnClickListener(v -> {
            String password = etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss();
            reauthenticateAndVerifyBeforeUpdateEmail(currentEmail, password, name, newEmail, phone);
        });

        dialog.show();
    }


    private void reauthenticateAndVerifyBeforeUpdateEmail(String currentEmail, String password,
                                                          String name, String newEmail, String phone) {
        if (currentUser == null) return;

        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, password);

        currentUser.reauthenticate(credential)
                .addOnSuccessListener(authResult -> {
                    currentUser.verifyBeforeUpdateEmail(newEmail)
                            .addOnSuccessListener(unused -> {
                                hideLoading();

                                // ⚠️ Email belum benar-benar berubah, jadi tetap simpan dengan currentEmail
                                saveToFirestore(currentUser.getUid(), name, newEmail, phone);

                                Toast.makeText(this,
                                        "Link verifikasi dikirim ke email baru. Klik link tersebut untuk menyelesaikan proses perubahan email.",
                                        Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                hideLoading();
                                Toast.makeText(this, "Gagal kirim email verifikasi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    Toast.makeText(this, "Password salah: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToFirestore(String uid, String name, String email, String phone) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email); // simpan email terakhir yang aktif (bukan email baru yang belum diverifikasi)
        userMap.put("phone", phone);

        firestore.collection(TABLE_CUSTOMER).document(uid)
                .set(userMap)
                .addOnSuccessListener(unused -> {
                    SessionManager.setCustData(EditProfileActivity.this, name, uid, email, phone);
                    Toast.makeText(this, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal menyimpan profil: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
