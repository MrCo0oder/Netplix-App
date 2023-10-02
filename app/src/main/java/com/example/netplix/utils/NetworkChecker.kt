package com.example.netplix.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import com.example.netplix.R
import com.google.android.material.snackbar.Snackbar

class NetworkChecker : BroadcastReceiver {
    private var snackbar1: Snackbar? = null
    private var connectivityManager: ConnectivityManager? = null

    constructor() {}
    constructor(activity: Activity) {
        connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        snackbar1 = Snackbar.make(
            activity.findViewById(R.id.snackBar),
            "No Internet Connection",
            Snackbar.LENGTH_INDEFINITE
        )
            .setBackgroundTint(ContextCompat.getColor(activity, R.color.plix_red))
            .setTextColor(Color.WHITE)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (connectivityManager!!.activeNetworkInfo != null && connectivityManager!!.activeNetworkInfo!!
                .isConnected
        ) {
            snackbar1!!.dismiss()
        } else {
            snackbar1!!.show()
        }
    }
}