package ly.img.catalog.examples.user_interface.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigMainMenu
import ly.img.android.pesdk.ui.panels.TransformToolPanel
import ly.img.android.pesdk.ui.panels.item.ToolItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoSingleTool(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }
            // highlight-single-tool
            .configure<UiConfigMainMenu> {
                // By default, single tool use is enabled.
                // it.singleToolUse = false // To disable single tool use
                // Set one of the supported tools to be displayed as single tool
                it.setToolList(
                    ToolItem(
                        TransformToolPanel.TOOL_ID,
                        ly.img.android.pesdk.ui.transform.R.string.pesdk_transform_title_name,
                        ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_transform)
                    )
                )
            }
        // highlight-single-tool

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