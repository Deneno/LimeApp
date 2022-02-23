package com.example.limeapp.screens.tvstream

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.limeapp.R
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
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding = FragmentTvStreamBinding.inflate(layoutInflater, container, false)
        val currentChannel = arguments?.getSerializable("currentChannel") as Channel
        urlStream = currentChannel.url
        binding.apply {
            Picasso.get().load(currentChannel.image).into(binding.streamChannelImage)
            streamChannelName.text = currentChannel.name_ru
            streamChannelTitle.text = currentChannel.current?.title
        }
        binding.backImage.setOnClickListener{
            requireActivity().findNavController(R.id.nav_host).popBackStack()
        }
        initPlayer()
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
        exoPlayerView.useController = false
        rootLayout = binding.rootLayout
    }

    private fun createMediaSource() {
        exoPlayer.seekTo(0)
        mediaSource = HlsMediaSource
            .Factory(DefaultDataSource.Factory (requireContext()))
            .createMediaSource(MediaItem.fromUri(Uri.parse(urlStream)))
    }

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        requireActivity().window.decorView.requestLayout()
    }*/

    private var playerListener = object : Player.Listener {

        override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
            super.onTrackSelectionParametersChanged(parameters)
        }
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(requireContext(), "Сегодня не работает :(", Toast.LENGTH_SHORT).show()
        }
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
}
