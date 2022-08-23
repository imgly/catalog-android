package ly.img.catalog.examples.saving_assets.vesdk

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.constant.OutputMode
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.VideoEditorSaveSettings
import ly.img.android.pesdk.backend.operator.headless.DocumentRenderWorker
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class SaveVideoInBackground(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // highlight-setup
        // In this example, we need access to the Uri(s) after the editor is closed
        // so we pass true in the constructor
        val settingsList = VideoEditorSettingsList(true)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }
            // Export only SettingsList, we use this SettingsList later to export in the background
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

        // highlight-observe
        WorkManager.getInstance(activity)
            .getWorkInfosByTagLiveData(DocumentRenderWorker.DEFAULT_WORK_INFO_TAG)
            .observe(activity) {
                it.forEach { job ->
                    Log.d("IMG.LY", "State: ${job.state} Progress: ${job.progress.getFloat(DocumentRenderWorker.FLOAT_PROGRESS_KEY, 1f)}")
                }
            }
        // highlight-observe
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
                    // Export the video in background using WorkManager
                    result.settingsList.use { document ->
                        WorkManager.getInstance(activity).enqueue(DocumentRenderWorker.createWorker(document))
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