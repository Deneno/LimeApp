package com.example.limeapp.data

import com.example.limeapp.model.Channel
import com.example.limeapp.model.Channels
import retrofit2.Response
import java.net.SocketTimeoutException

class ChannelsRepository {
    var channels = emptyList<Channel>()
    var favoriteChannelIDs = ArrayList<Int>()
    suspend fun getChannelsFromApiService(): Response<Channels> {
        return try {
            return RetrofitInstance.api.getChannelsFromApi()
        } catch (e: SocketTimeoutException) {
            return Response.success(null)
        }
    }
}