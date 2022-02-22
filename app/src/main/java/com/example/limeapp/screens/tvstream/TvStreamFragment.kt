package com.example.limeapp.screens.tvstream

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.limeapp.databinding.FragmentTvStreamBinding
import com.example.limeapp.model.Channel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.squareup.picasso.Picasso


class TvStreamFragment : Fragment() {
    lateinit var binding: FragmentTvStreamBinding
    lateinit var rootLayout: ConstraintLayout
    lateinit var exoPlayer: ExoPlayer
    lateinit var exoPlayerView: PlayerView
    lateinit var mediaSource: MediaSource
    var urlStream = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        binding = FragmentTvStreamBinding.inflate(layoutInflater, container, false)
        val currentChannel = arguments?.getSerializable("currentChannel") as Channel
        urlStream = currentChannel.url
        binding.apply {
            Picasso.get().load(currentChannel.image).into(binding.streamChannelImage)
            streamChannelName.text = currentChannel.name_ru
            streamChannelTitle.text = currentChannel.current?.title
        }
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initPlayer()
        binding.backImage.setOnClickListener{
            //APP.navController.popBackStack()
        }
        return binding.root
    }

    private fun initPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        exoPlayer.addListener(playerListener)
        createMediaSource()
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayerView = binding.exoPlayerView
        exoPlayerView.player = exoPlayer
        rootLayout = binding.rootLayout
    }

    private fun createMediaSource() {
        exoPlayer.seekTo(0)
        mediaSource = HlsMediaSource
            .Factory(DefaultDataSource.Factory (requireContext()))
            .createMediaSource(MediaItem.fromUri(Uri.parse(urlStream)))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        requireActivity().window.decorView.requestLayout()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.playWhenReady = true
        exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerListener)
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    private var playerListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
            exoPlayerView.useController = false
        }

        override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
            super.onTrackSelectionParametersChanged(parameters)
        }
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(requireContext(), "Сегодня не работает :(", Toast.LENGTH_SHORT).show()
        }
    }
}
