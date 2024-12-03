package com.example.music_app.ui.search.data

import android.os.Parcelable
import com.example.music_app.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    var album_image: Int = R.drawable.ic_launcher_background,
    var track_title: String,
    var artist_name: String,
) :  Parcelable
