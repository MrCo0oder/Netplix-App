package com.example.netplix.ui

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.netplix.adapter.MyFragmentAdapter
import com.example.netplix.databinding.ActivityMainBinding
import com.example.netplix.utils.NetworkChecker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class MainActivity : AppCompatActivity() {

    lateinit var fragmentsAdapter: MyFragmentAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var networkChecker:NetworkChecker
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Network Checker Broadcast Receiver
        networkChecker = NetworkChecker(this)
        registerReceiver(networkChecker, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Movies"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("TV Shows"));
       //disable swiping
        binding.viewPager2.setUserInputEnabled(false)


        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentsAdapter = MyFragmentAdapter(fragmentManager, lifecycle)
        binding.viewPager2.adapter = fragmentsAdapter
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
               binding.viewPager2.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab( binding.tabLayout.getTabAt(position))
            }
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChecker)
    }
    }


/*  private fun loadData(type: String) {
        binding.swipe.setRefreshing(true);
        getMovie.getMovie().second.observe(this, object : Observer<String> {
            override fun onChanged(t: String) {

            }
        })
        getMovie.getMovie().first.observe(this){
            moviesList = it.results
        }

        if (type.equals("Refresh")) {
            binding.recyclerView.apply {
                adapter = Adapter(baseContext, moviesList)
                binding.recyclerView.adapter = adapter
                binding.swipe.isRefreshing = false
                }
            adapter.notifyDataSetChanged()

        } else
        {
            try {
                binding.recyclerView.apply {
                    adapter = Adapter(baseContext, moviesList)
                    binding.recyclerView.adapter = adapter
                    binding.swipe.isRefreshing = false
                }
            }
            catch (e: Exception) {
                Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }*/




