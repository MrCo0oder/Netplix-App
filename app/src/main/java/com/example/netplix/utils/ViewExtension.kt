package com.example.netplix.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.netplix.R

fun ImageView.loadImage(
    context: Fragment,
    url: String?,
    placeholder: Int = R.drawable.placeholder,
    hasBase: Boolean = true,
    callback: (Boolean, String) -> Unit
) {
    if (hasBase) {
        /*Picasso.get()
            .load(Constants.IMAGES_BASE + url)
            .placeholder(placeholder).into(this, object : Callback {
                override fun onSuccess() {
                    callback(true, "")
                }

                override fun onError(e: Exception?) {
                    if (e != null) {
                        callback(false, "${e.message.toString()}")
                    }
                }
            })*/
        GlideApp.with(context)
            .load(Constants.IMAGES_BASE + url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (e != null) {
                        callback(false, "${e.message.toString()}")
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(true, "")
                    return false
                }

            })
            .centerCrop()
            .placeholder(placeholder)
            .into(this)
    } else {
        GlideApp.with(context)
            .load(url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (e != null) {
                        callback(false, "${e.message.toString()}")
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(true, "")
                    return false
                }

            })
            .placeholder(placeholder)
            .into(this)
    }
}


fun View.enableView(enable: Boolean, disabledAlpha: Float = 0.5f) {
    if (enable) {
        isEnabled = true
        alpha = 1.toFloat()
    } else {
        isEnabled = false
        alpha = disabledAlpha
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

