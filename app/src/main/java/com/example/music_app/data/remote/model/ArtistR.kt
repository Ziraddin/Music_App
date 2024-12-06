package com.example.music_app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistR(
    val name: String,
    val picture: String,

    @SerialName("picture_small") val pictureSmall: String,

    @SerialName("picture_medium") val pictureMedium: String,

    @SerialName("picture_big") val pictureBig: String,
)