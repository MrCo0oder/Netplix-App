package com.example.netplix.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.netplix.R
import com.example.netplix.databinding.DialogForgetPasswordBinding
import com.example.netplix.di.FirebaseModule
import com.example.netplix.di.DialogModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgetPasswordDialog : AppCompatDialogFragment() {
    @Inject
    lateinit var firebaseModule: FirebaseModule

    @Inject
    lateinit var dialogModule: DialogModule

    lateinit var binding: DialogForgetPasswordBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().getLayoutInflater()
        binding = DialogForgetPasswordBinding.inflate(inflater, null, false)
        builder.setView(binding.root)
        builder.setPositiveButton("Reset") { dialog, _ ->
            if (!binding.username.editText?.text.toString().isNullOrEmpty()) {
                firebaseModule.resetPassword(binding.username.editText?.text.toString()) { b, m ->
                    if (b) {
                        dialog.dismiss()
                        dialogModule.initDialog(
                            body = m,
                            dialogBackgroundColor = R.color.white,
                            iconId = R.drawable.ic_launcher_foreground2,
                            pText = getString(R.string.check_your_emails)
                        )
                    } else {
                        dialogModule.initDialog(
                            getString(R.string.ops),
                            m,
                            R.color.white,
                            iconId = R.drawable.ic_launcher_foreground2,
                            pText = getString(R.string.try_again)
                        )
                    }
                }
                return@setPositiveButton
            }

        }
        builder.setNegativeButton(
            getString(R.string.cancel),
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
        return builder.create()
    }
}