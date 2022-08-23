package ly.img.catalog.examples.opening_assets.vesdk

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.catalog.examples.Example

// <code-region>
class OpenVideoFromCamera(private val activity: AppCompatActivity) : Example(activity) {

    companion object {
        private const val CAMERA_REQUEST_CODE = 0x69
    }

    override fun invoke() {
        // highlight-open-camera
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            activity.startActivityForResult(takeVideoIntent, CAMERA_REQUEST_CODE)
        } catch (ex: ActivityNotFoundException) {
            showMessage("No Camera app installed")
        }
        // highlight-open-camera
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            EDITOR_REQUEST_CODE -> {
                intent ?: return
                val result = EditorSDKResult(intent)
                when (result.resultStatus) {
                    EditorSDKResult.Status.CANCELED -> showMessage("Editor cancelled")
                    EditorSDKResult.Status.EXPORT_DONE -> showMessage("Result saved at ${result.resultUri}")
                    else -> {
                    }
                }
            }
            // highlight-camera-result
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    intent?.data?.let { showEditor(it) } ?: showMessage("Invalid Uri")
                }
            }
            // highlight-camera-result
        }
    }

    // highlight-editor
    private fun showEditor(uri: Uri) {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = uri
            }
        // Start the video editor using VideoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        VideoEditorBuilder(activity)
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)
        // Release the SettingsList once done
        settingsList.release()
    }
    // highlight-editor
}
// <code-region>