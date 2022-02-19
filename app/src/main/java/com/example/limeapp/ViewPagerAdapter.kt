package com.example.limeapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.limeapp.screens.all.AllChannelFragment
import com.example.limeapp.screens.FavoriteChannelFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AllChannelFragment()
            else -> FavoriteChannelFragment()
        }
    }
}