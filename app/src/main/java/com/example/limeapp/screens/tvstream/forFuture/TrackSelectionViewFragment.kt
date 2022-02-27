package com.example.limeapp.screens.tvstream.forFuture
/*
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import com.example.limeapp.R
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Format.NO_VALUE
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider
import com.google.android.exoplayer2.ui.TrackNameProvider
import com.google.android.exoplayer2.ui.TrackSelectionView
import com.google.android.exoplayer2.ui.TrackSelectionView.TrackSelectionListener
import com.google.android.exoplayer2.util.Assertions


class TrackSelectionViewFragment(private val trackSelector: DefaultTrackSelector) : DialogFragment(), TrackSelectionListener {
    private var onClickListener: DialogInterface.OnClickListener? = null
    private var isDisabled = false
    private var overrides: List<SelectionOverride>? = null

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mappedTrackInfo = Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
        val rootView: View = inflater.inflate(R.layout.exo_track_selection_dialog2, container, false)
        val trackSelectionView = rootView.findViewById<TrackSelectionView>(R.id.exo_track_selection_view2)
        //Убрать None
        //trackSelectionView.setShowDisableOption(true)
        val trackNameProvider: TrackNameProvider = DefaultTrackNameProvider(resources)
        trackSelectionView.setTrackNameProvider { f: Format ->
            if (f.height != NO_VALUE) f.height.toString() else trackNameProvider.getTrackName(f)
        }
        onClickListener = DialogInterface.OnClickListener { _, _ ->
            val builder = trackSelector.parameters.buildUpon()
            builder.clearSelectionOverrides(0).setRendererDisabled(0,this.isDisabled)
            val overridesOnClickListener = this.overrides!!
            if (overridesOnClickListener.isNotEmpty()) {builder.setSelectionOverride(
                0,mappedTrackInfo.getTrackGroups(0),overridesOnClickListener[0])}
            trackSelector.setParameters(builder)
        }
        val trackGroupArray = mappedTrackInfo.getTrackGroups(0)
        isDisabled = trackSelector.parameters.getRendererDisabled(0)
        overrides = trackSelector.parameters.getSelectionOverride(0, trackGroupArray)?.let { listOf(it) } ?: emptyList()
        trackSelectionView.setShowDisableOption(false)
        trackSelectionView.init(mappedTrackInfo, 0, isDisabled, overrides!!, null, this)
        return rootView
    }

    override fun onTrackSelectionChanged(isDisabled: Boolean, overrides: List<SelectionOverride>) {
        this.isDisabled = isDisabled
        this.overrides = overrides
        onClickListener!!.onClick(this.dialog, DialogInterface.BUTTON_POSITIVE)
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AppCompatDialog(activity, R.style.TrackSelectionDialogThemeOverlay)
    }

}
*/