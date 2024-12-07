package com.example.music_app.ui.search.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Album(
    val id: Int,
    val album_title: String,
    val artist_name: String,
    val artist_image: String,
    var album_image: String,
    var tracks: @RawValue List<Track>
) : Parcelable