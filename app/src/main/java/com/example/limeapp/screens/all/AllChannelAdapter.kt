package com.example.limeapp.screens.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.limeapp.APP
import com.example.limeapp.R
import com.example.limeapp.databinding.ItemChannelLayoutBinding
import com.example.limeapp.model.Channel
import com.example.limeapp.model.Channels
import com.squareup.picasso.Picasso

class AllChannelAdapter: RecyclerView.Adapter<AllChannelAdapter.AllChannelViewHolder>() {
    private var channels = emptyList<Channel>()

    class AllChannelViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var binding = ItemChannelLayoutBinding.bind(view)

        fun bind(channel: Channel) {
            binding.channelName.text = channel.name_ru
            //binding.channelTitle.text = channel.current.title
            Picasso.with(APP.applicationContext).load(channel.image).into(binding.channelImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel_layout, parent, false)
        return AllChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllChannelViewHolder, position: Int) {
        holder.bind(channels[position])
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    fun setList(list: Channels) {
        channels = list.channels
        notifyDataSetChanged()
    }
}