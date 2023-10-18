package com.example.netplix.di

import android.app.Activity
import com.example.netplix.R
import com.example.window.*
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DialogModule @Inject constructor(var activity: Activity) {
    @Singleton
    fun initDialog(
        title: String = "",
        body: String,
        bodyColor: Int = R.color.white,
        dialogBackgroundColor: Int? = R.color.black,
        position: Popup.POSITIONS = Popup.POSITIONS.CENTER,
        iconId: Int?,
        isAnimated: Boolean = true,
        pAction: (() -> Unit)? = null,
        pText: String? = null,
        pTextColor: Int? = null,
        pBtnBackgroundRes: Int = R.drawable.rounded_background_white,
        nAction: (() -> Unit)? = null,
        nText: String? = null,
        nBtnBackgroundRes: Int = R.drawable.rounded_background_black,
        nTextColor: Int? = null,
        isCancelable: Boolean = true,
    ) {
        Popup.init(activity)
            .header(title, titleColor = activity.getColor(R.color.white))
            .body(body, titleColor = activity.getColor(bodyColor))
            .background(dialogBackgroundColor?.let { activity.getColor(it) })
            .position(position)
            .icon(iconId)
            .isCancelable(isCancelable)
            .onPositive(pText, buttonBackground = pBtnBackgroundRes, textColor = pTextColor) {
                pAction?.invoke()
            }
            .onNegative(nText, buttonBackground = nBtnBackgroundRes, textColor = nTextColor) {
                nAction?.invoke()
            }.show()
    }
}