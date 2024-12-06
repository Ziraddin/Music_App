package com.example.music_app.core.constants

import com.example.music_app.R
import com.example.music_app.data.remote.model.AlbumR
import com.example.music_app.data.remote.model.TrackR
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track

object Converter {

    fun convertAlbumRToAlbums(albumRList: List<AlbumR>): List<Album> {
        return albumRList.map { albumR ->
            Album(
                id = albumR.id,
                album_title = albumR.title,
                artist_name = albumR.artist.name,
                album_image = getImageResourceForAlbum(albumR.cover_medium),
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
        )
    }


    private fun getImageResourceForAlbum(imageUrl: String): Int {
        return R.drawable.ic_launcher_background
    }
}
