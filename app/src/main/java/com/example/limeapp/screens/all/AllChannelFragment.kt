package com.example.limeapp.screens.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.limeapp.databinding.FragmentAllChannelBinding


class AllChannelFragment : Fragment() {
    private lateinit var binding: FragmentAllChannelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllChannelBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val viewModel = ViewModelProvider(this)[AllChannelViewModel::class.java]
        val allChannelRV = binding.allChannelRV
        val adapter = AllChannelAdapter()
        allChannelRV.adapter = adapter
        viewModel.getChannels()
        viewModel.channels.observe(viewLifecycleOwner) { list ->
            list.body()?.let { adapter.setList(it)}
        }
    }
}