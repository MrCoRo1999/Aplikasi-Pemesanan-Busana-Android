package com.app.penjahit.ui;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.app.penjahit.R;
import com.app.penjahit.databinding.ActivityRegisterBinding;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.CallbackFireStore;
import com.app.penjahit.util.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseAppCompat {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private static final String TABLE_CUSTOMER = "customer"; // Ganti jika nama koleksi berbeda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.ivShowPass.setOnClickListener(view -> {
            if ("0".equals(binding.ivShowPass.getTag())) {
                binding.ivShowPass.setColorFilter(ContextCompat.getColor(this, R.color.colorSecondary), PorterDuff.Mode.SRC_IN);
                binding.ivShowPass.setTag("1");
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.ivShowPass.setColorFilter(ContextCompat.getColor(this, R.color.light_gray_2), PorterDuff.Mode.SRC_IN);
                binding.ivShowPass.setTag("0");
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });
        binding.tvLogin.setOnClickListener(view -> finish());

        binding.btnRegister.setOnClickListener(view -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||TextUtils.isEmpty(password)) {
                showToast("Semua field harus diisi");
                return;
            }

            register(email, password, name, phone);
        });

        if (SessionManager.getIsLogin(this)) {
            goToPageAndClearPrevious(HomeActivity.class);
        }
    }

    private void register(String email, String password, String name, String phone) {
        showLoading("Registering...");
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();
                        saveUserToFirestore(userId, name, email, phone);
                    } else {
                        hideLoading();
                        showToast("Gagal registrasi: " + task.getException().getMessage());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        showToast("Gagal registrasi: " + e.getMessage());
                    }
                });
    }

    private void saveUserToFirestore(String userId, String name, String email, String phone) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", userId);
        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);

        firestore.collection(TABLE_CUSTOMER).document(userId)
                .set(data)
                .addOnSuccessListener(unused -> {
                    SessionManager.setCustData(this, name, userId, email, phone);
                    hideLoading();
                    showToast("Registrasi berhasil");
                    goToPageAndClearPrevious(HomeActivity.class);
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    showToast("Gagal menyimpan data: " + e.getMessage());
                });
    }
}
