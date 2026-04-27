package com.app.penjahit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.app.penjahit.R;
import com.app.penjahit.databinding.ActivityIntroductionBinding;
import com.app.penjahit.databinding.ActivityLoginBinding;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroductionActivity extends BaseAppCompat {
    private ActivityIntroductionBinding binding;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    Handler handler;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);


//        binding = ActivityIntroductionBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        auth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();
//        currentUser = auth.getCurrentUser();
//        loadUserData();
    }


//    private void loadUserData() {
//        if (currentUser == null) return;
//
//        String uid = currentUser.getUid();
//        firestore.collection("customer").document(uid)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String name = documentSnapshot.getString("name");
//                        String email = documentSnapshot.getString("email");
//                        String phone = documentSnapshot.getString("phone");
//                        SessionManager.setCustData(IntroductionActivity.this, name, uid, currentUser.getEmail(), phone);
//                        new Handler().postDelayed(
//                                () ->{
//                                    if(SessionManager.getIsLogin(this)){
//                                        goToPageAndClearPrevious(LoginActivity.class);
//                                    }else{
//                                        goToPageAndClearPrevious(LoginActivity.class);
//                                    }
//                                }, 3000
//                        );
//                    }
//                })
//                .addOnFailureListener(e ->
//                        Toast.makeText(this, "Gagal memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
//                );
//    }
}
