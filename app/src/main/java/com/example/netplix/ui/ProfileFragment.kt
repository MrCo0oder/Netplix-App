package com.example.netplix.ui

import android.Manifest
import android.R.attr.button
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.netplix.MainActivity
import com.example.netplix.R
import com.example.netplix.RegisterActivity
import com.example.netplix.database.NetplixDB
import com.example.netplix.databinding.FragmentProfileBinding
import com.example.netplix.di.DatabaseModule
import com.example.netplix.di.DialogModule
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.NavigationModule
import com.example.netplix.ui.movies.MovieViewModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.gone
import com.example.netplix.utils.loadImage
import com.example.netplix.utils.show
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var navigationModule: NavigationModule

    @Inject
    lateinit var dialogModule: DialogModule

    @Inject
    lateinit var appModule: DatabaseModule

    lateinit var backup: RoomBackup
    private lateinit var binding: FragmentProfileBinding
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProvider(requireActivity())[MovieViewModel::class.java]
        firebaseModule.init(requireActivity())

        initLogoutButton()
        initDeleteAccountButton()
        initUploadPic()
        displayUserInfo()
        displayZPic()
        binding.apply {
            profilePicProgress.root.background = null
            infoProgress.root.background = null
        }
//        restore(appModule.dbService(requireActivity().application))
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initBackupButton() {
        val fragmentActivity = (requireActivity() as MainActivity)
//        backup = fragmentActivity.backup
//        backUp(appModule.dbService(requireActivity().application))
        binding.uploadImagView.setOnClickListener {
            showMenu(it)
            if (checkStoragePermissions()) {
            } else {
                requestStoragePermissionsExport()
                Log.d("exportData: ", "444444")

            }
        }
    }


    private fun showMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.getMenuInflater().inflate(R.menu.bottom_navigation, popupMenu.getMenu())
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                // Toast message on menu item clicked
                Toast.makeText(
                    requireContext(),
                    "You Clicked " + menuItem.title,
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        })
        popupMenu.show()
    }

    private fun initUploadPic() {
        binding.cameraImageView.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .compress(720)
                .maxResultSize(1080, 1080)
                .start()
        }
    }

    private fun displayZPic() {
        binding.profilePicImageView.setOnClickListener {
            navigationModule.navigateTo(
                R.id.zoomFragment,
                R.id.nav_host_fragment,
                Bundle().apply {
                    putString(
                        Constants.POSTER_URL,
                        firebaseModule.getAuth().currentUser?.photoUrl.toString()
                    )
                },
            )
        }

    }

    private fun initLogoutButton() {
        binding.logoutImageView.isClickable = true
        binding.logoutImageView.setOnClickListener {
            firebaseModule.logOut()
            gotoWelcomeScreen()
//            handleNavigation(R.id.welcomeFragment, true)
        }
    }

    private fun initDeleteAccountButton() {
        binding.deleteAccountImageView.setOnClickListener {
            firebaseModule.deleteAccount() { isDeleted, message ->
                if (isDeleted) {

                    gotoWelcomeScreen()
                } else {
                    dialogModule.initDialog(
                        getString(R.string.ops),
                        message,
                        R.color.white,
                        iconId = R.drawable.access_denied,
                        pAction = ::tryAgain,
                        pText = getString(R.string.try_again),
                        nText = getString(R.string.cancel)
                    )
                }
            }
        }
    }

    private fun tryAgain() {
        firebaseModule.deleteAccount() { _, _ ->
        }
    }

    private fun gotoWelcomeScreen() {
        requireActivity().startActivity(Intent(requireActivity(), RegisterActivity::class.java))
        requireActivity().finish()
    }

    private fun handleNavigation(destination: Int, clearBackStack: Boolean) {
        navigationModule.navigateTo(
            destination,
            R.id.nav_host_fragment,
            clearBackStack = clearBackStack,
        )
    }

    private fun displayUserInfo() {
        firebaseModule.getUserInfo { task ->
            binding.infoProgress.root.gone()
//            Log.d(this.javaClass.simpleName, "displayUserInfo: ".plus(task.result.get("imageUrl")))

            if (task.isSuccessful) {
                if (task.result != null) {
                    mapDataToViews(task)
                    binding.group.show()
                }
//                else
                //no data
            } else {
                Log.d(this.javaClass.simpleName, "displayUserInfo: ".plus(task.exception))
            }
        }
    }

    private fun mapDataToViews(task: Task<DocumentSnapshot>) {
        binding.apply {
            welcomeTextView.text = buildString {
                append(getString(R.string.welcome))
                append(" ")
                append(task.result.get("firstName"))
            }
            fullNameTextView.text = buildString {
                append(task.result.get("firstName"))
                append(" ")
                append(task.result.get("secondName"))
            }
            emailTextView.text = task.result.get("email").toString()
            phoneTextView.text = task.result.get("phone").toString()
            genderTextView.text = task.result.get("gender").toString()
            when (task.result.get("gender").toString()) {
                "Male" -> loadProfileImage(0)
                else -> loadProfileImage(1)
            }

        }
    }

    private fun loadProfileImage(gender: Int) {
        firebaseModule.getUserPic(gender) { task ->
            if (task.isSuccessful) {
                binding.profilePicProgress.root.gone()
                binding.apply {
                    profilePicImageView.loadImage(
                        this@ProfileFragment,
                        firebaseModule.getAuth().currentUser?.photoUrl.toString(),
                        placeholder = R.drawable.imge,
                        hasBase = false
                    ) { state, m ->
                        if (!state) {
                            Log.d("imageLoad", m)
                        }
                    }
                }
            } else {
                binding.profilePicProgress.root.gone()
                Log.d("displayUser", task.exception.toString())
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            var uri: Uri?
            if (data != null) {
                uri = data.data
                uploadProfilePic(uri)
            }
        }
    }

    fun uploadProfilePic(uri: Uri?) {
        if (uri != null) {
            val storageReference =
                FirebaseStorage.getInstance().getReference(firebaseModule.getAuth().uid + "/pic")
            storageReference.putFile(uri)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    val profileUpdates: UserProfileChangeRequest
                    profileUpdates = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
                    firebaseModule.getAuth().currentUser?.updateProfile(profileUpdates)
                    binding.profilePicImageView.setImageURI(uri)
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkStoragePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestStoragePermissionsExport() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE), 1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "upload",
                        Toast.LENGTH_SHORT
                    ).show()
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