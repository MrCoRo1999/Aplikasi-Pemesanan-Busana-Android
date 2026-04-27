package com.app.penjahit.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.app.penjahit.R;


public class LoadingPopUp {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingPopUp(Activity myactivity) {
        activity = myactivity;
    }

    public void startLoadingDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.pop_up_loading, null);
        builder.setView(view);
        TextView textView = (TextView) view.findViewById(R.id.tvTitle);
        builder.setCancelable(false);
        if (message.isEmpty()) {
            textView.setText("Loading");
        } else {
            textView.setText(message);
        }

        alertDialog = builder.create();
        alertDialog.show();
    }


    public void dismisDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
