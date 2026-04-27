package com.app.penjahit.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.app.penjahit.databinding.ActivityIntroductionBinding;
import com.app.penjahit.databinding.ActivityProfileBinding;
import com.app.penjahit.util.BaseAppCompat;
import com.app.penjahit.util.SessionManager;

public class ProfileActivity extends BaseAppCompat {
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogout.setOnClickListener(view -> {
            goToPageAndClearPrevious(IntroductionActivity.class);
            SessionManager.clearData(ProfileActivity.this);
        });
        binding.tvName.setText(SessionManager.getName(this));
        binding.tvEmail.setText(SessionManager.getEmail(this));
        binding.tvPhone.setText(SessionManager.getNoHp(this));
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPage(EditProfileActivity.class);
            }
        });
    }
}
