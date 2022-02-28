package com.example.limeapp.screens.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.limeapp.REPO
import com.example.limeapp.model.Channel
import com.example.limeapp.model.Current
import kotlinx.coroutines.launch

class AllChannelViewModel: ViewModel(){
    var channels: MutableLiveData<List<Channel>> = MutableLiveData()

    fun getChannels(searchText: String) {
        if (searchText == "") channels.value = REPO.channels
        else channels.value = REPO.channels.filter { it.name_ru.contains(searchText, true) }

    }

    fun updateChannelsFromApi() {
        viewModelScope.launch {
            if (REPO.getChannelsFromApiService().body() == null) {
                channels.value = listOf(Channel(id = 105, name_ru= "Первый канал", cdn= "", url= "", image= "https://assets.limehd.tv/uploads/channel/105/thumb_839f7096207876cec33636d0f9edb62c.png",  current = Current(title= "Время покажет" )))
                REPO.channels = REPO.channels.union(listOf(Channel(id = 105, name_ru= "Первый канал", cdn= "", url= "", image= "https://assets.limehd.tv/uploads/channel/105/thumb_839f7096207876cec33636d0f9edb62c.png",  current = Current(title= "Время покажет" )))).toList()
            } else {
                channels.value = REPO.getChannelsFromApiService().body()?.channels!!
                REPO.channels = REPO.channels.union(REPO.getChannelsFromApiService().body()?.channels!!).toList()
            }
        }
    }
}