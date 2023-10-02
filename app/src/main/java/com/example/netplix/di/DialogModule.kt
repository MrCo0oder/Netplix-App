package com.example.netplix.di

import android.app.Activity
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.background
import com.example.awesomedialog.body
import com.example.awesomedialog.icon
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.position
import com.example.awesomedialog.title
import com.example.netplix.R
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
        dialogBackgroundColor: Int? = R.drawable.rounded_background,
        position: AwesomeDialog.POSITIONS = AwesomeDialog.POSITIONS.CENTER,
        iconId: Int,
        isAnimated: Boolean = true,
        pAction: (() -> Unit)? = null,
        pText: String,
        nAction: (() -> Unit)? = null,
        nText: String = ""
    ) {
        AwesomeDialog.build(activity)
            .title(title, titleColor = R.color.white)
            .body(body, color = R.color.white)
            .background(dialogBackgroundColor)
            .position(position)
            .icon(iconId, isAnimated)
            .onPositive(pText, R.drawable.rounded_background) {
                pAction?.invoke()
            }
            .onNegative(nText, R.color.white, textColor = R.color.plix_red) {
                nAction?.invoke()
            }
    }
}