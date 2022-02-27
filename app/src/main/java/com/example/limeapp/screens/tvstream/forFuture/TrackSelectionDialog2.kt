package com.example.limeapp.screens.tvstream.forFuture

import androidx.fragment.app.DialogFragment

class TrackSelectionDialog2: DialogFragment() {
    /*var tabFragments = SparseArray<TrackSelectionViewFragment>()
    var onClickListener: DialogInterface.OnClickListener? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null

    companion object {

        class TrackSelectionViewFragment : Fragment(),
            TrackSelectionListener {
            private var mappedTrackInfo: MappedTrackInfo? = null
            private var rendererIndex = 0
            private var allowAdaptiveSelections = false
            private var allowMultipleOverrides = false
            var isDisabled = false
            var overrides: List<SelectionOverride>? = null

            fun init(
                mappedTrackInfo: MappedTrackInfo?,
                rendererIndex: Int,
                initialIsDisabled: Boolean,
                initialOverride: SelectionOverride?,
                allowAdaptiveSelections: Boolean,
                allowMultipleOverrides: Boolean
            ) {
                Log.d("mytag", "number 1")

                this.mappedTrackInfo = mappedTrackInfo
                this.rendererIndex = rendererIndex
                isDisabled = initialIsDisabled
                overrides = initialOverride?.let { listOf(it) } ?: emptyList()
                this.allowAdaptiveSelections = allowAdaptiveSelections
                this.allowMultipleOverrides = allowMultipleOverrides
            }

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                Log.d("mytag", "number 2")

                val rootView: View = inflater.inflate(
                    R.layout.exo_track_selection_dialog, container,  /* attachToRoot= */false
                )
                val trackSelectionView =
                    rootView.findViewById<TrackSelectionView>(R.id.exo_track_selection_view)
                trackSelectionView.setShowDisableOption(true)
                trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides)
                trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections)
                trackSelectionView.init(
                    mappedTrackInfo!!,
                    rendererIndex,
                    isDisabled,
                    overrides!!,  /* trackFormatComparator= */
                    null,  /* listener= */
                    this
                )
                return rootView
            }

            override fun onTrackSelectionChanged(
                isDisabled: Boolean, overrides: List<SelectionOverride>
            ) {
                Log.d("mytag", "onTrackSelectionChanged - При выборе качества")
                this.isDisabled = isDisabled
                this.overrides = overrides
                (this.requireParentFragment() as (TrackSelectionDialog2)).onClickListener!!.onClick((this.requireParentFragment() as (TrackSelectionDialog2)).dialog, DialogInterface.BUTTON_POSITIVE)
                (this.requireParentFragment() as (TrackSelectionDialog2)).dismiss()
            }

        }

        fun willHaveContent(trackSelector: DefaultTrackSelector): Boolean {
            Log.d("mytag", "willHaveContent - проверка наличия контента 1")
            val mappedTrackInfo = trackSelector.currentMappedTrackInfo
            return mappedTrackInfo != null && willHaveContent(mappedTrackInfo)
        }


        private fun willHaveContent(mappedTrackInfo: MappedTrackInfo): Boolean {
            Log.d("mytag", "willHaveContent - проверка наличия контента 2")
            for (i in 0 until mappedTrackInfo.rendererCount) {

                if (showTabForRenderer(mappedTrackInfo, i)) {
                    return true
                }
            }
            return false
        }

        fun showTabForRenderer(mappedTrackInfo: MappedTrackInfo, rendererIndex: Int): Boolean {
            /*Log.d("mytag", "number 6")
            val trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
            if (trackGroupArray.length == 0) {
                return false
            }
            val trackType = mappedTrackInfo.getRendererType(rendererIndex)*/
            return true //trackType == C.TRACK_TYPE_VIDEO
        }

        fun createForTrackSelector(
            trackSelector: DefaultTrackSelector, onDismissListener: DialogInterface.OnDismissListener
        ): TrackSelectionDialog2 {
            Log.d("mytag", "number 7")
            val mappedTrackInfo = Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
            val trackSelectionDialog = TrackSelectionDialog2()
            val parameters = trackSelector.parameters
            trackSelectionDialog.init(
                mappedTrackInfo,  /* initialParameters = */
                parameters,
                allowAdaptiveSelections = true,
                allowMultipleOverrides = false,
                onClickListener = { _, _ ->
                    val builder = parameters.buildUpon()
                    for (i in 0 until mappedTrackInfo.rendererCount) {
                        builder
                            .clearSelectionOverrides( /* rendererIndex= */i)
                            .setRendererDisabled( /* rendererIndex= */
                                i,
                                trackSelectionDialog.getIsDisabled( /* rendererIndex= */i)
                            )
                        val overrides: List<SelectionOverride> =
                            trackSelectionDialog.getOverrides( /* rendererIndex= */i)
                        if (overrides.isNotEmpty()) {
                            builder.setSelectionOverride( /* rendererIndex= */
                                i,
                                mappedTrackInfo.getTrackGroups( /* rendererIndex= */i),
                                overrides[0]
                            )
                        }
                    }
                    trackSelector.setParameters(builder)
                },
                onDismissListener = onDismissListener
            )
            return trackSelectionDialog
        }

    }

    private fun init(
        mappedTrackInfo: MappedTrackInfo,
        initialParameters: DefaultTrackSelector.Parameters,
        allowAdaptiveSelections: Boolean,
        allowMultipleOverrides: Boolean,
        onClickListener: DialogInterface.OnClickListener,
        onDismissListener: DialogInterface.OnDismissListener
    ) {
        Log.d("mytag", "number 8")
        this.onClickListener = onClickListener
        this.onDismissListener = onDismissListener
        for (i in 0 until mappedTrackInfo.rendererCount) {
            if (showTabForRenderer(mappedTrackInfo, i)) {
                val trackType = mappedTrackInfo.getRendererType( /* rendererIndex= */i)
                val trackGroupArray = mappedTrackInfo.getTrackGroups(i)
                val tabFragment = TrackSelectionViewFragment()
                tabFragment.init(
                    mappedTrackInfo,  /* rendererIndex= */
                    i,
                    initialParameters.getRendererDisabled( /* rendererIndex= */i),
                    initialParameters.getSelectionOverride( /* rendererIndex= */i, trackGroupArray),
                    allowAdaptiveSelections,
                    allowMultipleOverrides
                )
                tabFragments.put(i, tabFragment)
            }
        }
    }

    private fun getIsDisabled(rendererIndex: Int): Boolean {
        Log.d("mytag", "number 9")
        val rendererView = tabFragments[rendererIndex]
        return rendererView != null && rendererView.isDisabled
    }

    private fun getOverrides(rendererIndex: Int): List<SelectionOverride> {
        Log.d("mytag", "number 10")
        val rendererView = tabFragments[rendererIndex]
        return if (rendererView == null) emptyList() else rendererView.overrides!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("mytag", "number 11")
        return AppCompatDialog(activity, R.style.TrackSelectionDialogThemeOverlay)
    }

    override fun onDismiss(dialog: DialogInterface) {
        Log.d("mytag", "number 12")
        super.onDismiss(dialog)
        onDismissListener!!.onDismiss(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        Log.d("mytag", "number 13")
        val dialogView: View = inflater.inflate(R.layout.track_selection_dialog, container, false)
        val tabLayout = dialogView.findViewById<TabLayout>(R.id.track_selection_dialog_tab_layout)
        val viewPager = dialogView.findViewById<ViewPager>(R.id.track_selection_dialog_view_pager)
        viewPager.adapter = FragmentAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.visibility = if (tabFragments.size() > 1) View.VISIBLE else View.GONE
        return dialogView
    }
    inner class FragmentAdapter(fragmentManager: FragmentManager?) :
        FragmentPagerAdapter(fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            Log.d("mytag", "number 14")
            return tabFragments.valueAt(position)
        }

        override fun getCount(): Int {
            return 1
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "Video"
        }
    }*/
}