package com.example.netplix.ui.profile

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.netplix.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val firebaseModule: FirebaseModule) :
    ViewModel() {
    fun init(activity: Activity) {
        firebaseModule.init(activity)
    }


    override fun onCleared() {
        super.onCleared()
//        firebaseModule.destroy()
    }
}