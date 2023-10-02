package com.example.netplix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.netplix.R
import com.example.netplix.databinding.FragmentHomeBinding
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.NavigationModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var navModule: NavigationModule

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
        initAppBar()
        initFab()
        handleNavigation()
    }

    private fun handleNavigation() {
        navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setOnItemSelectedListener {
            navController.navigate(it.itemId)
            true
        }
    }

    private fun initAppBar() {
        binding.bottomNavigationView.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }
    }

    private fun initFab() {
        binding.floatingActionButton
            .setOnClickListener {
                binding.bottomNavigationView.menu.findItem(R.id.profileFragment).setChecked(true)
                navController.navigate(R.id.profileFragment)
            }
    }
}