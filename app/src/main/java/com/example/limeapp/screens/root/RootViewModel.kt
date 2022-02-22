package com.example.limeapp.screens.root

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.limeapp.screens.all.AllChannelFragment
import com.example.limeapp.screens.favorite.FavoriteChannelFragment

class RootViewModel: ViewModel(){
    var allChannelFragment: AllChannelFragment? = null
    var favoriteChannelFragment: FavoriteChannelFragment? = null

    val messageSearchText: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}