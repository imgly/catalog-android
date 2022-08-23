package ly.img.catalog.examples.trim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.TrimSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri
import java.util.concurrent.TimeUnit

// <code-region>
class TrimEnforceDuration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // The min/max length limits are also respected by the VideoCompositionToolPanel.
        settingsList.configure<TrimSettings> {
            // By default the editor does not allow videos shorter than 0.5 seconds. For this example the duration is set,
            // e.g. for a social media application where the posts are not allowed to be shorter than 2 seconds.
            // highlight-min-max
            it.setMinimumVideoLength(2, TimeUnit.SECONDS)
            // By default the editor does not have a maximum duration. For this example the duration is set,
            // e.g. for a social media application where the posts are not allowed to be longer than 5 seconds.
            it.setMaximumVideoLength(5, TimeUnit.SECONDS)
            // highlight-min-max

            // By default the editor trims the video automatically if it is longer than the specified maximum duration.
            // For this example, the user is prompted to review and adjust the automatically trimmed video.
            // highlight-force-trim
            it.forceTrimMode = TrimSettings.ForceTrim.IF_NEEDED
            // highlight-force-trim
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