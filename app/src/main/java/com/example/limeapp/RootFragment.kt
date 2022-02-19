package com.example.limeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.limeapp.databinding.FragmentRootBinding
import com.google.android.material.tabs.TabLayoutMediator

class RootFragment : Fragment() {
    lateinit var binding: FragmentRootBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRootBinding.inflate(layoutInflater, container,false)
        binding.viewPager.adapter = ViewPagerAdapter(requireContext() as FragmentActivity)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){
            tab, pos ->
            when(pos){
                0 -> tab.text = getText(R.string.All)
                1 -> tab.text = getText(R.string.favorite)
            }
        }.attach()
        return binding.root
    }


}