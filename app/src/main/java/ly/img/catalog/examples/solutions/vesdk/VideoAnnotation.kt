package ly.img.catalog.examples.solutions.vesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigMainMenu
import ly.img.android.pesdk.ui.model.state.UiConfigSticker
import ly.img.android.pesdk.ui.panels.BrushToolPanel
import ly.img.android.pesdk.ui.panels.StickerToolPanel
import ly.img.android.pesdk.ui.panels.TextToolPanel
import ly.img.android.pesdk.ui.panels.item.ImageStickerItem
import ly.img.android.pesdk.ui.panels.item.StickerCategoryItem
import ly.img.android.pesdk.ui.panels.item.ToolItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class VideoAnnotation(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {

        // For this example only the sticker, text, and brush tool are enabled
        // highlight-menu
        val toolList = arrayListOf(
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
                BrushToolPanel.TOOL_ID,
                ly.img.android.pesdk.ui.brush.R.string.pesdk_brush_title_name,
                ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_brush)
            )
        )
        // highlight-menu

        // For this example only stickers suitable for annotations are enabled
        // highlight-stickers
        val annotationStickers = listOf(
            ImageStickerItem(
                "imgly_sticker_shapes_arrow_02",
                ly.img.android.pesdk.assets.sticker.shapes.R.string.imgly_sticker_name_shapes_arrow_02,
                ImageSource.create(ly.img.android.pesdk.assets.sticker.shapes.R.drawable.imgly_sticker_shapes_arrow_02)
            ),
            ImageStickerItem(
                "imgly_sticker_shapes_arrow_03",
                ly.img.android.pesdk.assets.sticker.shapes.R.string.imgly_sticker_name_shapes_arrow_03,
                ImageSource.create(ly.img.android.pesdk.assets.sticker.shapes.R.drawable.imgly_sticker_shapes_arrow_03)
            ),
            ImageStickerItem(
                "imgly_sticker_shapes_badge_11",
                ly.img.android.pesdk.assets.sticker.shapes.R.string.imgly_sticker_name_shapes_badge_11,
                ImageSource.create(ly.img.android.pesdk.assets.sticker.shapes.R.drawable.imgly_sticker_shapes_badge_11)
            ),
            ImageStickerItem(
                "imgly_sticker_shapes_badge_12",
                ly.img.android.pesdk.assets.sticker.shapes.R.string.imgly_sticker_name_shapes_badge_12,
                ImageSource.create(ly.img.android.pesdk.assets.sticker.shapes.R.drawable.imgly_sticker_shapes_badge_12)
            ),
            ImageStickerItem(
                "imgly_sticker_shapes_badge_36",
                ly.img.android.pesdk.assets.sticker.shapes.R.string.imgly_sticker_name_shapes_badge_36,
                ImageSource.create(ly.img.android.pesdk.assets.sticker.shapes.R.drawable.imgly_sticker_shapes_badge_36)
            )
        )

        // Create a custom sticker category for the annotation stickers
        val annotationStickersCategory = StickerCategoryItem(
            "annotation_stickers",
            "Annotation",
            ImageSource.create(ly.img.android.pesdk.assets.sticker.shapes.R.drawable.imgly_sticker_shapes_arrow_02),
            annotationStickers
        )
        // highlight-stickers

        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            .configure<LoadSettings> {
                // Set the source as the Uri of the video to be loaded
                it.source = activity.resourceUri(R.raw.skater)
            }

        // highlight-config
        settingsList.configure<UiConfigMainMenu> {
            // Set custom tool list
            it.setToolList(toolList)
        }.configure<UiConfigSticker> {
            // Set custom sticker list
            it.setStickerLists(annotationStickersCategory)
        }
        // highlight-config

        // Start the video editor using VideoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        VideoEditorBuilder(activity)
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)

        // Release the SettingsList once done
        settingsList.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        intent ?: return
        if (requestCode == EDITOR_REQUEST_CODE) {
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