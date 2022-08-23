package ly.img.catalog.examples.brush

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.ColorAsset
import ly.img.android.pesdk.backend.model.state.BrushSettings
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigBrush
import ly.img.android.pesdk.ui.panels.BrushToolPanel
import ly.img.android.pesdk.ui.panels.item.BrushColorOption
import ly.img.android.pesdk.ui.panels.item.BrushOption
import ly.img.android.pesdk.ui.panels.item.ColorItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class VideoBrushConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        settingsList.configure<UiConfigBrush> {
            // By default all available brush tools are enabled.
            // For this example only a couple are enabled.
            // highlight-tools
            it.optionList.set(
                listOf(
                    BrushColorOption(BrushToolPanel.OPTION_COLOR, Color.TRANSPARENT),
                    BrushOption(
                        BrushToolPanel.OPTION_SIZE,
                        ly.img.android.pesdk.ui.brush.R.string.pesdk_brush_button_size,
                        ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_option_align_resize)
                    )
                )
            )
            // highlight-tools

            // By default the default color for the brush stroke is white
            // For this example the default color is set to black
            // highlight-color
            it.setDefaultBrushColor(Color.BLACK)
            // highlight-color

            // By default the editor provides a variety of different colors to customize the color of the brush stroke
            // For this example only a small selection of colors is enabled
            // highlight-colors
            it.setBrushColorList(
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_whiteColor, ColorAsset(-0x1)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_blackColor, ColorAsset(-0x1000000)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_redColor, ColorAsset(-0x18afb0))
            )
            // highlight-colors
        }

        settingsList.configure<BrushSettings> {
            // By default the default brush size is set to 5% of the smaller side of the video
            // For this example, we change it to 8%
            // highlight-size
            it.brushSize = 0.08f
            // highlight-size
        }

        // Start the video editor using VideoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        VideoEditorBuilder(activity)
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)

        // Release the SettingsList once done
        settingsList.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
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