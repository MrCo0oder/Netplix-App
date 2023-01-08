package com.example.netplix.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2.*
import com.example.netplix.R
import com.example.netplix.adapter.MyFragmentAdapter
import com.example.netplix.databinding.ActivityMainBinding
import com.example.netplix.utils.NetworkChecker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.*
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var fragmentsAdapter: MyFragmentAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var networkChecker: NetworkChecker
    lateinit var fragmentManager: FragmentManager

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Network Checker
        networkChecker = NetworkChecker(this)
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        //disable swiping
        binding.viewPager2.setUserInputEnabled(false)
        fragmentManager = supportFragmentManager
        fragmentsAdapter = MyFragmentAdapter(fragmentManager, lifecycle)
        binding.viewPager2.adapter = fragmentsAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getText(R.string.movies)
                    tab.icon = getDrawable(R.drawable.ic_movies)
                    tab.select()
                }
                1 -> {
                    tab.text = getText(R.string.tv_shows)
                    tab.icon = getDrawable(R.drawable.ic_tv_show)
                    tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
                }
                2 -> {
                tab.text = getText(R.string.wish_list)
                tab.icon = getDrawable(R.drawable.heart)
                tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
            }
                else -> {
                    tab.text = getText(R.string.search)
                    tab.icon = getDrawable(R.drawable.ic_search)
                    tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
                }
            }
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager2.currentItem = tab.position
                tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_LABELED
                tab.select()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.tabLabelVisibility = TAB_LABEL_VISIBILITY_UNLABELED
            }

            override fun onTabReselected(tab: TabLayout.Tab) {


            }
        })

        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChecker)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getText(R.string.exit))
        val yes = getText(R.string.yes)
        val no = getText(R.string.no)
        alertDialogBuilder.setCancelable(true).setIcon(R.mipmap.ic_launcher)
            .setPositiveButton(yes) { _, _ ->
                moveTaskToBack(true)
                Process.killProcess(Process.myPid())
                System.exit(1)
                unregisterReceiver(networkChecker)
            }.setNegativeButton(no) { dialog, _ -> dialog.cancel() }
        alertDialogBuilder.create().show()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}








