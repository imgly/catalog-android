package ly.img.catalog.examples.text.pesdk

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.layer.TextGlLayer
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.ColorAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigText
import ly.img.android.pesdk.ui.panels.TextOptionToolPanel
import ly.img.android.pesdk.ui.panels.item.ColorItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoTextConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        settingsList.configure<UiConfigText> {
            // By default the text alignment is set to `Paint.Align.CENTER`
            // In this example, the default text alignment is set to `Paint.Align.LEFT`
            // highlight-text-align
            it.setDefaultTextAlignment(Paint.Align.LEFT)
            // highlight-text-align

            // By default all available text tools are enabled
            // For this example only a couple are enabled
            // highlight-tools
            it.optionList.set(it.optionList.filter { item ->
                item.id == TextOptionToolPanel.OPTION_FONT
                    || item.id == TextOptionToolPanel.OPTION_COLOR
            })
            // highlight-tools

            // By default the editor provides a variety of different colors to customize the color of the text
            // For this example only a small selection of colors is shown by default e.g. based on favorite colors of the user
            // highlight-colors
            it.setTextColorList(
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_whiteColor, ColorAsset(-0x1)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_blackColor, ColorAsset(-0x1000000))
            )
            // highlight-colors

            // By default the editor provides a variety of different colors to customize the background color of the text
            // For this example only a small selection of colors is shown by default e.g. based on favorite colors of the user
            // highlight-background-colors
            it.setTextBackgroundColorList(
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_redColor, ColorAsset(-0x18afb0)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_yellowColor, ColorAsset(-0x89d)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_blueColor, ColorAsset(-0x997901))
            )
            // highlight-background-colors
        }

        // By default, the editor does not allow to add emojis as text input since they are device specific and have high rendering time
        // Since emojis are not cross-platform compatible, using the serialization feature to share edits across different platforms
        // will result in emojis being rendered with the system's local set of emojis and therefore will appear differently
        // For this example emoji input is enabled
        // highlight-emojis
        TextGlLayer.ALLOW_SYSTEM_EMOJI = true
        // highlight-emojis

        // Start the photo editor using PhotoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        PhotoEditorBuilder(activity)
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
        resetDefaultValues()
    }

    // We do not want the static values set in this example to affect other examples in this app so we reset them to their defaults
    private fun resetDefaultValues() {
        TextGlLayer.ALLOW_SYSTEM_EMOJI = false
    }
}
// <code-region>