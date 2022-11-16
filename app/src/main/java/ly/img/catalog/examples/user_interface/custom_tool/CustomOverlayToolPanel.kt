package ly.img.catalog.examples.user_interface.custom_tool

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import ly.img.android.pesdk.backend.model.config.OverlayAsset
import ly.img.android.pesdk.backend.model.state.AssetConfig
import ly.img.android.pesdk.backend.model.state.OverlaySettings
import ly.img.android.pesdk.backend.model.state.manager.StateHandler
import ly.img.android.pesdk.ui.adapter.DataSourceListAdapter
import ly.img.android.pesdk.ui.model.data.PanelData
import ly.img.android.pesdk.ui.model.data.TitleData
import ly.img.android.pesdk.ui.model.state.UiState
import ly.img.android.pesdk.ui.panels.AbstractToolPanel
import ly.img.android.pesdk.ui.panels.item.AbstractIdItem
import ly.img.android.pesdk.utils.SetHardwareAnimatedViews

// <code-region>
class CustomOverlayToolPanel @Keep constructor(stateHandler: StateHandler) : AbstractToolPanel(stateHandler) {
    companion object {
        const val TOOL_ID = "MyCustomOverlayToolPanelId"

        // This is needed to load the init block
        fun initAndGetToolId(): String {
            return TOOL_ID
        }

        init {
            // Add Panel to the definition list
            UiState.addPanel(PanelData(TOOL_ID, CustomOverlayToolPanel::class.java))
            UiState.addTitle(TitleData(TOOL_ID, ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_title_name))
        }
    }

    private val assetConfig = stateHandler[AssetConfig::class]
    private val uiConfigOverlay = stateHandler[CustomUiConfigOverlay::class]
    private val overlaySettings = stateHandler[OverlaySettings::class]

    override fun onAttached(context: Context, panelView: View) {
        super.onAttached(context, panelView)
        val categoryList = panelView.findViewById<RecyclerView>(ly.img.catalog.R.id.overlayList)
        val gridView = panelView.findViewById<RecyclerView>(ly.img.catalog.R.id.grid)

        val overlayGridAdapter = DataSourceListAdapter()
        overlayGridAdapter.setOnItemClickListener {
            if (it is AbstractIdItem) {
                val asset = it.getData(assetConfig.getAssetMap(OverlayAsset::class.java))
                if (asset != null) {
                    overlaySettings.overlayAsset = asset
                    overlaySettings.blendMode = asset.blendMode
                    overlaySettings.intensity = asset.intensity
                    gridView.visibility = View.GONE
                } else throw RuntimeException("OverlayAsset with id `${it.id}` is missing.")
            }
        }
        gridView.adapter = overlayGridAdapter

        val categoryAdapter = DataSourceListAdapter()
        categoryAdapter.data = uiConfigOverlay.overlayList
        categoryAdapter.setOnItemClickListener {
            if (it is NoOverlay) {
                overlaySettings.overlayAsset = OverlayAsset.NONE_BACKDROP
                gridView.visibility = View.GONE
            } else if (it is CustomOverlayCategory) {
                overlayGridAdapter.data = it.overlayList
                gridView.visibility = View.VISIBLE
            }
        }
        categoryList.adapter = categoryAdapter
    }

    override fun getLayoutResource(): Int = ly.img.catalog.R.layout.custom_overlay_tool

    override fun getHistorySettings() = arrayOf(OverlaySettings::class.java)

    override fun createShowAnimator(panelView: View) = AnimatorSet().also {
        it.playTogether(
            ObjectAnimator.ofFloat(panelView, "alpha", 0f, 1f),
            ObjectAnimator.ofFloat(panelView, "translationY", panelView.height.toFloat(), 0f)
        )
        it.addListener(SetHardwareAnimatedViews(panelView))
        it.duration = ANIMATION_DURATION.toLong()
    }

    override fun createExitAnimator(panelView: View) = AnimatorSet().also {
        it.playTogether(
            ObjectAnimator.ofFloat(panelView, "alpha", 1f, 0f),
            ObjectAnimator.ofFloat(panelView, "translationY", 0f, panelView.height.toFloat())
        )
        it.addListener(SetHardwareAnimatedViews(panelView))
        it.duration = ANIMATION_DURATION.toLong()
    }

    override fun onDetached() {}
}
// <code-region>