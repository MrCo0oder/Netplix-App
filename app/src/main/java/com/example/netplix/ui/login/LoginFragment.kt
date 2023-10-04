package com.example.netplix.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.MainActivity
import com.example.netplix.R
import com.example.netplix.RegisterActivity
import com.example.netplix.database.NetplixDB
import com.example.netplix.databinding.FragmentLoginBinding
import com.example.netplix.di.DatabaseModule
import com.example.netplix.di.DialogModule
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.NavigationModule
import com.example.netplix.utils.Constants
import com.example.netplix.utils.ForgetPasswordDialog
import com.example.netplix.utils.enableView
import com.example.netplix.utils.gone
import com.example.netplix.utils.show
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject
    lateinit var navigationModule: NavigationModule

    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var dialogModule: DialogModule

    @Inject
    lateinit var dbModule: DatabaseModule

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationModule.init(requireActivity())
        handleNavigationToSignUp()
        loginButtonSetup()
        forgetPasswordDialog()
        loginViewModel = loginViewModel()
        handleFormValidation()
    }




    private fun loginViewModel() = ViewModelProvider(this)[LoginViewModel::class.java]

    private fun handleFormValidation() {
        loginViewModel.initValidation(binding)
        loginViewModel.isValidForm().observe(viewLifecycleOwner) {
            binding.loginButton.enableView(it)
        }
    }

    private fun loginButtonSetup() {
        binding.loginButton.setOnClickListener {
            handleLogin()
            binding.progressCardView.root.show()
        }
    }

    private fun handleLogin() {
        binding.apply {
            var email = emailTextInputEditText.text.toString()
            var password = passwordTextInputEditText.text.toString()
            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                firebaseModule.login(
                    email,
                    password
                ) { isSuccessful, message ->
                    binding.progressCardView.root.gone()
                    if (isSuccessful) {
                        dialogModule.initDialog(
                            getString(R.string.welcome_back),
                            "",
                            R.color.white,
                            iconId = R.drawable.welcoming,
                            pAction = ::gotoHomeFragment,
                            pText = getString(R.string.go_to_home),
                            nAction = ::gotoHomeFragment
                        )
                    } else {
                        dialogModule.initDialog(
                            getString(R.string.ops),
                            message,
                            R.color.white,
                            iconId = R.drawable.access_denied,
                            pAction = null,
                            pText = getString(R.string.try_again),
                            nText = getString(R.string.cancel)
                        )
                    }
                }
            }
        }
    }

    private fun handleNavigationToSignUp() {

        binding.gotoSignUpButton
            .setOnClickListener {
                handleNavigation(
                    R.id.signUpFragment,
                    clearBackStack = true
                )
            }
    }

    private fun gotoHomeFragment() {
//        handleNavigation(R.id.homeFragment, true)
        requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun handleNavigation(destination: Int, clearBackStack: Boolean) {
        navigationModule.navigateTo(
            destination,
            R.id.register_nav_host_fragment,
            clearBackStack = clearBackStack
        )
    }

    private fun forgetPasswordDialog() {
        binding.forgetPasswordButton.setOnClickListener {
            ForgetPasswordDialog().show(childFragmentManager, "forgetPassword dialog")
        }
    }

}