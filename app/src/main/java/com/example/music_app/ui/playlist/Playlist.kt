package com.example.music_app.ui.playlist

import android.os.Parcelable
import com.example.music_app.data.local.entity.PlaylistEntity
import com.example.music_app.ui.search.data.Track
import kotlinx.parcelize.Parcelize
import kotlin.random.Random


@Parcelize
data class Playlist(
    val id: Int = Random.nextInt(),
    var name: String,
    var trackCount: Int = 0,
    val tracks: MutableList<Track> = mutableListOf()
) : Parcelable

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        trackCount = this.trackCount,
        tracks = this.tracks.toMutableList()
    )
}