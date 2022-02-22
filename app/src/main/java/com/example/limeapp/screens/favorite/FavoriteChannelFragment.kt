package com.example.limeapp.screens.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.limeapp.screens.root.RootViewModel
import com.example.limeapp.databinding.FragmentFavoriteChannelBinding


class FavoriteChannelFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteChannelBinding
    private lateinit var adapter: FavoriteChannelAdapter
    private lateinit var viewModel: FavoriteChannelViewModel
    private val dataModel: RootViewModel by activityViewModels()
    private var searchText=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteChannelBinding.inflate(layoutInflater, container, false)
        adapter = FavoriteChannelAdapter()
        val favoriteChannelRV = binding.favoriteChannelRV
        favoriteChannelRV.adapter = adapter
        viewModel = ViewModelProvider(this)[FavoriteChannelViewModel::class.java]
        updateFragment()
        dataModel.favoriteChannelFragment = this
        return binding.root
    }

    fun updateFragment() {
        dataModel.messageSearchText.observe(activity as LifecycleOwner) {searchText = it}
        viewModel.getFavoriteChannels(searchText)
        viewModel.channels.observe(viewLifecycleOwner) {adapter.setList(it)}
    }

    override fun onResume() {
        updateFragment()
        super.onResume()
    }
}