package com.example.netplix.di

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.netplix.R
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.PermissionBuilder

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton



class PermissionModule  constructor(
    private var context: Context,
    private var activity: Activity,
    private var fragment: Fragment?,
    private var dialogModule: DialogModule,
) {
    private lateinit var permissionName: String
    private lateinit var callBack: (entity: Boolean) -> Unit
    private var permissionNameList: List<String>? = null
    private var requiredPermission: Boolean = true

    fun init(
        permissionName: String,
        requiredPermission: Boolean = true,
        callBack: (entity: Boolean) -> Unit,

        ) {
        if (checkCustomPermission(permissionName))
            callBack(true)
        else {
            this.permissionName = permissionName
            this.callBack = callBack
            this.requiredPermission = requiredPermission
            doRequestPermission()
        }
    }

    fun init(
        permissionNameList: List<String>,
        requiredPermission: Boolean = true,
        callBack: (entity: Boolean) -> Unit,
    ) {
        handleDinedPermissionList(permissionNameList)
        this.requiredPermission = requiredPermission
        if (this.permissionNameList.isNullOrEmpty()) {
            callBack(true)
        } else {
            this.callBack = callBack

            doRequestPermission()
        }
    }

    private fun handleDinedPermissionList(permissions: List<String>) {
        val deniedList = mutableListOf<String>()
        permissions.forEach { item ->
            if (!checkCustomPermission(item))
                deniedList.add(item)
        }
        if (deniedList.isNotEmpty())
            permissionNameList = deniedList
    }

     fun initPermissionX(): PermissionMediator {
        return if (fragment == null)
            PermissionX.init((activity as FragmentActivity))
        else PermissionX.init(fragment!!)
    }
     fun getPermission(): PermissionBuilder? {
        return try {
            if (permissionNameList == null)
                initPermissionX().permissions(permissionName)
            else initPermissionX().permissions(permissionNameList!!)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun doRequestPermission() {
        getPermission()?.let {
            it.onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "permission",
                    context.getString(R.string.yes),
                    context.getString(R.string.cancel)
                )
            }
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        callBack(true)
                    } else {
                        if (requiredPermission)
                            showSetting(deniedList)
                        else callBack(false)
                    }
                }
        }

    }

    private fun showSetting(deniedList: List<String>) {
        dialogModule.initDialog(
            "Permission",
            getDeniedDialogPermissionList(deniedList).toString(),
            iconId = R.drawable.access_denied,
            pText = "ok", pAction = ::pov
        )
    }

    private fun pov() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri =
                Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }, 500)
        callBack(false)
    }

    private fun getDeniedDialogPermissionList(deniedList: List<String>): MutableList<String> {
        val list = mutableListOf<String>()
        deniedList.forEach {
            list.add(it.split(".").last())
        }
        return list
    }

    private fun doCheckPermissionIfDenied(deniedList: List<String>) {

    }

    private fun checkCustomPermission(permissionName: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionName
        ) == PackageManager.PERMISSION_GRANTED
    }
}