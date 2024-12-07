package com.example.music_app.core.constants

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.music_app.R

object SharedFunctions {

    //Fetch Image from Url
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context).load(imageUrl).placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background).into(imageView)
    }
}

