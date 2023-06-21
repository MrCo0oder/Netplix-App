package com.example.netplix.di

import android.app.Activity
import android.os.Bundle
import androidx.navigation.Navigation
import com.example.netplix.ui.MainActivity


class NavigationModule(
    private var activity: Activity = MainActivity()
) {


    fun navigateTo(
        destination: Int,
        navHostId: Int,
        bundle: Bundle? = null
    ) {
        try {
            activityNavController(navHostId).navigate(destination, bundle, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun popBack(destination: Int? = null, navHostId: Int? = null) {
        try {
            if (navHostId != null) {
                destination?.let { activityNavController(navHostId).popBackStack(it, true) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun activityNavController(navHostId: Int) =
        Navigation.findNavController(activity, navHostId)
}