package com.example.netplix.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.netplix.R
import com.example.netplix.databinding.FragmentHomeBinding
import com.example.netplix.di.NavigationModule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var navigationModule: NavigationModule
    private lateinit var binding: FragmentHomeBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var scrollThreshold = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationModule = NavigationModule(requireActivity())
        navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setOnItemSelectedListener {
            navController.navigate(it.itemId)
            true
        }

    }
}