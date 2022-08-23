package ly.img.catalog.examples.textdesign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.ColorAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigTextDesign
import ly.img.android.pesdk.ui.panels.item.ColorItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoTextDesignConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        settingsList.configure<UiConfigTextDesign> { textDesignConfig ->
            // By default the editor provides a variety of different colors to customize the color of the text design
            // For this example only a small selection of colors is enabled
            // highlight-colors
            textDesignConfig.setTextColorList(
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_whiteColor, ColorAsset(-0x1)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_blackColor, ColorAsset(-0x1000000))
            )
            // highlight-colors

            // By default the editor has all available overlay actions for this tool enabled
            // For this example the last 3 items (space, undo, redo) are removed
            // highlight-actions
            repeat(3) {
                textDesignConfig.quickOptionList.removeLast()
            }
            // highlight-actions
        }

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
    }
}
// <code-region>