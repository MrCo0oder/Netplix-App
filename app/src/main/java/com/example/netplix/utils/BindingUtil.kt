package com.example.netplix.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.netplix.R
import com.example.netplix.utils.Constants.Companion.IMAGES_BASE
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String?) {
    val fullUrl: String = IMAGES_BASE + url
    Picasso.get().load(fullUrl).placeholder(R.drawable.placeholder).into(view)
}

fun ImageView.loadImage(
    url: String?,
    placeholder: Int = R.drawable.placeholder,
    hasBase: Boolean = true,
    callback: (Boolean, String) -> Unit
) {
    if (hasBase) {
        Picasso.get()
            .load(IMAGES_BASE + url)
            .placeholder(placeholder).into(this, object : Callback {
                override fun onSuccess() {
                    callback(true, "")
                }

                override fun onError(e: Exception?) {
                    if (e != null) {
                        callback(false, "${e.message.toString()}")
                    }
                }
            })
    } else {
        Picasso.get()
            .load(url)
            .placeholder(placeholder).into(this, object : Callback {
                override fun onSuccess() {
                    callback(true, "")
                }

                override fun onError(e: Exception?) {
                    if (e != null) {
                        callback(false, "${e.message.toString()}")
                    }
                }
            })
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