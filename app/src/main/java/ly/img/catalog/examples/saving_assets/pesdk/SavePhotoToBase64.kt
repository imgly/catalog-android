package ly.img.catalog.examples.saving_assets.pesdk

import android.content.Intent
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class SavePhotoToBase64(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // highlight-setup
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }
        // Start the photo editor using PhotoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        PhotoEditorBuilder(activity)
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
                EditorSDKResult.Status.EXPORT_DONE -> {
                    activity.lifecycleScope.launch {
                        showLoader(true)
                        val base64String = withContext(Dispatchers.IO) {
                            runCatching {
                                val bytes = activity.contentResolver.openInputStream(result.resultUri!!)?.readBytes()
                                Base64.encodeToString(bytes, Base64.DEFAULT)
                            }.getOrNull()
                        }
                        showLoader(false)
                        base64String?.let {
                            showMessage("Received Base64 encoded string with ${it.length} characters")
                        } ?: showMessage("Error encoding to Base64")
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