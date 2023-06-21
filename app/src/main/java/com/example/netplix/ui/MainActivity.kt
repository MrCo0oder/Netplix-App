package com.example.netplix.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2.*
import com.example.netplix.R
import com.example.netplix.adapter.MyFragmentAdapter
import com.example.netplix.databinding.ActivityMainBinding
import com.example.netplix.di.NavigationModule
import com.example.netplix.utils.NetworkChecker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var fragmentsAdapter: MyFragmentAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var networkChecker: NetworkChecker
    lateinit var fragmentManager: FragmentManager
    lateinit var navigationModule: NavigationModule
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
//        Thread.sleep(3000)
        setContentView(binding.root)
        //Network Checker
        supportActionBar?.hide()
        networkChecker = NetworkChecker(this)
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChecker)
    }

    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setTitle(getText(com.example.netplix.R.string.exit))
//        val yes = getText(com.example.netplix.R.string.yes)
//        val no = getText(com.example.netplix.R.string.no)
//        alertDialogBuilder.setCancelable(true).setIcon(com.example.netplix.R.mipmap.ic_launcher)
//            .setPositiveButton(yes) { _, _ ->
//                moveTaskToBack(true)
//                Process.killProcess(Process.myPid())
//                System.exit(1)
//                unregisterReceiver(networkChecker)
//            }.setNegativeButton(no) { dialog, _ -> dialog.cancel() }
//        alertDialogBuilder.create().show()
//    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}