package ly.img.catalog.examples.frame.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigFrame
import ly.img.android.pesdk.ui.panels.item.FrameOption
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoFrameConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        settingsList.configure<UiConfigFrame> {
            // By default all available frame tools are enabled
            // For this example only a couple are enabled
            // highlight-tools
            val tools = listOf(
                FrameOption(
                    FrameOption.OPTION_REPLACE,
                    ly.img.android.pesdk.ui.frame.R.string.pesdk_frame_button_replace,
                    ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_replace)
                ),
                FrameOption(
                    FrameOption.OPTION_OPACITY,
                    ly.img.android.pesdk.ui.frame.R.string.pesdk_frame_button_opacity,
                    ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_option_opacity)
                )
            )
            it.optionList.set(tools)
            // highlight-tools

            // By default the editor has all available overlay actions for this tool enabled
            // For this example, we remove all the actions
            // highlight-actions
            it.quickOptionsList.clear()
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