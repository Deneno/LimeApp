package com.example.limeapp.screens.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.limeapp.screens.root.RootViewModel
import com.example.limeapp.databinding.FragmentAllChannelBinding


class AllChannelFragment : Fragment() {
    private lateinit var binding: FragmentAllChannelBinding
    private lateinit var adapter: AllChannelAdapter
    private lateinit var viewModel: AllChannelViewModel
    private val dataModel: RootViewModel by activityViewModels()
    private var searchText=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllChannelBinding.inflate(layoutInflater, container, false)
        val allChannelRV = binding.allChannelRV
        adapter = AllChannelAdapter()
        allChannelRV.adapter = adapter
        viewModel = ViewModelProvider(this)[AllChannelViewModel::class.java]
        viewModel.updateChannelsFromApi()
        viewModel.channels.observe(viewLifecycleOwner) {adapter.setList(it)}
        dataModel.allChannelFragment = this
        return binding.root
    }

    fun updateFragment() {
        dataModel.messageSearchText.observe(activity as LifecycleOwner) {searchText = it}
        viewModel.getChannels(searchText)
        viewModel.channels.observe(viewLifecycleOwner) {adapter.setList(it)}
    }

    override fun onResume() {
        updateFragment()
        super.onResume()
    }
}