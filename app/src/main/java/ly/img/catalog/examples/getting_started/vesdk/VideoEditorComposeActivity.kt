package ly.img.catalog.examples.getting_started.vesdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorActivityResultContract
import ly.img.catalog.R
import ly.img.catalog.resourceUri
import ly.img.catalog.showMessage

// <code-region>
class VideoEditorComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // highlight-handle-result
            val editorLauncher = rememberLauncherForActivityResult(
                contract = VideoEditorActivityResultContract(),
                onResult = { result ->
                    when (result.resultStatus) {
                        EditorSDKResult.Status.CANCELED -> showMessage("Editor cancelled")
                        EditorSDKResult.Status.EXPORT_DONE -> showMessage("Result saved at ${result.resultUri}")
                        else -> {
                        }
                    }
                }
            )
            // highlight-handle-result

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
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
                        editorLauncher.launch(settingsList)
                        // Release the SettingsList once done
                        settingsList.release()
                        // highlight-start-editor
                    },
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(text = "Start", color = Color.White)
                }
            }
        }
    }
}
// <code-region>