package com.example.limeapp.screens.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limeapp.REPO
import com.example.limeapp.model.Channel
import kotlinx.coroutines.launch

class AllChannelViewModel: ViewModel(){
    var channels: MutableLiveData<List<Channel>> = MutableLiveData()

    fun getChannels(searchText: String) {
        if (searchText == "") channels.value = REPO.channels
        else channels.value = REPO.channels.filter { it.name_ru.contains(searchText, true) }

    }

    fun updateChannelsFromApi() {
        viewModelScope.launch {
            channels.value = REPO.getChannelsFromApiService().body()?.channels!!
            REPO.channels = REPO.channels.union(REPO.getChannelsFromApiService().body()?.channels!!).toList()
        }
    }
}