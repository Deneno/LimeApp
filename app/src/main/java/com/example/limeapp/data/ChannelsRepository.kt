package com.example.limeapp.data

import com.example.limeapp.model.Channel
import com.example.limeapp.model.Channels
import retrofit2.Response

class ChannelsRepository {
    var channels = emptyList<Channel>()
    var favoriteChannelIDs = ArrayList<Int>()
    suspend fun getChannelsFromApiService(): Response<Channels> {
        return RetrofitInstance.api.getChannelsFromApi()
    }
}