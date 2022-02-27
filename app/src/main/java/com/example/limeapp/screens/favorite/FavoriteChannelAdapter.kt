package com.example.limeapp.screens.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.limeapp.R
import com.example.limeapp.REPO
import com.example.limeapp.databinding.ItemChannelLayoutBinding
import com.example.limeapp.model.Channel
import com.squareup.picasso.Picasso

class FavoriteChannelAdapter: RecyclerView.Adapter<FavoriteChannelAdapter.AllChannelViewHolder>() {
    private var channels = emptyList<Channel>()

    class AllChannelViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var binding = ItemChannelLayoutBinding.bind(view)

        fun bind(channel: Channel) {
            binding.channelName.text = channel.name_ru
            binding.channelTitle.text = channel.current?.title
            Picasso.get().load(channel.image).into(binding.channelImage)
            if (REPO.favoriteChannelIDs.contains(channel.id)) binding.favoriteImage.setImageResource(R.drawable.ic_checkstar)
            else binding.favoriteImage.setImageResource(R.drawable.ic_uncheckstar)
            binding.favoriteImage.setOnClickListener {
                if (!REPO.favoriteChannelIDs.contains(channel.id)) {
                    REPO.favoriteChannelIDs.add(channel.id)
                    binding.favoriteImage.setImageResource(R.drawable.ic_checkstar)
                } else {
                    REPO.favoriteChannelIDs.removeAll{ it == channel.id }
                    binding.favoriteImage.setImageResource(R.drawable.ic_uncheckstar)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel_layout, parent, false)
        return AllChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllChannelViewHolder, position: Int) {
        holder.bind(channels[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("currentChannel", channels[position])
            FragmentManager.findFragment<FavoriteChannelFragment>(holder.itemView)
                .requireActivity()
                .findNavController(R.id.nav_host)
                .navigate(R.id.action_rootFragment_to_tvStreamFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    fun setList(list: List<Channel>) {
        channels = list
        notifyDataSetChanged()
    }
}