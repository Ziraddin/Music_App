package com.example.music_app.data.repository

import com.example.music_app.data.remote.api.DeezerApi
import com.example.music_app.data.remote.api.RetrofitHelper
import com.example.music_app.data.remote.model.AlbumResponse
import com.example.music_app.data.remote.model.TrackResponse
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class DeezerRepository(private val api: DeezerApi = RetrofitHelper.getInstance()) {

    suspend fun getAlbums(albumQuery: String): Response<AlbumResponse> {
        return api.getAlbums(albumQuery)
    }

    suspend fun getTracks(trackQuery: String): Response<TrackResponse> {
        return api.getTracks(trackQuery)
    }

    suspend fun getTracksById(trackId: Int): Response<TrackResponse> {
        return api.getTracksById(trackId)
    }
}


fun main() {
    runBlocking {
        val repo = DeezerRepository()
        val response: Response<AlbumResponse> = repo.getAlbums("eminem")

        if (response.isSuccessful) {
            val searchResponse = response.body()
            searchResponse?.data?.forEach {
                println("Album: ${it.title} - ${it.tracklist}")
            }
        } else {
            println("Failed to fetch data: ${response.message()}")
        }
    }
}
