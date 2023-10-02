package com.example.netplix

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.netplix.databinding.ActivityRegisterBinding
import com.example.netplix.di.DatabaseModule
import com.example.netplix.di.FirebaseModule
import com.example.netplix.utils.NetworkChecker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseModule: FirebaseModule
    @Inject
    lateinit var appModule: DatabaseModule

    public lateinit var backup: RoomBackup
    lateinit var networkChecker: NetworkChecker
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        Thread.sleep(1000)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        networkChecker = NetworkChecker(this)
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        backup = RoomBackup(this)
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChecker)
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}