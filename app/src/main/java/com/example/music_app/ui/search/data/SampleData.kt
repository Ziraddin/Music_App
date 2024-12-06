package com.example.music_app.ui.search.data


class SampleData {

    companion object {
        fun generateTracks(itemCount: Int): List<Track> {
            val tracks = mutableListOf<Track>()

            for (i in 1..itemCount) {
                val trackTitle = "Track Title $i"
                val artistName = "Artist $i"

                tracks.add(
                    Track(
                        track_title = trackTitle, artist_name = artistName
                    )
                )
            }
            return tracks
        }

        fun generateAlbums(itemCount: Int): List<Album> {
            val albums = mutableListOf<Album>()
            val tracks = generateTracks(7)
            for (i in 1..itemCount) {
                val albumTitle = "Album Title $i"
                val artistName = "Artist $i"

                albums.add(
                    Album(
                        album_title = albumTitle, artist_name = artistName, tracks = tracks, id = 1
                    )
                )
            }
            return albums
        }
    }
}
