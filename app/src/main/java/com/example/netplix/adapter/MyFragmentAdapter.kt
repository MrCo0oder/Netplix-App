package com.example.netplix.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.netplix.ui.MoviesFragment
import com.example.netplix.ui.SearchFragment
import com.example.netplix.ui.TvShowsFragment
import com.example.netplix.ui.WishListFragment

class MyFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MoviesFragment()
            1 -> return TvShowsFragment()
            2 -> return WishListFragment()
            else -> {
                return SearchFragment()
            }
        }

    }
}