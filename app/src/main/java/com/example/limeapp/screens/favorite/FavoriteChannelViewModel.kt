package com.example.limeapp.screens.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.limeapp.REPO
import com.example.limeapp.model.Channel

class FavoriteChannelViewModel: ViewModel(){
    var channels: MutableLiveData<List<Channel>> = MutableLiveData()

    fun getFavoriteChannels(searchText: String) {
        if (searchText == "") channels.value = REPO.channels.filter {REPO.favoriteChannelIDs.contains(it.id)}
        else channels.value = REPO.channels.filter {REPO.favoriteChannelIDs.contains(it.id)}.filter { it.name_ru.contains(searchText, true) }
    }
}