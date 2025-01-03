package com.example.music_app.ui.search.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Track(
    var track_image: String,
    var track_title: String,
    var artist_name: String,
    var preview: String,
) : Parcelable, SearchItem()
