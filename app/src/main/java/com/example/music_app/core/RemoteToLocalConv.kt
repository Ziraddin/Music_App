package com.example.music_app.core

import com.example.music_app.data.remote.model.AlbumR
import com.example.music_app.data.remote.model.TrackR
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track

object RemoteToLocalConv {

    fun convertAlbumRToAlbums(albumRList: List<AlbumR>): List<Album> {
        return albumRList.map { albumR ->
            Album(
                id = albumR.id,
                album_title = albumR.title,
                artist_name = albumR.artist.name,
                album_image = albumR.cover_big,
                artist_image = albumR.artist.picture_big,
                tracks = emptyList()
            )
        }
    }

    fun convertTrackRToTracks(trackRList: List<TrackR>): List<Track> {
        return trackRList.map { trackR ->
            convertTrackRToTrack(trackR)
        }
    }

    private fun convertTrackRToTrack(trackR: TrackR): Track {
        return Track(
            track_title = trackR.title,
            artist_name = trackR.artist.name,
            track_image =
            trackR.artist.picture_big,
            preview = trackR.preview,
        )
    }
}
