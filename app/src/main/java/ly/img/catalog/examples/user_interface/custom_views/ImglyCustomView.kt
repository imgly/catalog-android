package ly.img.catalog.examples.user_interface.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.MainThread
import ly.img.android.pesdk.annotations.OnEvent
import ly.img.android.pesdk.backend.model.state.manager.ImglySettings
import ly.img.android.pesdk.backend.model.state.manager.ImglyState
import ly.img.android.pesdk.backend.model.state.manager.stateHandlerResolve
import ly.img.android.pesdk.backend.views.abstracts.ImgLyUIFrameContainer
import ly.img.android.pesdk.backend.views.abstracts.ImgLyUILinearContainer
import ly.img.android.pesdk.backend.views.abstracts.ImgLyUIRelativeContainer
import ly.img.android.pesdk.ui.model.state.UiStateMenu
import ly.img.android.pesdk.ui.panels.MenuToolPanel
import ly.img.catalog.R

// <code-region>
/**
 * A custom view showcasing how to subscribe to IMG.LY events and access [ImglyState] or [ImglySettings] classes.
 * IMG.LY SDKs provides multiple views ([ImgLyUIFrameContainer], [ImgLyUILinearContainer], [ImgLyUIRelativeContainer])
 * that you can extend from.
 *
 * Extending from ImgLyUI* views  has the following advantages -
 *   1. The view will automatically subscribe to events. Simply use the [OnEvent] annotation to listen to any event.
 *   2. Use `val name: ModelClass by stateHandlerResolve()` to resolve any [ImglyState] or [ImglySettings] class.
 *
 * If you cannot extend from an ImgLyUI* view, see [RawCustomView] for achieving the same in other custom views.
 *
 */
class ImglyCustomView constructor(
    context: Context, attrs: AttributeSet? = null
// highlight-imgly-view
) : ImgLyUIRelativeContainer(context, attrs) {
// highlight-imgly-view

    private val menuState: UiStateMenu by stateHandlerResolve()

    init {
        inflate(context, R.layout.custom_view, this)
    }

    @MainThread
    @OnEvent(value = [UiStateMenu.Event.TOOL_STACK_CHANGED], ignoreReverts = true)
    internal fun onToolStackChanged() {
        if (menuState.currentPanelData.id == MenuToolPanel.TOOL_ID) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
    }
}
// <code-region>