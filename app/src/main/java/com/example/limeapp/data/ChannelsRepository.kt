package com.example.limeapp.data

import com.example.limeapp.model.Channels
import retrofit2.Response

class ChannelsRepository {
    suspend fun getChannelsFromApiService(): Response<Channels> {
        return RetrofitInstance.api.getChannelsFromApi()
    }
}