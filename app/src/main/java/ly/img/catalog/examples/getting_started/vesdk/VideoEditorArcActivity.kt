package ly.img.catalog.examples.getting_started.vesdk

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorActivityResultContract
import ly.img.catalog.R
import ly.img.catalog.resourceUri
import ly.img.catalog.showMessage

// <code-region>
class VideoEditorArcActivity : AppCompatActivity() {

    // highlight-handle-result
    private val videoEditorResult = registerForActivityResult(VideoEditorActivityResultContract()) {
        when (it.resultStatus) {
            EditorSDKResult.Status.CANCELED -> showMessage("Editor cancelled")
            EditorSDKResult.Status.EXPORT_DONE -> showMessage("Result saved at ${it.resultUri}")
            else -> {
            }
        }
    }
    // highlight-handle-result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arc)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            // highlight-settings-list
            // In this example, we do not need access to the Uri(s) after the editor is closed
            // so we pass false in the constructor
            val settingsList = VideoEditorSettingsList(false)
                .configure<LoadSettings> {
                    // Set the source as the Uri of the video to be loaded
                    it.source = resourceUri(R.raw.skater)
                }
            // highlight-settings-list
            // highlight-start-editor
            videoEditorResult.launch(settingsList)
            // Release the SettingsList once done
            settingsList.release()
            // highlight-start-editor
        }
    }
}
// <code-region>