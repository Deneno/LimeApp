package com.example.limeapp.screens.tvstream

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.limeapp.R
import com.example.limeapp.data.TrackSelectionClass
import com.example.limeapp.databinding.FragmentTvStreamBinding
import com.example.limeapp.model.Channel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TrackNameProvider
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.Assertions
import com.squareup.picasso.Picasso


class TvStreamFragment : Fragment() {
    private lateinit var binding: FragmentTvStreamBinding
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var exoPlayerView: PlayerView
    private lateinit var mediaSource: MediaSource
    private lateinit var trackSelector: DefaultTrackSelector
    private var urlStream = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        binding = FragmentTvStreamBinding.inflate(layoutInflater, container, false)
        val currentChannel = arguments?.getSerializable("currentChannel") as Channel
        binding.apply {
            Picasso.get().load(currentChannel.image).into(binding.streamChannelImage)
            streamChannelName.text = currentChannel.name_ru
            streamChannelTitle.text = currentChannel.current?.title
        }
        urlStream = "https://streaming.astrakhan.ru/astrakhan24/index.m3u8"
        //urlStream = currentChannel.url
        trackSelector= DefaultTrackSelector(requireContext())
        initPlayer()
        binding.backImage.setOnClickListener{
            requireActivity().findNavController(R.id.nav_host).popBackStack()
        }
        binding.qualityImage.setOnClickListener {
            if (binding.trackSelectionCardView.visibility == View.GONE && trackSelector.currentMappedTrackInfo != null){
                exoPlayer.videoChangeFrameRateStrategy
                binding.trackSelectionCardView.visibility = View.VISIBLE
                val mappedTrackInfo = Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
                val trackSelectionView: TrackSelectionClass = binding.trackSelectionView
                val trackNameProvider: TrackNameProvider = DefaultTrackNameProvider(resources)
                trackSelectionView.setTrackNameProvider { f: Format ->
                    if (f.height != Format.NO_VALUE) f.height.toString() + "р" else trackNameProvider.getTrackName(f)
                }
                /*for (view in trackSelectionView.allViews) {
                    view.layoutParams.resolveLayoutDirection(R.layout.item_quality_layout)
                }*/
                val trackGroupArray = mappedTrackInfo.getTrackGroups(0)
                var isDisabledTv = trackSelector.parameters.getRendererDisabled(0)
                var overridesTv = trackSelector.parameters.getSelectionOverride(0, trackGroupArray)?.let { listOf(it) } ?: emptyList()
                trackSelectionView.init(mappedTrackInfo, 0, isDisabledTv, overridesTv,
                    { o1: Format, o2: Format -> o2.height-o1.height},
                    { isDisabled, overrides ->
                        isDisabledTv = isDisabled
                        overridesTv = overrides
                        val builder = trackSelector.parameters.buildUpon()
                        builder.clearSelectionOverrides(0).setRendererDisabled(0,isDisabledTv)
                        val overridesOnClickListener = overridesTv
                        if (overridesOnClickListener.isNotEmpty()) {builder.setSelectionOverride(
                            0,mappedTrackInfo.getTrackGroups(0),overridesOnClickListener[0])}
                        trackSelector.setParameters(builder)
                        binding.trackSelectionCardView.visibility = View.GONE
                    }
                )
            }
        }
        return binding.root
    }

    private fun initPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).setTrackSelector(trackSelector).build()
        exoPlayer.addListener(playerListener)
        createMediaSource()
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayerView = binding.exoPlayerView
        exoPlayerView.player = exoPlayer
        exoPlayerView.useController = false
        rootLayout = binding.rootLayout
    }

    private var playerListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(requireContext(), "Сегодня не работает :(", Toast.LENGTH_SHORT).show()
            Log.d("mytag", "${error.message}")
        }
    }

    private fun createMediaSource() {
        exoPlayer.seekTo(0)
        mediaSource = HlsMediaSource
            .Factory(DefaultDataSource.Factory (requireContext()))
            .createMediaSource(MediaItem.fromUri(Uri.parse(urlStream)))
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
