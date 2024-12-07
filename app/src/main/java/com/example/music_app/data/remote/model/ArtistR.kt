package com.example.music_app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistR(
    val name: String,
    val picture: String,

    @SerialName("picture_small") var picture_small: String,

    @SerialName("picture_medium") var picture_medium: String,

    @SerialName("picture_big") var picture_big: String,
)