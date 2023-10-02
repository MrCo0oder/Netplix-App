package com.example.netplix.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.MainActivity
import com.example.netplix.R
import com.example.netplix.databinding.FragmentSignUpBinding
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.DialogModule
import com.example.netplix.di.NavigationModule
import com.example.netplix.utils.enableView
import com.example.netplix.utils.gone
import com.example.netplix.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var navigationModule: NavigationModule

    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var dialogModule: DialogModule
    private lateinit var viewModel: SignupViewModel

    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseModule.init(requireActivity())
        initViewModel()
        initNavigation()
        initGenderDropDown()
        initValidation()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
    }


    private fun initNavigation() {
        binding.gotoLoginButton
            .setOnClickListener {
                handleNavigation(R.id.loginFragment)
            }
        binding.signUpButton.setOnClickListener {
            handleSigningUp()
            binding.progressCardView.root.show()
        }
    }

    private fun handleSigningUp() {
        binding.apply {
            var email = emailTextInputEditText.text.toString()
            var password = passwordTextInputEditText.text.toString()
            var firstName = nameTextInputEditText.text.toString()
            var secondName = secondNameTextInputEditText.text.toString()
            var gender = genderAutoCompleteTextView.text.toString()
            var phone = phoneTextInputEditText.text.toString()
            if (!email.isNullOrEmpty()
                && !password.isNullOrEmpty()
                && !firstName.isNullOrEmpty()
                && !secondName.isNullOrEmpty()
                && !gender.isNullOrEmpty()
                && !phone.isNullOrEmpty()
            ) {
                firebaseModule.signUp(
                    email,
                    password,
                    firstName,
                    secondName,
                    gender,
                    phone
                ) { _, isSuccessful, message ->
                    binding.progressCardView.root.gone()
                    if (isSuccessful) {
                        dialogModule.initDialog(
                            getString(R.string.congrats), buildString {
                                append(getString(R.string.welcome))
                                append(firstName)
                                append("\n")
                                getString(R.string.your_account_created_successfully)
                            },
                            R.color.white,
                            iconId = R.drawable.account_created_successfully,
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

    private fun gotoHomeFragment() {
//        handleNavigation(R.id.homeFragment, true)
        requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun handleNavigation(destination: Int) {
        navigationModule.navigateTo(
            destination,
            R.id.register_nav_host_fragment,
            clearBackStack = true
        )
    }

    private fun initGenderDropDown() {
        val type = arrayOf(getString(R.string.male), getString(R.string.female))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu_popup_item, type)
        binding.genderAutoCompleteTextView.setAdapter<ArrayAdapter<String>>(adapter)

    }

    private fun initValidation() {
        viewModel.initValidation(binding)
        viewModel.isValidForm().observe(viewLifecycleOwner) {
            binding.signUpButton.enableView(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseModule.destroy()
    }
}