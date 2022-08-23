package ly.img.catalog.examples.user_interface.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.MainThread
import ly.img.android.pesdk.annotations.OnEvent
import ly.img.android.pesdk.backend.model.state.manager.ImglySettings
import ly.img.android.pesdk.backend.model.state.manager.ImglyState
import ly.img.android.pesdk.backend.model.state.manager.StateHandler
import ly.img.android.pesdk.backend.model.state.manager.StateHandlerContext
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
 *
 * We recommend extending from one of the ImgLyUI* classes [ImgLyUIFrameContainer], [ImgLyUILinearContainer], [ImgLyUIRelativeContainer])
 * whenever possible to reduce boilerplate code. See [ImglyCustomView] for example.
 */
class RawCustomView constructor(
    context: Context, attrs: AttributeSet? = null
// highlight-custom-view
) : RelativeLayout(context, attrs), StateHandlerContext {
// highlight-custom-view

    private val menuState: UiStateMenu by stateHandlerResolve()

    init {
        inflate(context, R.layout.custom_view, this)
    }

    override var stateHandler = try {
        if (isInEditMode) {
            StateHandler(context)
        } else {
            StateHandler.findInViewContext(context)
        }
    } catch (e: StateHandler.StateHandlerNotFoundException) {
        throw RuntimeException(e)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        stateHandler.registerSettingsEventListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateHandler.unregisterSettingsEventListener(this)
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