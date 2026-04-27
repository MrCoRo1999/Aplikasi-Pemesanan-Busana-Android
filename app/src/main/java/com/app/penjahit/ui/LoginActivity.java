package com.app.penjahit.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.app.penjahit.R;
import com.app.penjahit.databinding.ActivityLoginBinding;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BaseAppCompat {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private static final String TABLE_CUSTOMER = "customer"; // Ganti jika nama koleksi berbeda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (SessionManager.getIsLogin(this)) {
            goToPageAndClearPrevious(HomeActivity.class);
        }

        binding.tvRegister.setOnClickListener(view -> goToPage(RegisterActivity.class));

        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                login(email, password);
            }
        });

        binding.ivShowPass.setOnClickListener(view -> {
            boolean isShowing = "1".equals(binding.ivShowPass.getTag());
            binding.ivShowPass.setColorFilter(ContextCompat.getColor(
                            this, isShowing ? R.color.light_gray_2 : R.color.colorSecondary),
                    PorterDuff.Mode.SRC_IN);
            binding.ivShowPass.setTag(isShowing ? "0" : "1");
            binding.etPassword.setTransformationMethod(
                    isShowing ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });
        binding.tvForgetPassword.setOnClickListener(view -> {
            String email = binding.etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Silakan isi email terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else {
                sendResetPassword(email);
            }
        });

    }
    private void sendResetPassword(String email) {
        showLoading("Mengirim email reset password...");
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    hideLoading();
                    if (task.isSuccessful()) {
                        showToast("Email reset password telah dikirim ke " + email);
                    } else {
                        showToast("Gagal mengirim email: " + task.getException().getMessage());
                    }
                });
    }


    private void login(String email, String password) {
        showLoading("Loading");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                getUserData(user.getUid());
                            }
                        } else {
                            hideLoading();
                            Toast.makeText(LoginActivity.this,
                                    task.getException() != null ? task.getException().getMessage() : "Login gagal",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getUserData(String userId) {
        firestore.collection(TABLE_CUSTOMER)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        SessionManager.setCustData(LoginActivity.this, name, userId, email, phone);
                        goToPageAndClearPrevious(HomeActivity.class);
                        hideLoading();
                    } else {
                        mAuth.signOut();
                        hideLoading();
                        showToast("Akun tidak ditemukan");
                    }
                })
                .addOnFailureListener(e -> {
                    hideLoading();
                    showToast("Gagal mengambil data user: " + e.getMessage());
                });
    }
}
