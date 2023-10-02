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
    private lateinit var backup: RoomBackup
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
        handleNavigationToSignUp()
        loginButtonSetup()
        forgetPasswordDialog()
        loginViewModel = loginViewModel()
        handleFormValidation()
        val fragmentActivity = (requireActivity() as RegisterActivity)
        backup = fragmentActivity.backup

        if (checkStoragePermissions()) {
            importData()
        } else {
            requestStoragePermissionsImport()
        }
    }

    private fun importData() {
        TODO("Not yet implemented")
    }

    private fun restoreZData() {
        val storage = FirebaseStorage.getInstance()
        val NETPLIX_DB =
            storage.getReferenceFromUrl("gs://netplix-a2240.appspot.com/" + firebaseModule.getAuth().uid + "/" + Constants.NETPLIX_DB)
        val NETPLIX_DB_EX =
            storage.getReferenceFromUrl("gs://netplix-a2240.appspot.com/" + firebaseModule.getAuth().uid + "/" + Constants.NETPLIX_DB + "-wal")
        val rootPath = File(
            "/data/data/com.example.netplix/database/"
        )
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val localFile = File(rootPath, Constants.NETPLIX_DB)
        val exFile = File(rootPath, Constants.NETPLIX_DB + "-wal")
        NETPLIX_DB_EX.getFile(exFile).addOnCompleteListener {
            if (it.isSuccessful) {
                NETPLIX_DB.getFile(localFile).addOnSuccessListener {
                    restore(dbModule.dbService(requireActivity().application))
                }.addOnFailureListener {
                    // Handle any errors
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    it.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    fun restore(db: NetplixDB) {
        backup
            .database(db)
            .enableLogDebug(true)
            .backupIsEncrypted(false)
            .customBackupFileName(Constants.NETPLIX_DB)
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_INTERNAL)
            .maxFileCount(1)
            .onCompleteListener { success, m, _ ->
                Toast.makeText(
                    requireActivity(),
                    m,
                    Toast.LENGTH_SHORT
                ).show()
                backup.restartApp(requireActivity().intent)
                if (success) {
                }
            }
            .restore()
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
                        restoreZData()
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

    private fun checkStoragePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }

    private fun requestStoragePermissionsImport() {
        val storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(requireActivity(), storagePermission, 2)
    }



    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            2 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importData()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Storage Permission Required...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}