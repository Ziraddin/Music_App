package com.example.music_app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackR(
    val title: String,
    val link: String,
    val duration: Long,
    val preview: String,

    @SerialName("title_short") val title_short: String,

    @SerialName("release_date") val release_date: String,

    val artist: ArtistR,
)

@Serializable
data class TrackResponse(
    val data: List<TrackR>,
)