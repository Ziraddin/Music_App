package com.example.music_app.data.remote.api

import com.example.music_app.data.remote.model.AlbumResponse
import com.example.music_app.data.remote.model.TrackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DeezerApi {

    @GET("search/album")
    suspend fun getAlbums(@Query("q") query: String): Response<AlbumResponse>

    @GET("search/track")
    suspend fun getTracks(@Query("q") query: String): Response<TrackResponse>

    @GET("/album/{trackId}/tracks")
    suspend fun getTracksById(@Path("trackId") trackId: Int): Response<TrackResponse>
}
