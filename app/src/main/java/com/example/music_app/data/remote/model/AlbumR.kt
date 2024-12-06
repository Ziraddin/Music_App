package com.example.music_app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumR(
    val id: Int, val title: String, val artist: ArtistR,

    @SerialName("cover_small") val cover_small: String,

    @SerialName("cover_medium") val cover_medium: String,

    @SerialName("cover_big") val cover_big: String,

    @SerialName("nb_tracks") val nb_tracks: Int,

    @SerialName("release_date") val release_date: String,

    val tracklist: String
)

@Serializable
data class AlbumResponse(
    val data: List<AlbumR>,
)
