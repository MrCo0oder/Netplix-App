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
class NavigationModule @Inject constructor() {

    private var activity: Activity? = null
    fun init(activity: Activity?) {
        this.activity = activity
    }

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
                activity?.let { Navigation.findNavController(it, navHostId).backQueue.clear() }
            }
            activity?.let {
                Navigation.findNavController(it, navHostId)
                    .navigate(destination, bundle, navOptions, extras)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun popBack(navHostId: Int = R.id.nav_host_fragment) {
        try {
            activity?.let { Navigation.findNavController(it, navHostId).popBackStack() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun destroy() {
        activity = null
    }
}