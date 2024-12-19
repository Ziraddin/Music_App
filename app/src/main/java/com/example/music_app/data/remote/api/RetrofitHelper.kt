package com.example.music_app.data.remote.api

import com.example.music_app.core.Constants.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun getInstance(): DeezerApi {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeezerApi::class.java)
    }
}