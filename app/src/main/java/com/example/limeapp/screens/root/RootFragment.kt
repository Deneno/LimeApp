package com.example.limeapp.screens.root

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.limeapp.R
import com.example.limeapp.REPO
import com.example.limeapp.databinding.FragmentRootBinding
import com.google.android.material.tabs.TabLayoutMediator


class RootFragment : Fragment() {
    private lateinit var binding: FragmentRootBinding
    private val dataModel: RootViewModel by activityViewModels()
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        prefs = requireContext().getSharedPreferences("favoriteChannelIDs", Context.MODE_PRIVATE)
        binding = FragmentRootBinding.inflate(layoutInflater, container,false)
        val viewPagerAdapter = ViewPagerAdapter(requireContext() as FragmentActivity)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){
                tab, pos -> when(pos){
            0 -> tab.text = getText(R.string.All)
            1 -> tab.text = getText(R.string.favorite)
        }
        }.attach()
        binding.searchChannelText.setOnEditorActionListener { textView, i, _ ->
            var ret = false
            if (i == IME_ACTION_SEARCH) {
                dataModel.messageSearchText.value = textView.text.toString()
                dataModel.allChannelFragment?.updateFragment()
                dataModel.favoriteChannelFragment?.updateFragment()
                val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                ret = true
            }
            ret
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        val editor = prefs.edit()
        editor.putStringSet("favoriteChannelIDs", REPO.favoriteChannelIDs.map{it.toString()}.toSet())
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        if (prefs.contains("favoriteChannelIDs")) {
            REPO.favoriteChannelIDs.clear()
            REPO.favoriteChannelIDs.addAll(prefs.getStringSet("favoriteChannelIDs", HashSet<String>())!!.map { it.toInt() })
        }
    }
}