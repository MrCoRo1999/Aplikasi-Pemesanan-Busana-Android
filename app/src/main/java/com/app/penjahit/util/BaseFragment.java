package com.app.penjahit.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BaseFragment extends Fragment implements CallBackIntentData {
    private int requestCode;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        onGetData(data, requestCode);
                        requestCode=0;
                    }
                }
            });
    @Override
    public void onGetData(Intent intent,int requestCode) {}

    public void goToPage(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    public void goToPageActivityResult(Class<?> cls, int requestCode) {
        this.requestCode=requestCode;
        Intent intent = new Intent(getActivity(), cls);
        launcher.launch(intent);
    }
    public void goToPageActivityResult(Intent intent, int requestCode) {
        this.requestCode=requestCode;
        launcher.launch(intent);
    }
    public void goToPageActivityResult(Class<?> cls, int requestCode, Bundle bundle) {
        this.requestCode=requestCode;
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        launcher.launch(intent);
    }

    public void goToPageAndClearPrevious(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void goToPageAndClearPrevious(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToPage(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    public String convertToCurrency(long price){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(price);
    }
}
