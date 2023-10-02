package com.example.netplix

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.netplix.databinding.ActivityMainBinding
import com.example.netplix.di.DatabaseModule
import com.example.netplix.di.DialogModule
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.NavigationModule
import com.example.netplix.di.PermissionModule
import com.example.netplix.utils.NetworkChecker
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.util.jar.Manifest
import javax.inject.Inject
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var networkChecker: NetworkChecker
    lateinit var fragmentManager: FragmentManager

    @Inject
    lateinit var dialogModule: DialogModule

    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var navigationModule: NavigationModule


    @Inject
    lateinit var appModule: DatabaseModule

//    public lateinit var backup: RoomBackup
    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
//        Thread.sleep(3000)
        setContentView(binding.root)
        //Network Checker
        supportActionBar?.hide()
        checkNetworkConnection()
        handleStartDestinationFragment()
        handleOnBackPressed()
//        backup = RoomBackup(this)

    }

    private fun checkNetworkConnection() {
        networkChecker = NetworkChecker(this)
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun handleOnBackPressed() {
        val callback: OnBackPressedCallback =
            object :
                OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.homeFragment) {
                        dialogModule.initDialog(
                            getString(R.string.ops),
                            "Bye 👋",
                            R.color.white,
                            iconId = R.drawable.ic_sad,
                            pAction = { exitProcess(0) },
                            pText = "Bye 👋",
                            nText = getString(R.string.cancel)
                        )
                    } else {
                        navigationModule.popBack()
                    }
                }
            }
        getOnBackPressedDispatcher()
            .addCallback(this, callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChecker)
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun handleStartDestinationFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.app_nav)
        navController = navHostFragment.navController

        val destination =
            if (firebaseModule.getAuth().currentUser != null) R.id.homeFragment else R.id.welcomeFragment
        navGraph.setStartDestination(destination)
        navController.graph = navGraph
    }
}