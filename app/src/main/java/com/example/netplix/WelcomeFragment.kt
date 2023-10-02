package com.example.netplix

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.netplix.databinding.FragmentWelcomeBinding
import com.example.netplix.di.NavigationModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    @Inject
    lateinit var navigationModule: NavigationModule

    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation()
    }

    private fun initNavigation() {
        binding.loginButton.setOnClickListener {
            handleNavigation(R.id.loginFragment)
        }
        binding.signUpButton.setOnClickListener {
            handleNavigation(R.id.signUpFragment)
        }
    }

    private fun handleNavigation(destination: Int) {
        navigationModule.navigateTo(
            destination,
            R.id.register_nav_host_fragment,
        )
    }
}
