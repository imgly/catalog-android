package ly.img.catalog.examples.events

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import ly.img.android.pesdk.annotations.OnEvent
import ly.img.android.pesdk.backend.model.state.ColorAdjustmentSettings
import ly.img.android.pesdk.backend.model.state.LayerListSettings
import ly.img.android.pesdk.backend.model.state.ProgressState
import ly.img.android.pesdk.backend.model.state.layer.ImageStickerLayerSettings
import ly.img.android.pesdk.backend.model.state.manager.EventTracker
import ly.img.android.pesdk.ui.model.state.UiStateMenu
import ly.img.android.pesdk.ui.panels.AbstractToolPanel
import ly.img.android.pesdk.ui.panels.AdjustmentToolPanel
import ly.img.android.pesdk.ui.panels.AudioGalleryToolPanel
import ly.img.android.pesdk.ui.panels.AudioOverlayOptionsToolPanel
import ly.img.android.pesdk.ui.panels.BrushToolPanel
import ly.img.android.pesdk.ui.panels.ColorOptionBrushToolPanel
import ly.img.android.pesdk.ui.panels.ColorOptionStickerInkToolPanel
import ly.img.android.pesdk.ui.panels.ColorOptionStickerTintToolPanel
import ly.img.android.pesdk.ui.panels.ColorOptionTextBackgroundToolPanel
import ly.img.android.pesdk.ui.panels.ColorOptionTextForegroundToolPanel
import ly.img.android.pesdk.ui.panels.FilterToolPanel
import ly.img.android.pesdk.ui.panels.FocusToolPanel
import ly.img.android.pesdk.ui.panels.FrameToolPanel
import ly.img.android.pesdk.ui.panels.MenuToolPanel
import ly.img.android.pesdk.ui.panels.OverlayToolPanel
import ly.img.android.pesdk.ui.panels.StickerOptionToolPanel
import ly.img.android.pesdk.ui.panels.StickerToolPanel
import ly.img.android.pesdk.ui.panels.TextDesignOptionToolPanel
import ly.img.android.pesdk.ui.panels.TextDesignToolPanel
import ly.img.android.pesdk.ui.panels.TextFontOptionToolPanel
import ly.img.android.pesdk.ui.panels.TextOptionToolPanel
import ly.img.android.pesdk.ui.panels.TextToolPanel
import ly.img.android.pesdk.ui.panels.TransformToolPanel
import ly.img.android.pesdk.ui.panels.VideoCompositionToolPanel
import ly.img.android.pesdk.ui.panels.VideoCompositionTrimToolPanel
import ly.img.android.pesdk.ui.panels.VideoLibraryToolPanel
import ly.img.android.pesdk.ui.panels.VideoTrimToolPanel

// <code-region>
class CustomEventTracker : EventTracker, Parcelable {

    private val map = hashMapOf<Class<out AbstractToolPanel>, String>()

    constructor() : super()

    constructor(parcel: Parcel) : super(parcel)

    init {
        map[MenuToolPanel::class.java] = "menu"
        map[VideoCompositionToolPanel::class.java] = "video composition"
        map[VideoCompositionTrimToolPanel::class.java] = "video composition trim"
        map[VideoLibraryToolPanel::class.java] = "video library"
        map[VideoTrimToolPanel::class.java] = "video trim"
        map[AudioOverlayOptionsToolPanel::class.java] = "audio overlay"
        map[AudioGalleryToolPanel::class.java] = "audio gallery"
        map[TransformToolPanel::class.java] = "transform"
        map[FilterToolPanel::class.java] = "filter"
        map[AdjustmentToolPanel::class.java] = "adjust"
        map[FocusToolPanel::class.java] = "focus"
        map[StickerToolPanel::class.java] = "sticker"
        map[StickerOptionToolPanel::class.java] = "sticker option"
        map[ColorOptionStickerInkToolPanel::class.java] = "sticker color ink"
        map[ColorOptionStickerTintToolPanel::class.java] = "sticker color tint"
        map[TextToolPanel::class.java] = "text"
        map[TextOptionToolPanel::class.java] = "text option"
        map[TextFontOptionToolPanel::class.java] = "text font"
        map[ColorOptionTextBackgroundToolPanel::class.java] = "text color bg"
        map[ColorOptionTextForegroundToolPanel::class.java] = "text color fg"
        map[TextDesignToolPanel::class.java] = "text design"
        map[TextDesignOptionToolPanel::class.java] = "text design option"
        map[OverlayToolPanel::class.java] = "overlay"
        map[FrameToolPanel::class.java] = "frame"
        map[BrushToolPanel::class.java] = "brush"
        map[ColorOptionBrushToolPanel::class.java] = "brush color"
    }

    /*
     * This annotated method tracks when the tool stack is changed (a tool is opened or closed)
     * ignoreReverts = true ensures that this event is not triggered again between undo/redo operations
     */
    @OnEvent(UiStateMenu.Event.TOOL_STACK_CHANGED, ignoreReverts = true)
    fun onToolStackChanged(menuState: UiStateMenu) {
        val toolName = map[menuState.currentTool.javaClass]
        // log new tool opened
    }

    /*
     * This annotated method tracks when the export starts
     */
    @OnEvent(ProgressState.Event.EXPORT_START)
    fun onExportStarted(settings: LayerListSettings) {
        // log all the added sticker ids
        settings.layerSettingsList.forEach {
            (it as? ImageStickerLayerSettings)?.stickerConfig?.id?.also {
                Log.d("Catalog", it)
            }
        }
    }

    /*
     * This annotated method tracks contrast changes after a delay of 1000ms (triggerDelay) in order to prevent too many tracking events
     * ignoreReverts = true ensures that this event is not triggered again between undo/redo operations
     */
    @OnEvent(ColorAdjustmentSettings.Event.CONTRAST, ignoreReverts = true, triggerDelay = 1000)
    fun onChangeContrast(colorAdjustmentSettings: ColorAdjustmentSettings) {
        val contrast = colorAdjustmentSettings.contrast
        // log change in contrast
    }

    /*
     * This annotated method tracks when the accept button is clicked on a tool
     * ignoreReverts = true ensures that this event is not triggered again between undo/redo operations
     */
    @OnEvent(UiStateMenu.Event.ACCEPT_AND_LEAVE, ignoreReverts = true)
    fun onAcceptTool(menuState: UiStateMenu) {
        val toolName = map[menuState.currentTool.javaClass]
        // log leave tool by accepting
    }

    /*
     * This annotated method tracks when the cancel button is clicked on a tool
     * ignoreReverts = true ensures that this event is not triggered again between undo/redo operations
     */
    @OnEvent(UiStateMenu.Event.CANCEL_AND_LEAVE, ignoreReverts = true)
    fun onCancelTool(menuState: UiStateMenu) {
        val toolName = map[menuState.currentTool.javaClass]
        // log leave tool by cancelling
    }

    companion object CREATOR : Parcelable.Creator<CustomEventTracker> {
        override fun createFromParcel(parcel: Parcel): CustomEventTracker {
            return CustomEventTracker(parcel)
        }

        override fun newArray(size: Int): Array<CustomEventTracker?> {
            return arrayOfNulls(size)
        }
    }
}
// <code-region>