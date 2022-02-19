package com.example.limeapp.data

import com.example.limeapp.model.Channels
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("playlist")
    suspend fun getChannelsFromApi(): Response<Channels>
}