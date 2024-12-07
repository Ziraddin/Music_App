package com.example.music_app.ui.playlist

import android.os.Parcelable
import com.example.music_app.ui.search.data.Track
import kotlinx.parcelize.Parcelize
import kotlin.random.Random


@Parcelize
data class Playlist(
    val id: Int = Random.nextInt(),
    var name: String,
    var trackCount: Int = 0,
    var tracks: MutableList<Track> = mutableListOf()
) : Parcelable
