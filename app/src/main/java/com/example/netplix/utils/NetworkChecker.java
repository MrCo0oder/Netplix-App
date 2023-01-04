package com.example.netplix.utils;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.netplix.R;
import com.google.android.material.snackbar.Snackbar;


public class NetworkChecker extends BroadcastReceiver {
    private Snackbar snackbar1;
    private ImageView imageView;
    private ConnectivityManager connectivityManager;

    public NetworkChecker() {
    }

    public NetworkChecker(@NonNull Activity activity) {
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        snackbar1 = Snackbar.make(activity.findViewById(R.id.snackBar), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(ContextCompat.getColor(activity, R.color.plix_red))
                .setTextColor(Color.WHITE)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        imageView = activity.findViewById(R.id.wifiIV);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            imageView.setVisibility(View.VISIBLE);
            snackbar1.dismiss();
        } else {
            imageView.setVisibility(View.GONE);
            snackbar1.show();
        }

    }

}