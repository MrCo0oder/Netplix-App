package com.example.netplix.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.netplix.R
import com.example.netplix.utils.Constants.Companion.IMAGES_BASE
import com.squareup.picasso.Picasso

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String?) {
    val fullUrl: String = IMAGES_BASE + url
    Picasso.get().load(fullUrl).placeholder(R.drawable.placeholder).into(view)
}