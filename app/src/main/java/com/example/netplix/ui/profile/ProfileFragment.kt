package com.example.netplix.ui.profile

import android.app.Activity
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.netplix.R
import com.example.netplix.RegisterActivity
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var navigationModule: NavigationModule

    @Inject
    lateinit var dialogModule: DialogModule

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
    }

    private fun showMenu() {
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
//                .galleryOnly()
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
        }
    }

    private fun initDeleteAccountButton() {
        binding.deleteAccountImageView.setOnClickListener {
            deleteAcount()
        }
    }

    private fun deleteAcount() {
        firebaseModule.deleteAccount() { isDeleted, message ->
            if (isDeleted) {
                gotoWelcomeScreen()
            } else {
                dialogModule.initDialog(
                    getString(R.string.ops),
                    message,
                    R.color.white,
                    iconId = R.drawable.access_denied,
                    pAction = null,
                    pText = getString(R.string.try_again),
                    pBtnBackgroundRes = R.drawable.rounded_background_white,
                    pTextColor = requireActivity().getColor(R.color.black),
                )
            }
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
            if (task.result.get("photoUrl").toString().isNullOrEmpty().not())
                loadProfileImage(task.result.get("photoUrl").toString())

        }
    }

    private fun loadProfileImage(url: String) {
        try {
            binding.profilePicImageView.loadImage(
                this,
                url,
                hasBase = false,
                placeholder = R.drawable.ic_profile
            ) { isComplete, _ ->
                if (isComplete)
                    binding.profilePicProgress.root.gone()

            }

        } catch (e: Exception) {
            Log.e("TAG", "loadProfileImage: ", e)
            binding.profilePicProgress.root.gone()
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
                    it.task.result.storage.downloadUrl.addOnSuccessListener {
                        firebaseModule.updateProfileImage(it.toString()) { status, m ->
                            if (status) {
                                binding.profilePicImageView.setImageURI(uri)
                            } else {
                                Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                })
        }
    }
}