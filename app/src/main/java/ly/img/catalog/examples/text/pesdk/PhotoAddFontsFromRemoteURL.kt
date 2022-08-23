package ly.img.catalog.examples.text.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.FontAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigText
import ly.img.android.pesdk.ui.panels.item.FontItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri
import java.io.File
import java.net.URL

// <code-region>
class PhotoAddFontsFromRemoteURL(private val activity: AppCompatActivity) : Example(activity) {

    // Although the editor supports adding assets with remote URLs, we highly recommend
    // that you manage the download of remote resources yourself, since this
    // gives you more control over the whole process.
    override fun invoke() {
        // highlight-download-file
        activity.lifecycleScope.launch {
            showLoader(true)
            val file = withContext(Dispatchers.IO) {
                runCatching {
                    // Create file in cache directory
                    val file = File(activity.cacheDir, "raleway.ttf")
                    if (file.exists()) file.delete()
                    file.createNewFile()
                    // Save remote resource to file
                    URL("https://img.ly/static/example-assets/custom_font_raleway_regular.ttf").openStream().use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    file
                }.getOrNull()
            }
            showLoader(false)
            file?.let {
                showEditor(it)
            } ?: showMessage("Error downloading the file")
        }
        // highlight-download-file
    }

    private fun showEditor(fontFile: File) {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        // Create a custom font
        // highlight-custom-font
        val customFont = FontAsset("custom_font_raleway_regular", fontFile)
        // highlight-custom-font

        // Add asset to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customFont)
        // highlight-asset-config

        // highlight-text-config
        settingsList.configure<UiConfigText> {
            // Add custom font to the UI
            it.fontList.add(FontItem("custom_font_raleway_regular", "Raleway"))
        }
        // highlight-text-config

        // Start the photo editor using PhotoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        PhotoEditorBuilder(activity)
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