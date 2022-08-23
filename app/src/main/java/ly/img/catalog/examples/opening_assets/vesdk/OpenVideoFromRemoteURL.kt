package ly.img.catalog.examples.opening_assets.vesdk

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.catalog.examples.Example
import java.io.File
import java.net.URL

// <code-region>
class OpenVideoFromRemoteURL(private val activity: AppCompatActivity) : Example(activity) {

    // Although the editor supports adding assets with remote URLs, we highly recommend
    // that you manage the download of remote resources yourself, since this
    // gives you more control over the whole process.
    override fun invoke() {
        // highlight-download-file
        activity.lifecycleScope.launch {
            showLoader(true)
            val file = withContext(Dispatchers.IO) {
                runCatching {
                    val file = File.createTempFile("imgly_video", ".mp4")
                    URL("https://img.ly/static/example-assets/Skater.mp4").openStream().use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    file
                }.getOrNull()
            }
            showLoader(false)
            file?.let {
                showEditor(Uri.fromFile(it))
            } ?: showMessage("Error downloading the file")
        }
        // highlight-download-file
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