package com.example.netplix.di

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import com.example.netplix.R
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NavigationModule @Inject constructor(
    var activity: Activity
) {
    @Singleton
    fun navigateTo(
        destination: Int,
        navHostId: Int,
        bundle: Bundle? = null,
        extras: Navigator.Extras? = null,
        clearBackStack: Boolean = false,
        navOptions: NavOptions? = null
    ) {
        try {
            if (clearBackStack) {
                Navigation.findNavController(activity, navHostId).backQueue.clear()
            }
            Navigation.findNavController(activity, navHostId)
                .navigate(destination, bundle, navOptions, extras)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun popBack(navHostId: Int = R.id.nav_host_fragment) {
        try {
            Navigation.findNavController(activity, navHostId).popBackStack()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}