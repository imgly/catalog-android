package ly.img.catalog.examples.restoring_state

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.serializer._3.IMGLYFileReader
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri
import java.io.File
import java.io.IOException

// <code-region>
class VideoDeserialization(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we need access to the Uri(s) after the editor is closed
        // so we pass true in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        activity.lifecycleScope.launch {
            // highlight-deserialization
            withContext(Dispatchers.IO) {
                val file = File(activity.getExternalFilesDir(null), "vesdk.json")
                if (file.exists()) {
                    try {
                        // Deserialize JSON file and read it into SettingsList
                        IMGLYFileReader(settingsList).readJson(file)
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            showMessage("Error reading serialisation")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showMessage("Serialisation file not found")
                    }
                }
            }
            // highlight-deserialization
            // Start the video editor using VideoEditorBuilder
            // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
            VideoEditorBuilder(activity)
                .setSettingsList(settingsList)
                .startActivityForResult(activity, EDITOR_REQUEST_CODE)
            // Release the SettingsList once done
            settingsList.release()
        }
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
    // <code-region>
}