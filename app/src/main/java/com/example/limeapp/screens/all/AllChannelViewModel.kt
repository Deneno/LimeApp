package com.example.limeapp.screens.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limeapp.data.ChannelsRepository
import com.example.limeapp.model.Channels
import kotlinx.coroutines.launch
import retrofit2.Response

class AllChannelViewModel: ViewModel(){
    private var channelsRepository = ChannelsRepository()
    var channels: MutableLiveData<Response<Channels>> = MutableLiveData()

    fun getChannels() {
        viewModelScope.launch {
            channels.value = channelsRepository.getChannelsFromApiService()
        }
    }
}