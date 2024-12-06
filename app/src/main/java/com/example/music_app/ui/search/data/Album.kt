package com.example.music_app.ui.search.data

import android.os.Parcelable
import com.example.music_app.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Album(
    val id: Int,
    val album_title: String,
    val artist_name: String,
    var album_image: Int = R.drawable.ic_launcher_background,
    var tracks: @RawValue List<Track>
) : Parcelable