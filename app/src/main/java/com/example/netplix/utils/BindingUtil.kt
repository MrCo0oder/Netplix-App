package com.example.netplix.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.example.netplix.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("image")
fun loadImage(view:ImageView , url:String) {
    val fullUrl: String = "https://image.tmdb.org/t/p/w500" + url
    Picasso.get().load(fullUrl).placeholder(R.drawable.placeholder).into(view, object :
        Callback {
        override fun onSuccess() {
        }
        override fun onError(e: Exception) {
        }
    })
}