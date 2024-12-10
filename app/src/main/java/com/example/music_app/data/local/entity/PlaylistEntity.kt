package com.example.music_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.music_app.ui.playlist.Playlist
import com.example.music_app.ui.search.data.Track

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: Int,
    var name: String,
    var trackCount: Int,
    val tracks: MutableList<Track> = mutableListOf()
)

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        trackCount = this.trackCount,
        tracks = this.tracks.toMutableList()
    )
}
