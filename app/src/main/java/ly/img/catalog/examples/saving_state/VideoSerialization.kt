package ly.img.catalog.examples.saving_state

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.constant.OutputMode
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.VideoEditorSaveSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.serializer._3.IMGLYFileWriter
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri
import java.io.File
import java.io.IOException

// <code-region>
class VideoSerialization(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // highlight-setup
        // In this example, we need access to the Uri(s) after the editor is closed
        // so we pass true in the constructor
        val settingsList = VideoEditorSettingsList(true)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }
            // Export only SettingsList, we use this SettingsList later for serialization
            // You can skip this if you want to export the video as well
            .configure<VideoEditorSaveSettings> {
                it.outputMode = OutputMode.EXPORT_ONLY_SETTINGS_LIST
            }
        // Start the video editor using VideoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        VideoEditorBuilder(activity)
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)
        // Release the SettingsList once done
        settingsList.release()
        // highlight-setup
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        intent ?: return
        // highlight-result
        if (requestCode == EDITOR_REQUEST_CODE) {
            val result = EditorSDKResult(intent)
            when (result.resultStatus) {
                EditorSDKResult.Status.CANCELED -> showMessage("Editor cancelled")
                EditorSDKResult.Status.DONE_WITHOUT_EXPORT -> {
                    activity.lifecycleScope.launch(Dispatchers.IO) {
                        result.settingsList.use { settingsList ->
                            try {
                                val file = File(activity.getExternalFilesDir(null), "vesdk.json")
                                if (file.exists()) {
                                    file.delete()
                                }
                                file.createNewFile()
                                // Serialize the settingsList as JSON into the file
                                IMGLYFileWriter(settingsList).writeJson(file)
                                withContext(Dispatchers.Main) {
                                    showMessage("Serialisation saved successfully")
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                                withContext(Dispatchers.Main) {
                                    showMessage("Error saving serialisation")
                                }
                            }
                        }
                    }
                }
                else -> {
                }
            }
        }
        // highlight-result
    }
}
// <code-region>