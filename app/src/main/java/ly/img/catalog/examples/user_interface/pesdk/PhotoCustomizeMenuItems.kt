package ly.img.catalog.examples.user_interface.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigMainMenu
import ly.img.android.pesdk.ui.panels.AdjustmentToolPanel
import ly.img.android.pesdk.ui.panels.BrushToolPanel
import ly.img.android.pesdk.ui.panels.FilterToolPanel
import ly.img.android.pesdk.ui.panels.FocusToolPanel
import ly.img.android.pesdk.ui.panels.FrameOptionToolPanel
import ly.img.android.pesdk.ui.panels.OverlayToolPanel
import ly.img.android.pesdk.ui.panels.StickerToolPanel
import ly.img.android.pesdk.ui.panels.TextDesignToolPanel
import ly.img.android.pesdk.ui.panels.TextToolPanel
import ly.img.android.pesdk.ui.panels.TransformToolPanel
import ly.img.android.pesdk.ui.panels.item.ToolItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoCustomizeMenuItems(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // highlight-tools
        val tools = listOf(
            ToolItem(
                TransformToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.transform.R.string.pesdk_transform_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_transform)
            ),
            ToolItem(
                FilterToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.filter.R.string.pesdk_filter_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_filters)
            ),
            ToolItem(
                AdjustmentToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.adjustment.R.string.pesdk_adjustments_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_adjust)
            ),
            ToolItem(
                FocusToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.focus.R.string.pesdk_focus_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_focus)
            ),
            ToolItem(
                StickerToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.sticker.R.string.pesdk_sticker_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_sticker)
            ),
            ToolItem(
                TextToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.text.R.string.pesdk_text_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_text)
            ),
            ToolItem(
                TextDesignToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.text_design.R.string.pesdk_textDesign_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_text_design)
            ),
            ToolItem(
                OverlayToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_overlay)
            ),
            ToolItem(
                FrameOptionToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.frame.R.string.pesdk_frame_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_frame)
            ),
            ToolItem(
                BrushToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.brush.R.string.pesdk_brush_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_brush)
            )
        ).sortedBy { it.name }
        // highlight-tools

        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }
            // highlight-set-tools
            .configure<UiConfigMainMenu> {
                it.setToolList(ArrayList(tools))
            }
        // highlight-set-tools

        // Start the photo editor using PhotoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        PhotoEditorBuilder(activity)
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)

        // Release the SettingsList once done
        settingsList.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        intent ?: return
        if (requestCode == EDITOR_REQUEST_CODE) {
            // Wrap the intent into an EditorSDKResult
            val result = EditorSDKResult(intent)
            when (result.resultStatus) {
                EditorSDKResult.Status.CANCELED -> showMessage("Editor cancelled")
                EditorSDKResult.Status.EXPORT_DONE -> showMessage("Result saved at ${result.resultUri}")
                else -> {
                }
            }
        }
    }
}
// <code-region>